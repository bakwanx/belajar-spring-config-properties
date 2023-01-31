package com.belajar.spring.config.profileproperties;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ProfilePropertiesTest.TestApplication.class)
@ActiveProfiles({"production", "test"})
public class ProfilePropertiesTest {

    @Autowired
    private TestApplication.ProfileProperties properties;

    //cara tanpa inject
    private ConfigurableApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
    }

    @Test
    void nameProfileProperties() {
        TestApplication.ProfileProperties properties1 = applicationContext.getBean(TestApplication.ProfileProperties.class);
        Assertions.assertEquals("Default", properties.getDefaultFile());
        Assertions.assertEquals("Production", properties.getProductionFile());
        Assertions.assertEquals("Test", properties.getTestFile());
//        Assertions.assertEquals("Default",properties1.getDefaultFile());
    }

    @Configuration
    public static class TestApplication {

        @Getter
        @Component
        public static class ProfileProperties {

            @Value("${profile.default}")
            private String defaultFile;

            @Value("${profile.production}")
            private String productionFile;

            @Value("${profile.test}")
            private String testFile;
        }

    }

}
