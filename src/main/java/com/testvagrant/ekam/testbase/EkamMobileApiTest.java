package com.testvagrant.ekam.testbase;

import com.testvagrant.ekam.api.modules.GrpcModule;
import com.testvagrant.ekam.api.modules.HttpClientModule;
import com.testvagrant.ekam.commons.modules.LocaleModule;
import com.testvagrant.ekam.commons.modules.PropertyModule;
import com.testvagrant.ekam.commons.modules.SwitchViewModule;
import com.testvagrant.ekam.mobile.modules.MobileModule;
import org.testng.annotations.Guice;

/**
 * TestBase for mobile tests with API access
 */
@Guice(modules = {PropertyModule.class,
        LocaleModule.class,
        SwitchViewModule.class,
        HttpClientModule.class,
        GrpcModule.class,
        MobileModule.class})
public class EkamMobileApiTest {
}
