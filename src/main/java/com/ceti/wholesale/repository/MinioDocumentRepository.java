package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.model.MinioDocument;

@Repository
public interface MinioDocumentRepository extends JpaRepository<MinioDocument, String> {

    @Query(value = "select * from minio_document where entity_id = :entityId", nativeQuery = true)
    Page<MinioDocument> findAll(String entityId, Pageable pageable);

    @Query(value = "select top 1 * from minio_document where entity_id = :entity_id order by upload_at desc", nativeQuery = true)
    MinioDocument getMinioDocumentLegislationNewest(@Param("entity_id") String entityId);

    @Transactional
    @Modifying
    @Query(value = "delete minio_document where entity_id = :entity_id", nativeQuery = true)
    void deleteByEntityId(@Param("entity_id")String entityId);
}
