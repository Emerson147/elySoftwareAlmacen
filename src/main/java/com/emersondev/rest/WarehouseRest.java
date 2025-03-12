package com.emersondev.rest;

import com.emersondev.wrapper.WarehouseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/warehouse")
public interface WarehouseRest {

  @PostMapping(path = "/add")
  public ResponseEntity<String> addNewWarehouse(@RequestBody Map<String, String> requestMap);

  @GetMapping(path = "/get")
  public ResponseEntity<List<WarehouseWrapper>> getAllWarehouses();

  @PostMapping(path = "/update")
  public ResponseEntity<String> updateWarehouse(@RequestBody Map<String, String> requestMap);

  @PostMapping(path = "/delete/{id}")
  public ResponseEntity<String> deleteWarehouse(@PathVariable("id") Integer id);

  @GetMapping(path = "/get/{id}")
  public ResponseEntity<WarehouseWrapper> getWarehouseById(@PathVariable("id") Integer id);
}
