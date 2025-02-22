package com.emersondev.POJO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email=:email")

@NamedQuery(name = "User.getAllUsers", query = "select new com.emersondev.wrapper.UserWrapper(u.id, u.status, u.contactnumber, u.email, u.name) from User u where u.role='user'")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")

@Data
//@Setter
//@Getter
//@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "contactnumber")
  private String contactnumber;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "status")
  private String status;

  @Column(name = "role")
  private String role;

}
