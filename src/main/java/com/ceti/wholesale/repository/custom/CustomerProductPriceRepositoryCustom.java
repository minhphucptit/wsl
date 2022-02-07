package com.ceti.wholesale.repository.custom;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.CustomerProductPrice;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface CustomerProductPriceRepositoryCustom {

    ResultPage<CustomerProductPrice> findAll(Pageable pageable, MultiValueMap<String, String> where);

    ResultPage<Object[]> findAllWithEmbed(Pageable pageable, List<String> listEmbedTable,
                                          MultiValueMap<String, String> where);

}
