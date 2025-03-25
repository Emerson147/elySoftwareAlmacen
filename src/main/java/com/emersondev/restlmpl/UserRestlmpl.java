package com.emersondev.restlmpl;

import com.emersondev.contansts.CafeConstants;
import com.emersondev.rest.UserRest;
import com.emersondev.service.UserService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.wrapper.UserWrapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestlmpl implements UserRest {

  @Autowired
  UserService userService;

  /**
   * Este método maneja la solicitud de registro de un usuario.
   * Intenta llamar al método signUp del servicio de usuarios con el mapa de solicitud proporcionado.
   * Si el proceso se completa con éxito, devuelve la respuesta del método signUp del servicio de usuarios.
   * Si ocurre alguna excepción durante el proceso, se imprime la traza de la pila de la excepción y se devuelve una respuesta con un estado de INTERNAL_SERVER_ERROR.
   *
   * @param requestMap Un mapa que contiene los datos de registro del usuario.
   * @return Una respuesta con el estado del registro.
   */
  @Override
  public ResponseEntity<String> signUp(Map<String, String> requestMap) {
    try {
      return userService.signUp(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> login(Map<String, String> requestMap) {
    try {
      return userService.login(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @Override
  public ResponseEntity<List<UserWrapper>> getAllUsers() {
    try {
      return userService.getAllUsers();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
    try {
      return userService.updateUser(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
    try {
      return userService.updateStatus(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> checkToken() {
    try {
      return userService.checkToken();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
    try {
      return userService.changePassword(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    try {
      return userService.forgotPassword(requestMap);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}