package com.emersondev.servicelmpl;

import com.emersondev.dao.BillDao;
import com.emersondev.dao.CategoryDao;
import com.emersondev.dao.ProductDao;
import com.emersondev.dao.WarehouseDao;
import com.emersondev.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

  @Override
  public ResponseEntity<Map<String, Object>> getCOunt() {
    System.out.println("Inside getCount");

    Map<String, Object> map = new HashMap<>();

    map.put("category", categoryDao.count());
    map.put("product", productDao.count());
    map.put("bill", billDao.count());
    map.put("warehouse", warehouseDao.count());

    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
