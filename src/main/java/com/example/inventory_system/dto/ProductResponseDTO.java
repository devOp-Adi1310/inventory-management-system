package com.example.inventory_system.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    // Notice what is MISSING here! 
    // We are deliberately leaving out the 'minThreshold' because the general public 
    // doesn't need to know our internal alert settings.
    
    private Long id;
    private String sku;
    private String name;
    private Double price;
    private String stockStatus; // We will create a custom readable status!
}