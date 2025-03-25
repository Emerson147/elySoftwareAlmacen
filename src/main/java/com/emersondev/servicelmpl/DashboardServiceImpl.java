package com.emersondev.servicelmpl;

import com.emersondev.dao.*;
import com.emersondev.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {

  @Autowired
  CategoryDao categoryDao;

  @Autowired
  ProductDao productDao;

  @Autowired
  BillDao billDao;
  @Autowired
  private WarehouseDao warehouseDao;
  @Autowired
  private UserDao userDao;

  @Override
  public ResponseEntity<Map<String, Object>> getCOunt() {
    System.out.println("Inside getCount");

    Map<String, Object> map = new HashMap<>();

    map.put("category", Optional.of(categoryDao.count()));
    map.put("product", Optional.of(productDao.count()));
    map.put("bill", Optional.of(billDao.count()));
    map.put("warehouse", Optional.of(warehouseDao.count()));
    map.put("users", Optional.of(userDao.count()));

    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
