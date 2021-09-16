package com.testvagrant.ekam.commons.parsers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class TestConfigParser {
  final ObjectMapper objectMapper = new ObjectMapper();

  public <T> T loadFeed(String name, String env, Class<T> tClass) {
    final InputStream inputStream = TestConfigParser.class.getResourceAsStream(name + ".json");
    try {
      return objectMapper.readValue(inputStream, tClass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public <T> T loadFeed(String name, String env, Type tType) {
    final InputStream inputStream = TestConfigParser.class.getResourceAsStream(name + ".json");
    try {
      return objectMapper.readValue(inputStream, (JavaType) tType);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
