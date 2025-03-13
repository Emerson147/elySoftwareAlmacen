package com.emersondev.restlmpl;

import com.emersondev.contansts.CafeConstants;
import com.emersondev.rest.ProductRest;
import com.emersondev.service.ProductService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

  @Autowired
  ProductService productService;

  @Override
  public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
    try {
      return productService.addNewProduct(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getAllProduct() {
    try {
      return productService.getAllProduct();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
    try {
      return productService.updateProduct(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> deleteProduct(Integer id) {
    try {
      return productService.deleteProduct(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
    try {
      return productService.updateProductStatus(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
    try {
      return productService.getByCategory(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<ProductWrapper> getProductById(Integer id) {
    try {
      return productService.getProductById(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<ProductWrapper> getProductByCode(String codigo) {
    try {
      return productService.getProductByCode(codigo);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<ProductWrapper> getProductBySerie(String serie) {
    try {
      return productService.getProductBySerie(serie);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getProductByWarehouse(Integer id) {
    try {
      return productService.getProductByWarehouse(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
