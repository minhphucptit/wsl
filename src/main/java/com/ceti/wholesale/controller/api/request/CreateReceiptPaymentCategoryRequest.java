package com.ceti.wholesale.controller.api.request;

import javax.validation.constraints.NotNull;

import com.ceti.wholesale.common.util.VNCharacterUtils;
import org.hibernate.validator.constraints.Length;

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
public class CreateReceiptPaymentCategoryRequest {
    @NotNull(message = "id can not be null")
    @Length(max = 128, message = "id must be less than 128 digits")
    private String id;

    @NotNull(message = "name can not be null")
    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    @NotNull(message = "type can not be null")
    private Boolean type;

    public String getId() {
        return VNCharacterUtils.removeAccent(id);
    }

    public String getName() {
        return name != null ? name.trim() : null;
    }
}
