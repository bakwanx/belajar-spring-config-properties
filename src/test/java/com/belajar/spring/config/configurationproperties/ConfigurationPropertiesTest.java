package com.belajar.spring.config.configurationproperties;

import com.belajar.spring.config.converter.StringToDateConverter;
import com.belajar.spring.config.properties.ApplicationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest(classes = ConfigurationPropertiesTest.TestApplication.class)
public class ConfigurationPropertiesTest {

    @Autowired
    Foo foo;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private ConversionService conversionService;

    @Test
    void testConversionService() {
        Assertions.assertTrue(conversionService.canConvert(String.class, Duration.class));
        Assertions.assertTrue(conversionService.canConvert(String.class, Date.class));

        Duration result = conversionService.convert("10s", Duration.class);
        Assertions.assertEquals(Duration.ofSeconds(10), result);
    }

    @Test
    void testConfigurationProperties() {
        Assertions.assertNotNull(foo);
        Assertions.assertEquals("Belajar Spring Boot Using Yaml", properties.getName());
        Assertions.assertEquals(3, properties.getVersion());
        Assertions.assertEquals(true, properties.isProductionMode());
    }

    @Test
    void testDatabaseProperties(){
        Assertions.assertEquals("farhan", properties.getDatabase().getUsername());
        Assertions.assertEquals("rahasia", properties.getDatabase().getPassword());
        Assertions.assertEquals("belajar", properties.getDatabase().getDatabase());
        Assertions.assertEquals("jdbc:contoh", properties.getDatabase().getUrl());
    }

    @Test
    void testEmbeddedCollection() {
        Assertions.assertEquals("default",properties.getDefaultRoles().get(0).getId());
        Assertions.assertEquals("Default Role",properties.getDefaultRoles().get(0).getName());
        Assertions.assertEquals("guest",properties.getDefaultRoles().get(1).getId());
        Assertions.assertEquals("Guest Role",properties.getDefaultRoles().get(1).getName());

        Assertions.assertEquals("admin", properties.getRoles().get("admin").getId());
        Assertions.assertEquals("Admin Role", properties.getRoles().get("admin").getName());
        Assertions.assertEquals("finance", properties.getRoles().get("finance").getId());
        Assertions.assertEquals("Finance Role", properties.getRoles().get("finance").getName());
    }

    @Test
    void testCollection() {
        Assertions.assertEquals(Arrays.asList("products","customers","categories"), properties.getDatabase().getWhiteListTables());
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("products"));
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("customers"));
        Assertions.assertEquals(100, properties.getDatabase().getMaxTablesSize().get("categories"));
    }

    @Test
    void testDuration() {
        Assertions.assertEquals(Duration.ofSeconds(10), properties.getDefaultTimeout());
    }

    @Test
    void testCustomConverter() {
        Date expiredDate = properties.getExpiredDate();
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Assertions.assertEquals("2020-10-10", dateFormat.format(expiredDate));
    }

    @SpringBootApplication
    @EnableConfigurationProperties({
            ApplicationProperties.class
    })
    @Import({
            StringToDateConverter.class
    })
    public static class TestApplication {

        @Bean
        public ConversionService conversionService(StringToDateConverter stringToDateConverter){
            ApplicationConversionService conversionService = new ApplicationConversionService();
            conversionService.addConverter(stringToDateConverter);
            return conversionService;
        }

        @Bean
        public Foo foo(){
            return new Foo();
        }
    }

    public static class Foo {

    }

}
