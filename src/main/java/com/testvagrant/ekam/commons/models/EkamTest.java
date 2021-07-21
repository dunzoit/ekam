package com.testvagrant.ekam.commons.models;

import com.google.common.base.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/***
 * Model to store test context: Feature and Scenario/TestClass and TestMethod
 */
@Getter
@Setter
@Builder
public class EkamTest {
  private String feature;
  private String scenario;

  @Override
  public int hashCode() {
    return Objects.hashCode(this.feature, this.scenario);
  }
}
