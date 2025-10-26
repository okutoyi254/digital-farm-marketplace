package com.farmplace.digitalmarket.DTO;

import com.farmplace.digitalmarket.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String categoryName;

    private List<ProductInfo> products=new ArrayList<>();
}