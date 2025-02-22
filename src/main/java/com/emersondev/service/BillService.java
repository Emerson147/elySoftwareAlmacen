package com.emersondev.service;

import com.emersondev.POJO.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {


  ResponseEntity<String> generateReport(Map<String, Object> requestMap);
}
