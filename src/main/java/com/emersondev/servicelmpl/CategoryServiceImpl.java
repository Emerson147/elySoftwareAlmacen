package com.emersondev.servicelmpl;

import com.emersondev.JWT.JwtFilter;
import com.emersondev.POJO.Category;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.dao.CategoryDao;
import com.emersondev.service.CategoryService;
import com.emersondev.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  JwtFilter jwtFilter;

  @Autowired
  private CategoryDao categoryDao;

  @Override
  public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
    log.info("Inside addNewCategory{}", requestMap);
    try {
      if (jwtFilter.isAdmin()) {
        if (validateCategoryMap(requestMap, false)) {
          categoryDao.save(getCategoryFromMap(requestMap, false));
          return CafeUtils.getResponseEntity("Category Added Succesfully", HttpStatus.OK);
        }
      } else {
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.OK);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
    if (requestMap.containsKey("name")) {
      if (requestMap.containsKey("id") && validateId) {
        return true;
      } else if (!validateId) {
        return true;
      }
    }
    return false;
  }

  private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
    Category category = new Category();
    if (isAdd) {
      category.setId(Integer.parseInt(requestMap.get("id")));
    }
    category.setName(requestMap.get("name"));
    category.setDescription(requestMap.get("description"));
    return category;
  }


  @Override
  public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
    try {
      if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
        log.info("Inside if");
        return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
      }
      return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @Override
  public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
    try {
      // verificacion temprana de autorizacion
      if (!jwtFilter.isAdmin()) {
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }

      //Validacion separada para mejor logabilidad
      if (!validateCategoryMap(requestMap, true)) {
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }

      //Manejo segura de conversion numerica
      int categoryId;
      try {
        categoryId = Integer.parseInt(requestMap.get("id"));
      } catch (NumberFormatException e) {
        return CafeUtils.getResponseEntity("Invalid category ID format", HttpStatus.BAD_REQUEST);
      }

      //Uso correcto de Optional y manejo de estados HTTP apropiado
      return categoryDao.findById(categoryId)
              .map(existingCategory -> {
                categoryDao.save(getCategoryFromMap(requestMap, true));
                return CafeUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
              })
              .orElseGet(() -> CafeUtils.getResponseEntity("Category not found", HttpStatus.NOT_FOUND));
    } catch (Exception ex) {
      log.error("Error updating category", ex); // Manejo de logging
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<String> deleteCategory(Integer id) {
    try {
      if (jwtFilter.isAdmin()) {
        if (categoryDao.existsById(id)) {
          categoryDao.deleteById(id);
          return CafeUtils.getResponseEntity("Category deleted successfully", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Category doesn't exist", HttpStatus.NOT_FOUND);
      } else {
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception ex) {
      log.error("Error deleting category", ex); // Manejo de logging
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}