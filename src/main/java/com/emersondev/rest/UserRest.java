package com.emersondev.rest;

import com.emersondev.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path = "/user")
public interface UserRest {

  @PostMapping(path = "/signup")
  public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

  @PostMapping(path = "/login")
  public ResponseEntity<String> login(@Validated @RequestBody Map<String, String> requestMap);

  @GetMapping(path = "/get")
  public ResponseEntity<List<UserWrapper>> getAllUsers();

  @PostMapping(path = "/updateUser")
  public ResponseEntity<String> updateUser(@Validated @RequestBody Map<String, String> requestMap);

  @PostMapping(path = "/update/status")
  public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap);

  @GetMapping(path = "/checkToken")
  public ResponseEntity<String> checkToken();

  @PostMapping(path = "/changePassword")
  public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

  @PostMapping(path =  "/forgotPassword")
  public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requesMap);
}
