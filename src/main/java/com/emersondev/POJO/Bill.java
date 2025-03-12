package com.emersondev.POJO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Bill.getAllBills" , query = "select b from bill b order by b.id desc")
@NamedQuery(name = "Bill.getBillsByUserName" , query = "select b from bill b where b.createdBy=:username order by b.id desc")

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity(name = "bill")
public class Bill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "contactNumber")
  private String contactNumber;

  @Column(name = "paymentMethod")
  private String paymentMethod;

  @Column(name = "total")
  private Integer total;

  @Column(name = "productDetails", columnDefinition = "json")
  private String productDetails;

  @Column(name = "createdBy")
  private String createdBy;

}
