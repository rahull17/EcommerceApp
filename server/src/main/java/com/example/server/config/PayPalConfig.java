package com.example.server.config;

import com.paypal.base.rest.APIContext;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    Dotenv dotenv = Dotenv.load();

    @Bean
    public APIContext apiContext() {
        String clientId = dotenv.get("PAYPAL_CLIENT_ID");
        String clientSecret = dotenv.get("PAYPAL_CLIENT_SECRET");
        String mode = dotenv.get("PAYPAL_MODE"); // "sandbox" or "live"
        return new APIContext(clientId, clientSecret, mode);
    }
}
