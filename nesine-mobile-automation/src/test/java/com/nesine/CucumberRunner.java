package com.nesine;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.nesine.StepImpl",
        plugin = {"pretty", "html:target/cucumber-reports"},
        tags = "@login"
)
public class CucumberRunner {
}