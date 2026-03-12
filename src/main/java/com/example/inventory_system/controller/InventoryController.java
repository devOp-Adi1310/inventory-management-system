package com.example.inventory_system.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping; // <-- Here is the missing magic line!
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_system.dto.ProductResponseDTO;
import com.example.inventory_system.model.Order;
import com.example.inventory_system.model.Product;
import com.example.inventory_system.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return new ResponseEntity<>(inventoryService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/page")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(inventoryService.getProductsPaginated(page, size), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(inventoryService.addProduct(product), HttpStatus.CREATED);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return new ResponseEntity<>(inventoryService.processOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<Product>> getLowStock() {
        return new ResponseEntity<>(inventoryService.getLowStockAlerts(), HttpStatus.OK);
    }

    // NEW ENDPOINT: Download CSV Report
    @GetMapping(value = "/reports/csv", produces = "text/csv")
    public ResponseEntity<String> downloadInventoryReport() {
        String csvData = inventoryService.generateInventoryReport();
        
        // Tell the browser this is a file to download, not a webpage to display
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=supply_chain_report.csv");
        
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }
}