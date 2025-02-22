package com.emersondev.restlmpl;

import com.emersondev.POJO.Bill;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.rest.BillRest;
import com.emersondev.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

  @Autowired
  private BillService billService;

  @Override
  public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
    try {
      return billService.generateReport(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
