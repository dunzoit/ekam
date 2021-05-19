package com.testvagrant.ekam.web.listeners;

import com.testvagrant.ekam.commons.injectors.InjectorCreator;
import com.testvagrant.ekam.commons.injectors.Injectors;
import com.testvagrant.ekam.commons.listeners.BuildListener;
import com.testvagrant.ekam.commons.testContext.EkamTestContextConverter;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class WebDriverListener extends BuildListener implements ITestListener {

  public WebDriverListener() {
    super("web");
  }

  @Override
  public void onTestStart(ITestResult result) {
    Reporter.log(String.format("Test %s has started", result.getName().toLowerCase()));
    addDivider();
    new InjectorCreator(EkamTestContextConverter.convert(result)).createWebInjector();
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    quit(result, "passed");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    quit(result, "failed");
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    quit(result, "skipped");
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    quit(result, "passed");
  }

  @Override
  public void onStart(ITestContext context) {}

  @Override
  public void onFinish(ITestContext context) {}

  public void quit(ITestResult result, String status) {
    updateBuild(result, status, Injectors.WEB_PAGE_INJECTOR);
    addDivider();
    WebDriverManager.dispose();
    Reporter.log(String.format("Test %s has ended", result.getName().toLowerCase()), true);
  }
}
