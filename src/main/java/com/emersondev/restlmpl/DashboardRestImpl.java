package com.emersondev.restlmpl;

import com.emersondev.rest.DashboardRest;
import com.emersondev.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardRestImpl implements DashboardRest {

  @Autowired
  DashboardService dashboardService;

  @Override
  public ResponseEntity<Map<String, Object>> getCount() {
    return dashboardService.getCOunt();
  }
}
