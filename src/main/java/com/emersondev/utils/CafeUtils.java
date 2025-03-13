package com.emersondev.utils;

import com.emersondev.wrapper.ProductWrapper;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.*;

@Slf4j
public class CafeUtils {

  private CafeUtils() {

  }

  public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
    return new ResponseEntity<String>("{\"messag\":\"" + responseMessage + "\"}", httpStatus);
  }

//  public static <T> ResponseEntity<T> getDataResponse(T data, HttpStatus httpStatus) {
//    return new ResponseEntity<T>(data, httpStatus);
//  }

  public static Map<String , Object> getMapFromJson(String data){
    if(!Strings.isNullOrEmpty(data))
      return new Gson().fromJson(data , new TypeToken<Map<String , Object>>(){
      }.getType());
    return new HashMap<>();
  }


  public static String getUUID() {
    Date data = new Date();
    long time = data.getTime();
    return "BILL" + time;
  }

  public static JSONArray getJsonArrayFromString(String data) throws JSONException {
    JSONArray jsonArray = new JSONArray(data);
    return jsonArray;
  }

  public static Boolean isFileExist(String path){
    log.info("Inside isFileExist {}" , path);
    try {
      File file = new File(path);
      return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;

    }catch (Exception ex){
      ex.printStackTrace();
    }
    return false;
  }

}
