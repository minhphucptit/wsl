package com.ceti.wholesale.model.compositeid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;

    private Instant activeAt;
}
