package com.redhat.internal.config;

import org.springframework.boot.test.context.SpringBootTest;

import com.redhat.internal.kie.DefaultWebSecurityConfig;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = DefaultWebSecurityConfig.class)
public class CucumberSpringConfiguration {
}
