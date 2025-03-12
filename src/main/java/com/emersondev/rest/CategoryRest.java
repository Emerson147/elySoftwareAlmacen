package com.emersondev.rest;

import com.emersondev.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {

  @PostMapping(path = "/add")
  public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);

  @GetMapping(path = "/get")
  public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String Value);

  @PostMapping(path = "/update/{id}")
  public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);

  @PostMapping(path = "delete/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Integer id);
}

