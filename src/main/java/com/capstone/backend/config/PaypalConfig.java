package com.capstone.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {

  @Value("${application.paypal.mode}")
  private String mode;

  @Value("${application.paypal.client_id}")
  private String clientId;

  @Value("${application.paypal.secret}")
  private String secret;

  @Bean
  public Map<String, String> paypalSdkConfig() {
    Map<String, String> sdkConfig = new HashMap<>();
    sdkConfig.put("mode", mode);
    return sdkConfig;
  }

  @Bean
  public OAuthTokenCredential authTokenCredential() {
    return new OAuthTokenCredential(clientId, secret, paypalSdkConfig());
  }

  @Bean
  public APIContext apiContext() throws PayPalRESTException {
    APIContext apiContext = new APIContext(authTokenCredential().getAccessToken());
    apiContext.setConfigurationMap(paypalSdkConfig());
    return apiContext;
  }

}
