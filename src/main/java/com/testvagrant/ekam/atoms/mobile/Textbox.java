package com.testvagrant.ekam.atoms.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class Textbox extends EkamMobileElement {

  public Textbox(AppiumDriver<MobileElement> driver) {
    super(driver);
  }

  public void setText(CharSequence value) {
    setText(value, false);
  }

  public void setText(CharSequence value, boolean clear) {
    if (clear) clear();
    getElement().sendKeys(value.toString());
  }

  public void clear() {
    getElement().clear();
  }
}
