package com.ceti.wholesale.controller.api.request;

import com.ceti.wholesale.common.util.VNCharacterUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateProductTypeRequest {
    @NotNull(message = "id can not be null")
    @Length(max = 32, message = "id must be less than 32 digits")
    private String id;

    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    public String getId() {
        return VNCharacterUtils.removeAccent(id);
    }

    public String getName() {
        return name != null ? name.trim() : null;
    }
}
