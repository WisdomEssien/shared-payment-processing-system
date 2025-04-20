package com.assessment.sharedpayment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "app-props")
public class AppProperty {

    private Double dynamicRate;
}
