package com.testvagrant.ekam.testBases.cucumber.web;

import com.google.inject.Injector;
import com.testvagrant.ekam.internal.executiontimeline.models.EkamTest;
import com.testvagrant.ekam.internal.injectors.EkamWebInjector;
import com.testvagrant.ekam.testBases.cucumber.ScenarioDefinition;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import static com.testvagrant.ekam.internal.injectors.InjectorsCacheProvider.injectorsCache;

public class WebScenarioDefinition extends ScenarioDefinition {

  public void setup(Scenario scenario) {
    EkamTest ekamTest = buildEkamTest(scenario);
    new EkamWebInjector(ekamTest).create();
  }

  public void tearDown() {
    Injector injector = injectorsCache().getInjector();
    WebDriver webDriver = injector.getInstance(WebDriver.class);
    if (webDriver != null) webDriver.quit();
  }
}
