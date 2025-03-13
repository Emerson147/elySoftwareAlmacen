package com.emersondev.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct", query = "select new com.emersondev.wrapper.ProductWrapper(u.id, u.codigo, u.name , u.description , u.price, u.status ,u.category.id, u.category.name, u.serie, u.stock, u.warehouse.id, u.warehouse.name ) from Product u")
@NamedQuery(name = "Product.updateProductStatus", query = "update Product u set u.status=:status where u.id=:id")
@NamedQuery(name = "Product.getByCategory", query = "select new com.emersondev.wrapper.ProductWrapper(u.id, u.codigo, u.name , u.description , u.price, u.status ,u.category.id, u.category.name, u.serie, u.stock, u.warehouse.id, u.warehouse.name) from Product u where u.category.id=:id and u.status='true'" )
@NamedQuery(name = "Product.getProductById", query = "select new com.emersondev.wrapper.ProductWrapper(u.id, u.codigo, u.name , u.description , u.price, u.status ,u.category.id, u.category.name, u.serie, u.stock, u.warehouse.id, u.warehouse.name) from Product u where u.id=:id")
@NamedQuery(name = "Product.findByCode", query = "select new com.emersondev.wrapper.ProductWrapper(u.id , u.codigo , u.name , u.description , u.price , u.serie , u.stock , u.warehouse.id , u.warehouse.name) from Product u where u.codigo=:codigo")
@NamedQuery(name = "Product.findBySerie", query = "select new com.emersondev.wrapper.ProductWrapper(u.id , u.codigo , u.name , u.description , u.price, u.status, u.category.id, u.category.name , u.serie , u.stock , u.warehouse.id , u.warehouse.name) from Product u where u.serie=:serie")
@NamedQuery(name = "Product.findByWarehouse", query = "select new com.emersondev.wrapper.ProductWrapper(u.id , u.codigo , u.name , u.description , u.price , u.status , u.category.id , u.category.name , u.serie , u.stock , u.warehouse.id , u.warehouse.name) from Product u where u.warehouse.id=:id")

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "product")
public class Product implements Serializable {

  private static final long serialVersionUID = 123456L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "codigo", unique = true)
  private String codigo;

  @Column(name = "name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_fk", nullable = false)
  private Category category;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private Integer price;

  @Column(name = "status")
  private String status;

  @Column(name = "serie")
  private String serie;

  @Column(name = "stock")
  private Integer stock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_fk")
  private Warehouse warehouse;
}
