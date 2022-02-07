package com.ceti.wholesale.controller.api.request;

import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateMinioDocumentRequest {

    private String objectKey;

    private String bucket;

    private String objectName;

    private String entity;

    private String entityId;

    private String fileExtension;

    private Instant uploadAt;
}
