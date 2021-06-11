package com.redhat.internal.config;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
/*@CucumberOptions( //
    features = "classpath:cs.feature", //
    format = {"pretty", "html:target/Destination"} //
)
@CucumberOptions( //
features = "classpath:cs.feature",
plugin = { "pretty", "html:target/cucumber-reports" }
)*/

@CucumberOptions( 
		features = "classpath:cs.feature",
		plugin = { "pretty", "html:target/cucumber-reports" },
		extraGlue = {"com.redhat.internal.config", "com.redhat.internal.cases"}
		)
public class RunnerTest {

}