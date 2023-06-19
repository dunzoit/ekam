package com.testvagrant.ekam.api.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.testvagrant.ekam.commons.parsers.SystemPropertyParser;
import com.testvagrant.ekam.config.models.ConfigKeys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.testvagrant.ekam.logger.EkamLogger.ekamLogger;

@SuppressWarnings("unchecked")
public class ApiHostsModule extends AbstractModule {

  @Override
  public void configure() {
    Map<String, String> envProps = loadApiTestFeed();
    Names.bindProperties(binder(), SystemPropertyParser.parse(envProps));
    ekamLogger().info("Binding hosts {} to Named properties", envProps);
  }

  private Map<String, String> loadApiTestFeed() {
    String fileName =
        System.getProperty(ConfigKeys.Api.HOSTS).replaceAll(".json", "").trim().concat(".json");
    final InputStream inputStream = ApiHostsModule.class.getResourceAsStream("/" + fileName);
    if (inputStream != null) {
      try {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(inputStream, Map.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return new HashMap<>();
  }
}
