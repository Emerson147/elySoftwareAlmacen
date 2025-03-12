package com.emersondev.dao;

import com.emersondev.POJO.Warehouse;
import com.emersondev.wrapper.WarehouseWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface WarehouseDao extends JpaRepository<Warehouse, Integer> {

  List<WarehouseWrapper> getAllWarehouses();

}
