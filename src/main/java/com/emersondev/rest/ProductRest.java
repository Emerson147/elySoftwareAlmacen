package com.emersondev.rest;

import com.emersondev.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {

  @PostMapping(path = "/add")
  public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);

  @GetMapping(path = "get")
  public ResponseEntity<List<ProductWrapper>> getAllProduct();

  @PostMapping(path = "/update")
  public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap);

  @PostMapping(path = "/delete/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable Integer id);

  @PostMapping(path = "/updateStatus")
  public ResponseEntity<String> updateProductStatus(@RequestBody Map<String, String> requestMap);

  @GetMapping(path = "getByCategory/{id}")
  public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

  @GetMapping(path = "getProductById/{id}")
  public ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);

}
