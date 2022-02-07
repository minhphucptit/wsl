package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Table(name = "minio_document")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MinioDocument {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "bucket", columnDefinition = "nvarchar")
    private String bucket;

    @Column(name = "object_key", columnDefinition = "nvarchar")
    private String objectKey;

    @Column(name = "object_name", columnDefinition = "nvarchar")
    private String objectName;

    @Column(name = "upload_at")
    private Instant uploadAt;

    @Column(name = "upload_by")
    private String uploadBy;

    @Column(name = "entity", columnDefinition = "nvarchar")
    private String entity;

    @Column(name = "entity_id", columnDefinition = "nvarchar")
    private String entityId;

    @Column(name = "file_extension", columnDefinition = "nvarchar")
    private String fileExtension;
}
