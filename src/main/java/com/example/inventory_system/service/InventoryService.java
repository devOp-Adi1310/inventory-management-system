package com.example.inventory_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory_system.dto.ProductResponseDTO;
import com.example.inventory_system.exception.InventoryException;
import com.example.inventory_system.model.Order;
import com.example.inventory_system.model.Product;
import com.example.inventory_system.repository.OrderRepository;
import com.example.inventory_system.repository.ProductRepository;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public InventoryService(ProductRepository productRepo, OrderRepository orderRepo) {
        this.productRepository = productRepo;
        this.orderRepository = orderRepo;
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getStockQuantity());         
        dto.setMinThreshold(product.getMinThreshold());

        Integer stock = product.getStockQuantity();
        Integer threshold = product.getMinThreshold();

        // Added safe checks just in case a database row has missing/null numbers!
        if (stock == null) {
            dto.setStockStatus("Status Unknown");
        } else if (stock == 0) {
            dto.setStockStatus("Out of Stock");
        } else if (threshold != null && stock <= threshold) {
            dto.setStockStatus("Low Stock - Only " + stock + " left!");
        } else {
            dto.setStockStatus("In Stock");
        }
        
        return dto;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductResponseDTO> getProductsPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::convertToDTO);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Order processOrder(Order order) {
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new InventoryException("Product with ID " + order.getProductId() + " not found."));

        if ("OUTBOUND".equalsIgnoreCase(order.getOrderType())) {
            if (product.getStockQuantity() < order.getQuantity()) {
                throw new InventoryException("Insufficient stock for sale! Current stock: " + product.getStockQuantity());
            }
            product.setStockQuantity(product.getStockQuantity() - order.getQuantity());
        } else if ("INBOUND".equalsIgnoreCase(order.getOrderType())) {
            product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        } else {
            throw new InventoryException("Invalid Order Type. Must be INBOUND or OUTBOUND.");
        }

        productRepository.save(product);
        return orderRepository.save(order);
    }

    public List<Product> getLowStockAlerts() {
        return productRepository.findProductsNeedingRestock();
    }

    // --- UPDATED: BULLETPROOF CSV REPORT GENERATION ---
    public String generateInventoryReport() {
        List<Product> products = productRepository.findAll();
        StringBuilder csv = new StringBuilder();
        
        // 1. Create the Spreadsheet Headers
        csv.append("ID,SKU,Product Name,Unit Price,Current Stock,Minimum Threshold,Supply Chain Status\n");
        
        // 2. Fill in the Data Rows Safely
        for (Product p : products) {
            
            // SAFETY NETS: If a database value is null, give it a safe default value
            String name = p.getName() != null ? p.getName().replace(",", "") : "Unknown Name";
            String sku = p.getSku() != null ? p.getSku() : "UNKNOWN-SKU";
            Double price = p.getPrice() != null ? p.getPrice() : 0.0;
            Integer stock = p.getStockQuantity() != null ? p.getStockQuantity() : 0;
            Integer threshold = p.getMinThreshold() != null ? p.getMinThreshold() : 0;

            // Supply Chain Logic
            String status = "HEALTHY";
            if (stock == 0) {
                status = "CRITICAL: OUT OF STOCK";
            } else if (stock <= threshold) {
                status = "ACTION REQUIRED: REORDER";
            }

            // Append safely
            csv.append(p.getId()).append(",")
               .append(sku).append(",")
               .append(name).append(",")
               .append(price).append(",")
               .append(stock).append(",")
               .append(threshold).append(",")
               .append(status).append("\n");
        }
        
        return csv.toString();
    }
}