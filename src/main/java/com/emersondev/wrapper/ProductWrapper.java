package com.emersondev.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductWrapper {

  Integer id;
  String codigo;
  String name;
  String description;
  Integer price;
  String status;
  Integer categoryId;
  String categoryName;
  String serie;
  Integer stock;
  Integer warehouseId;
  String warehouseName;

  public ProductWrapper(Integer id, String codigo, String name, String description, Integer price, String status, Integer categoryId, String categoryName, String serie, Integer stock, Integer warehouseId, String warehouseName) {
    this.id = id;
    this.codigo = codigo;
    this.name = name;
    this.description = description;
    this.price = price;
    this.status = status;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.serie = serie;
    this.stock = stock;
    this.warehouseId = warehouseId;
    this.warehouseName = warehouseName;
  }

  public ProductWrapper(Integer id, String name, String description,Integer price, Integer categoryId, String categoryName, String status) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.status = status;
  }

  public ProductWrapper(Integer id, String name, String description, Integer price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  // Constructor para getProductById
  public ProductWrapper(Integer id, String codigo, String name, String description, Integer price,
                        String serie, Integer stock, Integer warehouseId, String warehouseName) {
    this.id = id;
    this.codigo = codigo;
    this.name = name;
    this.description = description;
    this.price = price;
    this.serie = serie;
    this.stock = stock;
    this.warehouseId = warehouseId;
    this.warehouseName = warehouseName;
  }
}
