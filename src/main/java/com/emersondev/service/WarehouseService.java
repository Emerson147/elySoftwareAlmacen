package com.emersondev.service;

import com.emersondev.wrapper.WarehouseWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface WarehouseService {

  ResponseEntity<String> addWarehouse(Map<String, String> requestMap);

  ResponseEntity<List<WarehouseWrapper>> getAllWarehouses();

  ResponseEntity<String> updateWarehouse(Map<String, String> requestMap);

  ResponseEntity<String> deleteWarehouse(Integer id);

  ResponseEntity<WarehouseWrapper> getAllWarehouseById(Integer id);
}
