package com.emersondev.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.List;


@NamedQuery(name = "Warehouse.getAllWarehouses", query = "select new com.emersondev.wrapper.WarehouseWrapper(w.id , w.name , w.location , w.description) from Warehouse w")

@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "warehouse")
public class Warehouse implements Serializable {

  private static final long serialVersionUID = 123457L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "location")
  private String location;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Product> products;

}
