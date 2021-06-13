package com.redhat.internal.cases;

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
		extraGlue = {"com.redhat.internal"}
		)
public class RunnerTest {

}