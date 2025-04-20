package com.assessment.sharedpayment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
//@EnableWebSecurity
//@EnableMethodSecurity
@EnableTransactionManagement
@SpringBootApplication
public class SharedPaymentProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedPaymentProcessingApplication.class, args);
        showArtifactVersion();
    }

    private static void showArtifactVersion() {
        log.info("<<-------------------------------------------------------->>>");
        log.info("<<------ Started SHARED PAYMENT PROCESSING SYSTEM -------->>>");
        log.info("<<------------------Version {}----------------------------->>>",
                SharedPaymentProcessingApplication.class.getPackage().getImplementationVersion());
        log.info("<<-------------------------------------------------------->>>");
    }

}