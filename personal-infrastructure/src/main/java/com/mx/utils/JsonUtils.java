package com.mx.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author FizzPu
 * @since 2024/8/18 下午9:57
 */
public class JsonUtils {
  private JsonUtils() {
    throw new UnsupportedOperationException();
  }
  
  public static String toJson(Object obj) {
    Gson gson = new GsonBuilder()
      .disableHtmlEscaping()
      .create();
    return gson.toJson(obj);
  }
}
