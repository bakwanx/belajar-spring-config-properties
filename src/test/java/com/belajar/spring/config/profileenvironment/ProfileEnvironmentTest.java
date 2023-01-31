package com.belajar.spring.config.profileenvironment;

import com.belajar.spring.config.profile.ProfileTest;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ProfileEnvironmentTest.TestApplication.class)
@ActiveProfiles({
        "production",
        "local"
})
public class ProfileEnvironmentTest {

    @Autowired
    private TestApplication.SampleProfileClass sampleProfile;

    @Test
    void testProfileEnvironment(){
        System.out.println(sampleProfile.getProfile());
    }

    @Test
    void testActiveProfiles() {
        Assertions.assertArrayEquals(new String[]{"production", "local"}, sampleProfile.getProfile());
    }


    @SpringBootApplication
    public static class TestApplication {

        @Component
        public static class SampleProfileClass implements EnvironmentAware {
            @Setter
            private Environment environment;

            public String[] getProfile() {
                return environment.getActiveProfiles();
            }
        }

    }

}
