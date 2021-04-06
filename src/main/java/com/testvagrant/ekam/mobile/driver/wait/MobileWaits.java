package com.testvagrant.ekam.mobile.driver.wait;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.testvagrant.ekam.commons.annotations.WaitDuration;
import com.testvagrant.optimuscloud.entities.MobileDriverDetails;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MobileWaits implements Provider<WebDriverWait> {
  private final WebDriverWait webDriverWait;

  @Inject
  public MobileWaits(MobileDriverDetails mobileDriverDetails, @WaitDuration String waitDuration) {
    webDriverWait =
        new WebDriverWait(mobileDriverDetails.getMobileDriver(), Long.parseLong(waitDuration));
  }

  @Override
  public WebDriverWait get() {
    return webDriverWait;
  }
}