package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProducts() {
        return ResponseEntity.ok(List.of(
                Map.of("id", 1, "name", "Bàn phím cơ AKKO", "price", 1500000),
                Map.of("id", 2, "name", "Chuột không dây Logitech", "price", 850000),
                Map.of("id", 3, "name", "Màn hình Dell UltraSharp", "price", 7200000)
        ));
    }
}
