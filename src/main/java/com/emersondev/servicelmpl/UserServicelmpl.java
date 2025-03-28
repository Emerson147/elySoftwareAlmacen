package com.emersondev.servicelmpl;

import com.emersondev.JWT.CustomerUserDetailsService;
import com.emersondev.JWT.JwtFilter;
import com.emersondev.JWT.JwtUtil;
import com.emersondev.POJO.User;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.dao.UserDao;
import com.emersondev.service.UserService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.utils.EmailUtil;
import com.emersondev.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.emersondev.utils.CafeUtils.getResponseEntity;

@Slf4j
@Service
public class UserServicelmpl implements UserService {

  @Autowired
  UserDao userDao;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  CustomerUserDetailsService customerUserDetailsService;

  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  JwtFilter jwtFilter;

  @Autowired
  EmailUtil emailUtil;

  @Autowired
  PasswordEncoder passwordEncoder;

  //Este método maneja la solicitud de registro de un usuario.
  @Override
  public ResponseEntity<String> signUp(Map<String, String> requestMap) {
    log.info("Inside signup {}", requestMap);
    try {
      if (validateSignUpMap(requestMap)) {
        User user = userDao.findByEmailId(requestMap.get("email"));
        if (Objects.isNull(user)) {
          userDao.save(getUserFromMap(requestMap));
          return getResponseEntity("Successfully  Registered.", HttpStatus.OK);
        } else {
          return getResponseEntity("Email already exits.", HttpStatus.BAD_REQUEST);
        }
      } else {
        return getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**Este método valida el mapa de solicitud de registro. **/
  private boolean validateSignUpMap(Map<String, String> requestMap) {
    return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
            && requestMap.containsKey("password") && requestMap.containsKey("role");
  }

  /** Este método crea un objeto de usuario a partir del mapa de solicitud. **/
  private User getUserFromMap(Map<String, String> requestMap) {
    User user = new User();
    user.setName(requestMap.get("name"));
    user.setContactNumber(requestMap.get("contactNumber"));
    user.setEmail(requestMap.get("email"));
    //Hashea el password antes de guardarlo en la base de datos
    user.setPassword(passwordEncoder.encode(requestMap.get("password")));
    user.setStatus(requestMap.get("status"));
    user.setRole(requestMap.get("role"));

    return user;

  }

  /** Este método maneja la solicitud de inicio de sesión de un usuario. **/
  @Override
  public ResponseEntity<String> login(Map<String, String> requestMap) {
    log.info("Inside login {}", requestMap);
    try {
      Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      requestMap.get("email"),
                      requestMap.get("password")
              )
      );

      if (auth.isAuthenticated()) {
        User user = customerUserDetailsService.getUserDetails();
        if ("true".equalsIgnoreCase(user.getStatus())) {
          String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
//          Map<String, String> responseMap = new HashMap<>();
//          responseMap.put("token", token);
//          responseMap.put("role", user.getRole());
          return ResponseEntity.ok()
                  .body("{\"token\":\"" + token + "\",\"role\":\"" + user.getRole() + "\"}");
        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                  .body("{\"message\":\"Wait for Admin Approval.\"}");
        }
      }
    } catch (BadCredentialsException e) {
      log.error("Bad credentials for user: {}", requestMap.get("email"));
    } catch (Exception ex) {
      log.error("Login error: ", ex);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("{\"message\":\"Bad Credentials.\"}");
  }

  /** Este método lista todos los usuarios siendo Admin**/
  @Override
  public ResponseEntity<List<UserWrapper>> getAllUsers() {
    try {
      if (jwtFilter.isAdmin()) {
        return new ResponseEntity<>(userDao.getAllUsers(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
    try {
      if (!jwtFilter.isAdmin()) {
        return getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }

      if (!validateUpdateUserMap(requestMap)) {
        return getResponseEntity("Invalid Data", HttpStatus.BAD_REQUEST);
      }

      //Parse ID properly
      int userId;
      try {
        userId = Integer.parseInt(requestMap.get("id"));
      } catch (NumberFormatException ex) {
        return getResponseEntity("Invalid User ID", HttpStatus.BAD_REQUEST);
      }

      Optional<User> optional = userDao.findById(Integer.valueOf(userId));
      if (optional.isPresent()) {
        User user = optional.get();
        updateUserFields(user, requestMap);
        userDao.save(user);

        return getResponseEntity("User Updated Successfully", HttpStatus.OK);
      } else {
        return getResponseEntity("User doesn't exist", HttpStatus.NOT_FOUND);
      }

    } catch (Exception ex) {
      log.error("Update user error: ", ex);
      return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean validateUpdateUserMap(Map<String, String> requestMap) {
    return requestMap.containsKey("id") && requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
            && requestMap.containsKey("email") && requestMap.containsKey("role") && requestMap.containsKey("status");
  }

  private void updateUserFields(User user, Map<String, String> requestMap) {
    user.setName(requestMap.get("name"));
    user.setContactNumber(requestMap.get("contactNumber"));
    user.setEmail(requestMap.get("email"));
    user.setRole(requestMap.get("role"));
    user.setStatus(requestMap.get("status"));
  }

  @Override
  public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
    try {
      if (jwtFilter.isAdmin()) {
        Optional<User> optional = userDao.findById(Integer.valueOf(Integer.parseInt(requestMap.get("id"))));
        if (!optional.isEmpty()) {

          userDao.updateStatus(requestMap.get("status"), Integer.valueOf(Integer.parseInt(requestMap.get("id"))));
          sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
          return getResponseEntity("User Status is updated succesfully", HttpStatus.OK);
        } else {
          return getResponseEntity("User doesn't exists", HttpStatus.OK);
      }
    } else {
        return getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }
  } catch (Exception ex) {
      ex.printStackTrace();
    }
    return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

  private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
    allAdmin.remove(jwtFilter.getCurrentUsername());
    if (status != null && status.equalsIgnoreCase("true")) {
      emailUtil.sendSimpleMessage(jwtFilter.getCurrentUsername(), "Account Aprobed", "User:- " + user + "\n is aprobed by \nAdmin:- " + jwtFilter.getCurrentUsername(), allAdmin);
    } else {
      emailUtil.sendSimpleMessage(jwtFilter.getCurrentUsername(), "Account Disabled", "User:- " + user + "\n is aprobed by \nAdmin:- " + jwtFilter.getCurrentUsername(), allAdmin);
    }
  }

  @Override
  public ResponseEntity<String> checkToken() {
    return getResponseEntity("Token is valid", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
    try {
      // Obtén el usuario actual
      User user = userDao.findByEmail(jwtFilter.getCurrentUsername());

      if (user == null) {
        return getResponseEntity("User not found", HttpStatus.NOT_FOUND);
      }

      // Obtén el PasswordEncoder (debes inyectarlo)
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

      // Verifica la contraseña antigua CON HASHING
      String oldPassword = requestMap.get("oldPassword");
      if (passwordEncoder.matches(oldPassword, user.getPassword())) {
        // Codifica la nueva contraseña
        String newPassword = passwordEncoder.encode(requestMap.get("newPassword"));
        user.setPassword(newPassword);
        userDao.save(user);
        return getResponseEntity("Password Updated Successfully", HttpStatus.OK);
      } else {
        return getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    System.out.println("Inside the forgot password function " + requestMap.get("email"));
    try {
      User user = userDao.findByEmail(requestMap.get("email"));
      System.out.println("User email is: " + user.getEmail());
      if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
        emailUtil.forgetMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
        return getResponseEntity("Check your email for credentials", HttpStatus.OK);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> deleteUser(String id) {
    try {
      log.info("Inside delete user with id: {}", id);
      if (jwtFilter.isAdmin()) {
        log.info("User is not admin");
        User user = userDao.findById(Integer.valueOf(id)).orElse(null);
        if (user != null) {
          log.info("User found, proceeding with deletion: {}", user);
          userDao.deleteById(Integer.valueOf(id));
          return getResponseEntity("User deleted successfully", HttpStatus.OK);
        } else {
          log.warn("User not found with id: {}", id);
          return getResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
      }
      return getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

