package com.mx.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author FizzPu
 * @since 2024/8/18 下午9:57
 */
public class JsonUtils {
  private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
  
  private static final ObjectMapper DEFAULT_OBJECT_MAPPER;
  
  static {
    DEFAULT_OBJECT_MAPPER = new ObjectMapper();
    // 容忍json中出现未知的列
    DEFAULT_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 兼容java中的驼峰的字段名命名
    DEFAULT_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
  }
  
  private JsonUtils() {
    throw new UnsupportedOperationException();
  }
  
  public static String toJson(Object obj) {
    String result = null;
    try {
      result = DEFAULT_OBJECT_MAPPER.writeValueAsString(obj);
    } catch (IOException e) {
      log.error("unexpected error when converting to json");
    }
    return result;
  }
}
