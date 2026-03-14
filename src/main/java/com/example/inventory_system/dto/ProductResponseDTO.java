package com.example.inventory_system.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    
    private Long id;
    private String sku;
    private String name;
    private Double price;
    private String stockStatus;
    private Integer quantity; 
    private Integer minThreshold; 
}