package com.emersondev.servicelmpl;

import com.emersondev.JWT.JwtFilter;
import com.emersondev.POJO.Category;
import com.emersondev.POJO.Product;
import com.emersondev.POJO.Warehouse;
import com.emersondev.contansts.CafeConstants;
import com.emersondev.dao.ProductDao;
import com.emersondev.service.ProductService;
import com.emersondev.utils.CafeUtils;
import com.emersondev.wrapper.ProductWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired
  ProductDao productDao;

  @Autowired
  JwtFilter jwtFilter;

  @Override
  public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
    try {
      if (!jwtFilter.isAdmin()) {
        logger.warn("Intento de acceso no autorizado para agregar producto");
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }

      if (!validateProductMap(requestMap, false)) {
        logger.warn("Datos inválidos para agregar producto: {}", requestMap);
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }

      productDao.save(getProductFromMap(requestMap, false));
      logger.info("Producto agregado exitosamente: {}", requestMap.get("name"));
      return CafeUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);

    } catch (Exception ex) {
      logger.error("Error al agregar producto: {}", ex.getMessage(), ex);
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
    if (validateId && (!requestMap.containsKey("id") || requestMap.get("id").trim().isEmpty())) {
      return false;
    }
    if (!requestMap.containsKey("name") || requestMap.get("name").trim().isEmpty() ||
            !requestMap.containsKey("codigo") || requestMap.get("codigo").trim().isEmpty() ||
            !requestMap.containsKey("categoryId") || requestMap.get("categoryId").trim().isEmpty() ||
            !requestMap.containsKey("price") || requestMap.get("price").trim().isEmpty() ||
            !requestMap.containsKey("serie") || requestMap.get("serie").trim().isEmpty() ||
            !requestMap.containsKey("stock") || requestMap.get("stock").trim().isEmpty() ||
            !requestMap.containsKey("warehouseId") || requestMap.get("warehouseId").trim().isEmpty()) {
      return false;
    }
    return true;
  }

  private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
    Product product = new Product();
    Category category = new Category();
    Warehouse warehouse = new Warehouse();

    try {
      category.setId(Integer.parseInt(requestMap.get("categoryId")));
      warehouse.setId(Integer.parseInt(requestMap.get("warehouseId")));

      product.setCodigo(requestMap.get("codigo"));
      product.setName(requestMap.get("name"));
      product.setDescription(requestMap.get("description"));
      product.setPrice(Integer.parseInt(requestMap.get("price")));
      product.setCategory(category);
      product.setSerie(requestMap.get("serie"));
      product.setStock(Integer.parseInt(requestMap.get("stock")));
      product.setWarehouse(warehouse);

      if (isAdd) {
        product.setId(Integer.parseInt(requestMap.get("id")));
      } else {
        product.setStatus("true");
      }
    } catch (NumberFormatException e) {
      logger.error("Error al parsear valores del requestMap: {}", requestMap, e);
      throw new IllegalArgumentException("Invalid number format in requestMap", e);
    }

    return product;
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getAllProduct() {
    try {
      List<ProductWrapper> products = productDao.getAllProduct();
      logger.info("Productos recuperados exitosamente, total: {}", products.size());
      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception ex) {
      logger.error("Error al obtener todos los productos: {}", ex.getMessage(), ex);
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
    try {
      if (!jwtFilter.isAdmin()) {
        logger.warn("Intento de acceso no autorizado para actualizar producto");
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }

      if (!validateProductMap(requestMap, true)) {
        logger.warn("Datos inválidos recibidos para actualización: {}", requestMap);
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }

      String idStr = requestMap.get("id");
      if (idStr == null || idStr.trim().isEmpty()) {
        logger.warn("ID de producto no proporcionado en el requestMap");
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }

      Integer id;
      try {
        id = Integer.parseInt(idStr);
      } catch (NumberFormatException e) {
        logger.warn("ID de producto inválido: {}", idStr);
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
      }

      Optional<Product> optional = productDao.findById(id);
      if (!optional.isPresent()) {
        logger.info("Producto con ID {} no encontrado", id);
        return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.NOT_FOUND);
      }

      Product product = getProductFromMap(requestMap, true);
      productDao.save(product);
      logger.info("Producto con ID {} actualizado exitosamente", id);
      return CafeUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);

    } catch (Exception ex) {
      logger.error("Error al actualizar producto: {}", ex.getMessage(), ex);
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<String> deleteProduct(Integer id) {
    try {
      if (jwtFilter.isAdmin()) {
        Optional optional = productDao.findById(id);
        if (!optional.isEmpty()) {
          productDao.deleteById(id);
          return CafeUtils.getResponseEntity("Product deleted successfully", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.OK);
      } else {
        return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
    try {
      if (jwtFilter.isAdmin()) {
        Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
        if (!optional.isEmpty()) {
          productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
          return CafeUtils.getResponseEntity("Product Status is updated succesfully", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Product doesn't exist", HttpStatus.OK);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
    logger.info("Intentando obtener productos por categoría ID: {}", id);
    try {
      List<ProductWrapper> products = productDao.getByCategory(id);
      HttpHeaders headers = new HttpHeaders();

      if (products.isEmpty()) {
        logger.warn("No se encontraron productos para la categoría ID: {}", id);
        return new ResponseEntity<>(products,headers, HttpStatus.NOT_FOUND);
      }
      logger.info("Se encontraron {} productos para la categoría ID: {}", products.size(), id);
      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception ex) {
      logger.error("Error al obtener productos por categoría ID {}: {}", id, ex.getMessage(), ex);
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public ResponseEntity<ProductWrapper> getProductById(Integer id) {
    logger.info("Intentando obtener producto por ID: {}", id);
    try {
      ProductWrapper product = productDao.getProductById(id);
      if (product != null) {
        logger.info("Producto encontrado con ID: {}", id);
        return new ResponseEntity<>(product, HttpStatus.OK);
      } else {
        logger.warn("No se encontró producto con ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      logger.error("Error al obtener producto por ID {}: {}", id, ex.getMessage(), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<ProductWrapper> getProductByCode(String codigo) {
    logger.info("Intentando obtener producto por código: {}", codigo);
    try {
      ProductWrapper product = productDao.findByCode(codigo);
      if (product != null) {
        logger.info("Producto encontrado con código: {}", codigo);
        return new ResponseEntity<>(product, HttpStatus.OK);
      } else {
        logger.warn("No se encontró producto con código: {}", codigo);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

    } catch (Exception ex) {
      logger.error("Error al obtener producto por código {}: {}", codigo, ex.getMessage(), ex);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<ProductWrapper> getProductBySerie(String serie) {
    logger.warn("Intentando obtener producto por serie: {}", serie);
    try {
      ProductWrapper product = productDao.findBySerie(serie);
      if (product != null) {
        logger.info("Producto encontrado con serie: {}", serie);
        return new ResponseEntity<>(product, HttpStatus.OK);
      } else {
        logger.warn("No se encontró producto con serie: {}", serie);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      logger.error("Error al obtener producto por serie {}: {}", serie, ex.getMessage(), ex);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<List<ProductWrapper>> getProductByWarehouse(Integer id) {
    try {
      List<ProductWrapper> products = productDao.findByWarehouse(id);

      if (products.isEmpty()) {
        logger.warn("No se encontraron productos para el almacén ID: {}", id);
        return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
      }
      logger.info("Se encontraron {} productos para el almacén ID: {}", products.size(), id);
      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception ex) {
      logger.error("Error al obtener productos por almacén ID {}: {}", id, ex.getMessage(), ex);
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}