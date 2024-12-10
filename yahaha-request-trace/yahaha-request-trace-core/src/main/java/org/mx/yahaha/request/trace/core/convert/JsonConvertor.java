package org.mx.yahaha.request.trace.core.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author FizzPu
 * @since 2024/12/9 下午3:13
 */
public class JsonConvertor {
  public final ObjectMapper API_OBJECT_MAPPER;
  private static final JsonConvertor me = new JsonConvertor();
  
  private JsonConvertor() {
    API_OBJECT_MAPPER = new ObjectMapper();
    
    API_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    API_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    
    API_OBJECT_MAPPER.setConfig(API_OBJECT_MAPPER.getDeserializationConfig()
      .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
    API_OBJECT_MAPPER.setConfig(API_OBJECT_MAPPER.getSerializationConfig()
      .with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
      .without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
  }
  
  public static JsonConvertor me() {
    return me;
  }
  
  public <T> T fromJson(Class<T> metadata, String json) throws IOException {
    return API_OBJECT_MAPPER.readValue(json, metadata);
  }
  
  public String toJson(Object ob) throws JsonProcessingException {
    return API_OBJECT_MAPPER.writeValueAsString(ob);
  }
}
