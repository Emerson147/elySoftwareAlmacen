package com.emersondev.restlmpl;

import com.emersondev.contansts.CafeConstants;
import com.emersondev.rest.WarehouseRest;
import com.emersondev.service.WarehouseService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.wrapper.WarehouseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class WarehouseRestImpl implements WarehouseRest {

  @Autowired
  WarehouseService warehouseService;

  @Override
  public ResponseEntity<String> addNewWarehouse(Map<String, String> requestMap) {
    try {
      return warehouseService.addWarehouse(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<List<WarehouseWrapper>> getAllWarehouses() {
    try {
      return warehouseService.getAllWarehouses();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateWarehouse(Map<String, String> requestMap) {
    try {
      return warehouseService.updateWarehouse(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> deleteWarehouse(Integer id) {
    try {
      return warehouseService.deleteWarehouse(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<WarehouseWrapper> getWarehouseById(Integer id) {
    try {
      return warehouseService.getAllWarehouseById(id);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new WarehouseWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
