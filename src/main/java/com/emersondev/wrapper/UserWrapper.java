package com.emersondev.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

  private Integer id;

  private String  name;

  private String email;

  private String contactnumber;

  private String status;

  public UserWrapper(Integer id, String status, String contactnumber, String email, String name) {
    this.id = id;
    this.status = status;
    this.contactnumber = contactnumber;
    this.email = email;
    this.name = name;
  }
}
