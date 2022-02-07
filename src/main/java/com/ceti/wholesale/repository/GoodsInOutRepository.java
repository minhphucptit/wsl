package com.ceti.wholesale.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.GoodsInOut;

@Repository
public interface GoodsInOutRepository extends JpaRepository<GoodsInOut, String> {

    List<GoodsInOut> getByVoucherIdAndIsMainProduct(String voucherId, Boolean isMainProduct);

    //factory rotation voucher
    List<GoodsInOut> getByVoucherIdAndOutQuantityAndFactoryId(String voucherId, BigDecimal zeroOutQuantity,
                                                              String factoryId);

    List<GoodsInOut> getByVoucherIdAndInQuantityAndFactoryId(String voucherId, BigDecimal zeroInQuantity,
                                                             String factoryId);

    @Modifying
    @Query(value = "DELETE goods_in_out WHERE voucher_id=?", nativeQuery = true)
    void deleteByVoucherId(String voucherId);

    @Transactional
    @Query(value = "SELECT gio.id FROM goods_in_out gio JOIN goods_in_out_maintain_detail giomd ON gio.id = giomd.id  WHERE gio.voucher_id=:voucher_id", nativeQuery = true)
    List<String> findAllId(@Param("voucher_id") String voucherId);

    // tổng số hàng mỗi sản phẩm tối đa có thể bán được hoặc nhập về= tổng số hàng xuất - tổng số hàng về
    @Query(value = "select isnull(sum(xxe_out_quantity),0) - isnull(sum(nxe_in_quantity), 0) - isnull(sum(xbx_out_quantity), 0) as max from goods_in_out as gio "
            + "where gio.voucher_id = :delivery_voucher_id and gio.product_id=:product_id and gio.stt=:stt and gio.is_main_product=1", nativeQuery = true)
    BigDecimal getMaxXBXOutQuantityOrInQuantityOneProduct(@Param("delivery_voucher_id") String deliveryVoucherId, @Param("product_id") String productId, @Param("stt") Integer stt);

    // sau khi tạo phiếu nhập thu hồi thì cập nhật lại hàng về mỗi sản phẩm trên phiếu xuất xe
    @Transactional
    @Modifying
    @Query(value = "update goods_in_out set nxe_in_quantity = isnull((select sum(gio.nxe_in_quantity) as A "
            + "from goods_in_out gio join recall_voucher rc on gio.voucher_id=rc.id "
            + "where rc.delivery_voucher_id=:delivery_voucher_id and gio.product_id=:product_id and gio.stt=:stt and gio.is_main_product = 1), 0) "
            + "where voucher_id=:delivery_voucher_id and product_id=:product_id and stt=:stt", nativeQuery = true)
    void updateNxeInQuantityXXEOneProduct(@Param("delivery_voucher_id") String deliveryVoucherId, @Param("product_id") String productId, @Param("stt") Integer stt);

    // sau khi tạo phiếu xuất xe bán thì cập nhật lại đã bán mỗi sản phẩm trên phiếu xuất xe
    @Transactional
    @Modifying
    @Query(value = "update goods_in_out set xbx_out_quantity = isnull((select sum(gio.out_quantity) as A "
            + "from goods_in_out gio join sold_delivery_voucher sdv on gio.voucher_id=sdv.id "
            + "where sdv.delivery_voucher_id=:delivery_voucher_id and gio.product_id=:product_id and gio.stt=:stt  and gio.is_main_product = 1), 0) "
            + "where voucher_id=:delivery_voucher_id and product_id=:product_id and stt=:stt", nativeQuery = true)
    void updateXBXOutQuantityXXEOneProduct(@Param("delivery_voucher_id") String deliveryVoucherId, @Param("product_id") String productId, @Param("stt") Integer stt);

    void deleteByVoucherIdIn(List<String> voucherIds);

    List<GoodsInOut> findByVoucherId(String deliveryVoucherId);
    
    //lấy list goods in out để gợi ý tạo tạo chiết nạp gas
    @Transactional
    @Modifying
    @Query(value="exec [dbo].[v4/post_inventory_voucher_tran] :factory_id,:voucher_at, :gas_refueling_voucher_id, :is_created", nativeQuery = true)
    List<GoodsInOut> getListSuggestion(@Param("voucher_at") Instant voucherAt, @Param("factory_id") String factoryId,
    		@Param("gas_refueling_voucher_id") String gasRefuelingVoucherId, @Param("is_created") boolean isCreated);

    // sau khi tạo phiếu xuất bán hàng thì cập nhật lại đã bán mỗi sản phẩm ở phiếu thanh toán tương ứng

    @Transactional
    @Modifying
    @Query(value = "declare @table_result table (\n"
    		+ "    quantity decimal(19,2),\n"
    		+ "    product_id varchar(64),\n"
    		+ "    [type] varchar(32))\n"
    		+ "insert into @table_result\n"
    		+ "select sum(quantity) quantity, product_id, max([type]) [type]\n"
    		+ "from(                     select xxe_out_quantity quantity, product_id, [type]\n"
    		+ "        from goods_in_out\n"
    		+ "        where voucher_id=:voucher_id\n"
    		+ "    union all\n"
    		+ "        select -nxe_in_quantity quantity, product_id, 'XBSP' [type]\n"
    		+ "        from goods_in_out gio join sold_voucher sv on gio.voucher_id=sv.payment_voucher_id\n"
    		+ "        where sv.id=:voucher_id and [type] ='NHTL' ) a\n"
    		+ "group by product_id\n"
    		+ "update goods_in_out set goods_in_out.out_quantity=a.quantity from @table_result a\n"
    		+ "where goods_in_out.voucher_id=:voucher_id\n"
    		+ "and goods_in_out.product_id=a.product_id and goods_in_out.[type]=a.[type]\n"
    		+ "\n"
    		+ "update cylinder_debt set cylinder_debt.out_quantity=a.quantity \n"
    		+ "from @table_result a join product_accessory b on a.product_id=b.main_product_id and b.factory_id=:factory_id and b.sub_product_type='VO'\n"
    		+ "where cylinder_debt.voucher_id=:voucher_id\n"
    		+ "and cylinder_debt.product_id=b.sub_product_id and cylinder_debt.[goods_in_out_type]=a.[type] "
    		+ "update sold_voucher set total_receivable= (select sum(out_quantity*(price-discount))\n"
    		+ "from goods_in_out where voucher_id=:voucher_id) where id=:voucher_id ", nativeQuery = true)
    void setOutQuantityOfSoldVoucher(@Param("voucher_id") String voucherId, @Param("factory_id") String factoryId);
    
    @Query(value = "select * from goods_in_out where product_id=:product_id and voucher_id=:voucher_id and [type]='XBSP'", nativeQuery = true)
    public GoodsInOut getGoodsInOutOfSoldVoucher1Product(@Param("voucher_id") String soldVoucherId, @Param("product_id") String productId);
    

}
