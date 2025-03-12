package com.emersondev.wrapper;

import com.emersondev.POJO.Warehouse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseWrapper {

  private Integer id;
  private String name;
  private String location;
  private String description;

  public WarehouseWrapper(Integer id, String name, String location, String description) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.description = description;
  }

  public WarehouseWrapper(Warehouse warehouse) {
    this.id = warehouse.getId();
    this.name = warehouse.getName();
    this.location = warehouse.getLocation();
    this.description = warehouse.getDescription();
  }
}
