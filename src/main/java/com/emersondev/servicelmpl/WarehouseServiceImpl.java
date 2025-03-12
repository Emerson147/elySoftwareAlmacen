package com.emersondev.servicelmpl;

import com.emersondev.JWT.JwtFilter;
import com.emersondev.POJO.Warehouse;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.dao.UserDao;
import com.emersondev.dao.WarehouseDao;
import com.emersondev.service.WarehouseService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.wrapper.WarehouseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

  @Autowired
  WarehouseDao warehouseDao;

  @Autowired
  JwtFilter jwtFilter;

  @Autowired
  UserDao userDao;

  @Override
  public ResponseEntity<String> addWarehouse(Map<String, String> requestMap) {
    try {
      log.info("Request received: {}", requestMap);
      if (jwtFilter.isAdmin()) {
        log.info("User is admin");
        if (validateWarehouseMap(requestMap, false)) {
          log.info("Warehouse data is valid");
          warehouseDao.save(getWarehouseFromMap(requestMap, false));
          return CafeUtils.getResponseEntity("Almacen agregado correctamente", HttpStatus.OK);
        }
        log.warn("Invalid warehouse data: {}", requestMap);
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }
      log.warn("Unauthorized access attempt");
      return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      log.error("Error adding warehouse", ex);
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean validateWarehouseMap(Map<String, String> requestMap, boolean validateId) {
    if (requestMap.containsKey("name") && requestMap.containsKey("location") && requestMap.containsKey("description")) {
      if (validateId) {
        return requestMap.containsKey("id");
      }
      return true;
    }
    return false;
  }

  private Warehouse getWarehouseFromMap(Map<String, String> requestMap, boolean isUpdate) {
    Warehouse warehouse = new Warehouse();
    if (isUpdate) {
      warehouse.setId(Integer.parseInt(requestMap.get("id")));
    }
    warehouse.setName(requestMap.get("name"));
    warehouse.setLocation(requestMap.get("location"));
    warehouse.setDescription(requestMap.get("description"));
    return warehouse;
  }

  @Override
  public ResponseEntity<List<WarehouseWrapper>> getAllWarehouses() {
    try {
      if (jwtFilter.isAdmin()) {
        return new ResponseEntity<>(warehouseDao.getAllWarehouses(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateWarehouse(Map<String, String> requestMap) {
    try {
      if (jwtFilter.isAdmin()) {
        if (validateWarehouseMap(requestMap, true)) {
          Warehouse warehouse = warehouseDao.findById(Integer.parseInt(requestMap.get("id"))).orElse(null);
          if (warehouse != null) {
            warehouseDao.save(getWarehouseFromMap(requestMap, true));
            return CafeUtils.getResponseEntity("Almacen actualizado correctamente", HttpStatus.OK);
          } else {
            return CafeUtils.getResponseEntity("Almacen no encontrado", HttpStatus.NOT_FOUND);
          }
        }
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }
      return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> deleteWarehouse(Integer id) {
    try {
      log.info("Request to delete warehouse with id: {}", id);
      if (jwtFilter.isAdmin()) {
        log.info("User is admin");
        Warehouse warehouse = warehouseDao.findById(id).orElse(null);
        if (warehouse != null) {
          log.info("Warehouse found, proceding with deletion: {}", warehouse);
          warehouseDao.deleteById(id);
          return CafeUtils.getResponseEntity("Almacen eliminado correctamente", HttpStatus.OK);
        } else {
          log.warn("Warehouse not found");
          return CafeUtils.getResponseEntity("Almacen no encontrado", HttpStatus.NOT_FOUND);
        }
      }
      log.warn("Unauthorized access attempt");
      return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<WarehouseWrapper> getAllWarehouseById(Integer id) {
    try {
      log.info("Request to get warehouse with id: {}", id);
      if (jwtFilter.isAdmin()) {
        log.info("User is admin");
        Warehouse warehouse = warehouseDao.findById(id).orElse(null);
        if (warehouse != null) {
          log.info("Warehouse found: {}", warehouse);
          return new ResponseEntity<>(new WarehouseWrapper(warehouse), HttpStatus.OK);
        } else {
          log.warn("Warehouse not found with id: {}", id);
          return new ResponseEntity<>(new WarehouseWrapper(), HttpStatus.NOT_FOUND);
        }
      }
      log.warn("Unauthorized access attempt");
      return new ResponseEntity<>(new WarehouseWrapper(), HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      log.error("Error getting warehouse by id", ex);
      return new ResponseEntity<>(new WarehouseWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
