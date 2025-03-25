package com.emersondev.service;

import com.emersondev.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

  ResponseEntity<String> signUp(Map<String, String> RequesMap);

  ResponseEntity<String> login(Map<String, String> RequesMap);

  ResponseEntity<List<UserWrapper>> getAllUsers();

  ResponseEntity<String> updateUser(Map<String, String> requestMap);

  ResponseEntity<String> updateStatus(Map<String, String> requestMap);

  ResponseEntity<String> checkToken();

  ResponseEntity<String> changePassword(Map<String, String> requestMap);

  ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

  ResponseEntity<String> deleteUser(String id);


}
