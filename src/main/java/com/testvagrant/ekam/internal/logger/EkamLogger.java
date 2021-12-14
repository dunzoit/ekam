package com.testvagrant.ekam.internal.logger;

import com.testvagrant.ekam.config.models.LogConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class EkamLogger {

  private final String filePath;
  private final LogConfig logConfig;

  public EkamLogger(String filePath, LogConfig logConfig) {
    this.filePath = filePath;
    this.logConfig = logConfig;
  }

  public Logger init() {
    String randomName = UUID.randomUUID().toString().replace("-", "");
    Logger logger = LogManager.getLogger(randomName);
    return logger;
  }

}
