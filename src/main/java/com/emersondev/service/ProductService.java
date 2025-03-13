package com.emersondev.service;

import com.emersondev.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

  ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

  ResponseEntity<List<ProductWrapper>> getAllProduct();

  ResponseEntity<String> updateProduct(Map<String, String> requestMap);

  ResponseEntity<String> deleteProduct(Integer id);

  ResponseEntity<String> updateProductStatus(Map<String, String> requestMap);

  ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

  ResponseEntity<ProductWrapper> getProductById(Integer id);

  ResponseEntity<ProductWrapper> getProductByCode(String codigo);

  ResponseEntity<ProductWrapper> getProductBySerie(String serie);

  ResponseEntity<List<ProductWrapper>> getProductByWarehouse(Integer id);
}
