package com.emersondev.dao;

import com.emersondev.POJO.Product;
import com.emersondev.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

  List<ProductWrapper> getAllProduct();

  @Modifying
  @Transactional
  void updateProductStatus(@Param("status") String status, @Param("id") Integer id);

  List<ProductWrapper> getByCategory(@Param("id") Integer id);

  ProductWrapper getProductById(@Param("id") Integer id);

  ProductWrapper findByCode(String codigo);

  ProductWrapper findBySerie(String serie);

  List<ProductWrapper> findByWarehouse(@Param("id") Integer id);
}
