package com.testvagrant.ekam.api.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.testvagrant.ekam.commons.io.GsonParser;
import com.testvagrant.ekam.commons.parsers.SystemPropertyParser;
import com.testvagrant.ekam.config.models.ConfigKeys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.testvagrant.ekam.commons.io.FileUtilities.fileUtils;

@SuppressWarnings("unchecked")
public class ApiHostsModule extends AbstractModule {

  @Override
  public void configure() {
    Map<String, String> envProps = loadApiTestFeed();
    Names.bindProperties(binder(), SystemPropertyParser.parse(envProps));
  }

  private Map<String, String> loadApiTestFeed() {
    String env = System.getProperty(ConfigKeys.Env.API_ENV, System.getProperty("env", ""));
    String fileName =
        System.getProperty(ConfigKeys.Api.HOSTS).replaceAll(".json", "").trim().concat(".json");

    Optional<File> file = fileUtils().findResource(fileName, env);

    if (file.isPresent()) {
      return new GsonParser().deserialize(file.get().getAbsolutePath(), Map.class);
    }

    final InputStream inputStream = ApiHostsModule.class.getResourceAsStream(fileName);
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
