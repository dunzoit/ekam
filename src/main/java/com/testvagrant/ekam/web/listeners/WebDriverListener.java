package com.testvagrant.ekam.web.listeners;

import com.google.inject.Injector;
import com.testvagrant.ekam.commons.Injectors;
import com.testvagrant.ekam.commons.ModulesLibrary;
import com.testvagrant.ekam.internal.Launcher;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class WebDriverListener implements ITestListener {

  @Override
  public void onTestStart(ITestResult result) {
    Reporter.log(String.format("Test %s has started", result.getName().toLowerCase()), true);
    addDivider();
    Injector driverInjector =
        result
            .getTestContext()
            .getSuite()
            .getParentInjector()
            .createChildInjector(new ModulesLibrary().webModules());

    result.setAttribute(Injectors.DRIVER_INJECTOR.getInjector(), driverInjector);
    result.setAttribute(Injectors.WEB_PAGE_INJECTOR.getInjector(), driverInjector);
    driverInjector.getInstance(Launcher.class).launch();
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    quit(result);
  }

  @Override
  public void onTestFailure(ITestResult result) {
    quit(result);
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    quit(result);
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    quit(result);
  }

  @Override
  public void onStart(ITestContext context) {}

  @Override
  public void onFinish(ITestContext context) {}

  public void quit(ITestResult result) {
    Injector driver = (Injector) result.getAttribute(Injectors.DRIVER_INJECTOR.getInjector());
    WebDriver driverInstance = driver.getInstance(WebDriver.class);
    addDivider();
    Reporter.log(String.format("Test %s has ended", result.getName().toLowerCase()), true);
    driverInstance.quit();
  }

  private void addDivider() {
    Reporter.log("==================================================", true);
  }
}
