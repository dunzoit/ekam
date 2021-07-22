package com.testvagrant.ekam.commons.parsers.testfeed;

import com.testvagrant.ekam.commons.generators.PortGenerator;
import com.testvagrant.ekam.commons.models.mobile.MobileTestFeed;
import com.testvagrant.ekam.commons.random.FindAny;
import com.testvagrant.ekam.commons.random.RepetitiveStringGenerator;
import com.testvagrant.ekam.config.models.MobileConfig;
import com.testvagrant.ekam.devicemanager.models.DeviceFilter;
import com.testvagrant.ekam.devicemanager.models.DeviceFilters;
import com.testvagrant.ekam.mobile.AppFinder;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.apache.commons.exec.OS;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.*;

import static com.testvagrant.ekam.commons.constants.MobilePlatforms.ANDROID;
import static com.testvagrant.ekam.commons.constants.MobilePlatforms.IOS;
import static com.testvagrant.ekam.config.models.ConfigKeys.Env.MOBILE_ENV;

public class MobileConfigParser extends TestConfigParser {
  private final MobileTestFeed testFeed;
  private final MobileConfig mobileConfig;
  private String platform;
  private DesiredCapabilities desiredCapabilities;
  private DeviceFilters deviceFilters;
  private Map<ServerArgument, String> serverArguments;

  public MobileConfigParser(MobileConfig mobileConfig) {
    this.mobileConfig = mobileConfig;
    setPlatform();
    this.testFeed = getTestFeed(mobileConfig.getFeed());
  }

  public String getPlatform() {
    if (platform == null) setPlatform();
    return platform;
  }

  public MobileConfig getMobileConfig() {
    return mobileConfig;
  }

  public Map<ServerArgument, String> getServerArguments() {
    if (serverArguments == null) setServerArguments();
    return serverArguments;
  }

  public DesiredCapabilities getDesiredCapabilities() {
    if (desiredCapabilities == null) setDesiredCapabilities();
    return desiredCapabilities;
  }

  public DeviceFilters getDeviceFilters() {
    if (deviceFilters == null) setDeviceFilters();
    return deviceFilters;
  }

  private void setDeviceFilters() {
    if (mobileConfig.isDeviceFiltersProvided()) {
      deviceFilters = loadFeed(mobileConfig.getDeviceFilters(), MOBILE_ENV, DeviceFilters.class);
      return;
    }

    Map<String, Object> desiredCapabilities = getDesiredCapabilities().asMap();
    String udid = (String) desiredCapabilities.getOrDefault(MobileCapabilityType.UDID, "");
    String model = (String) desiredCapabilities.getOrDefault(MobileCapabilityType.DEVICE_NAME, "");
    String platformVersion =
        (String) desiredCapabilities.getOrDefault(MobileCapabilityType.PLATFORM_VERSION, "");

    DeviceFilter udidFilter =
        new DeviceFilter().toBuilder().include(Collections.singletonList(udid)).build();

    DeviceFilter modelFilter =
        new DeviceFilter().toBuilder().include(Collections.singletonList(model)).build();

    DeviceFilter platformVersionFilter =
        new DeviceFilter().toBuilder().include(Collections.singletonList(platformVersion)).build();

    deviceFilters =
        new DeviceFilters()
            .toBuilder()
                .model(modelFilter)
                .udid(udidFilter)
                .platformVersion(platformVersionFilter)
                .build();
  }

  private void setServerArguments() {
    serverArguments =
        mobileConfig.isServerArgsProvided()
            ? new ServerArgumentParser(mobileConfig.getServerArgs()).getServerArgumentsMap()
            : new ServerArgumentParser(testFeed.getServerArguments()).getServerArgumentsMap();
  }

  private void setDesiredCapabilities() {
    List<Map<String, Object>> capabilitiesList = testFeed.getDesiredCapabilities();
    RuntimeException exception = new RuntimeException("Cannot find desired capabilities");

    if (capabilitiesList.isEmpty() || mobileConfig.isAny() || capabilitiesList.size() == 1) {
      desiredCapabilities =
          new DesiredCapabilities(capabilitiesList.stream().findAny().orElseThrow(() -> exception));
      return;
    }

    Map<String, Object> capabilities =
        capabilitiesList.stream()
            .filter(
                desiredCaps ->
                    desiredCaps
                        .get(MobileCapabilityType.PLATFORM_NAME)
                        .toString()
                        .equalsIgnoreCase(platform))
            .findFirst()
            .orElseThrow(() -> exception);

    Map<String, Object> updatedCapabilities = updateMandatoryCapabilities(capabilities);
    desiredCapabilities = new DesiredCapabilities(updatedCapabilities);
  }

  private Map<String, Object> updateMandatoryCapabilities(Map<String, Object> capabilities) {
    capabilities.put(CapabilityType.PLATFORM_NAME, platform);

    String app = (String) capabilities.getOrDefault(MobileCapabilityType.APP, "");
    if (!app.isEmpty()) {
      String appPath =
          Objects.isNull(mobileConfig.getFeed()) || mobileConfig.getFeed().isEmpty()
              ? AppFinder.getDefaultApp(platform)
              : app.contains(":") ? app : AppFinder.findApp(app);
      capabilities.put(MobileCapabilityType.APP, appPath);
    }

    if (!mobileConfig.isRemote() && platform.equalsIgnoreCase(ANDROID)) {
      capabilities.put(
          AndroidMobileCapabilityType.SYSTEM_PORT,
          PortGenerator.randomOpenPortOnAllLocalInterfaces());
    }

    return capabilities;
  }

  private void setPlatform() {
    if (mobileConfig.isAny()) {
      List<String> randomPlatforms = generateRandomPlatforms();
      platform = FindAny.inList(randomPlatforms);
      mobileConfig.setTarget(platform.trim());
    }

    platform = mobileConfig.getTarget().trim();
  }

  private MobileTestFeed getTestFeed(String testFeed) {
    if (testFeed == null || testFeed.isEmpty()) {
      return MobileTestFeed.builder().desiredCapabilities(generateDefaultCapabilities()).build();
    }

    return loadFeed(testFeed, System.getProperty(MOBILE_ENV), MobileTestFeed.class);
  }

  private List<String> generateRandomPlatforms() {
    RepetitiveStringGenerator repetitiveStringGenerator = new RepetitiveStringGenerator();
    return OS.isFamilyMac()
        ? repetitiveStringGenerator.generate(ANDROID, IOS)
        : repetitiveStringGenerator.generate(ANDROID);
  }

  private List<Map<String, Object>> generateDefaultCapabilities() {
    Map<String, Object> capabilities = new HashMap<>();
    capabilities.put(MobileCapabilityType.PLATFORM_NAME, platform);

    List<Map<String, Object>> capabilitiesList = new ArrayList<>();
    capabilitiesList.add(capabilities);
    return capabilitiesList;
  }
}
