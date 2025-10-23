package com.farmplace.digitalmarket.DTO;


import com.farmplace.digitalmarket.enums.Availability;

public record ProductDTO( String productName, Double unitPrice, Integer currentStock,
                         Availability availability) {
}
