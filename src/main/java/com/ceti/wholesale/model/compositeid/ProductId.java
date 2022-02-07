package com.ceti.wholesale.model.compositeid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductId implements Serializable {

    private String id;

    private String factoryId;

}
