package com.xiangsy.demo.weather.service;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.xiangsy.demo.DemoApp;
import com.xiangsy.demo.common.DemoExceptionKeys;
import com.xiangsy.demo.infrastructure.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-09 22:51
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ControllerAdvice("com.xiangsy.demo")
@ActiveProfiles("dev")
@Slf4j
class WeatherServiceServiceTest {
    @Autowired
    private WeatherService weatherServiceImpl;

    @Before
    void setUp() throws Exception {
        // sleep 3s for application load province and cities
        // Thread.sleep(3000);
    }

    /**
     * valid province&city&county
     */
    @Test
    void testGetTemperature4RealLoation() {
        String province = "江苏";
        String city = "南京";
        String county = "六合";
        Optional<Float> tempOpt = weatherServiceImpl.getTemperature(province, city, county);
        assertTrue(tempOpt.isPresent() && tempOpt.get() > 0);
    }

    /**
     * province length is not correct
     */
    @Test
    void testGetTemperature4ErrorPovinceLength() {
        String province = "";
        String city = "南京";
        String county = "六合";
        try {
            weatherServiceImpl.getTemperature(province, city, county);
        } catch (BizException e) {
            log.error(e.getErrorKeyAndValues());
            assertTrue(StringUtils.equals(DemoExceptionKeys.GEO_PROVINCE_LENGTH_INVALID, e.getErrorKey()));
        }
        
        try {
            province = "江苏江苏江苏江苏江苏江苏";
            weatherServiceImpl.getTemperature(province, city, county);
        } catch (BizException e) {
            log.error(e.getErrorKeyAndValues());
            assertTrue(StringUtils.equals(DemoExceptionKeys.GEO_PROVINCE_LENGTH_INVALID, e.getErrorKey()));
        }
    }

    /**
     * invalid province
     */
    @Test
    void testGetTemperature4ErrorPovince() {
        String province = "苏江";
        String city = "南京";
        String county = "六合";
        try {
            weatherServiceImpl.getTemperature(province, city, county);
        } catch (BizException e) {
            log.error(e.getErrorKeyAndValues());
            assertTrue(StringUtils.equals(DemoExceptionKeys.GEO_PROVINCE_NAME_INVALID, e.getErrorKey()));
        }
    }

    /**
     * invalid city
     */
    @Test
    void testGetTemperature4ErrorCity() {
        String province = "江苏";
        String city = "天津";
        String county = "六合";
        try {
            weatherServiceImpl.getTemperature(province, city, county);
        } catch (BizException e) {
            log.error(e.getErrorKeyAndValues());
            assertTrue(StringUtils.equals(DemoExceptionKeys.GEO_CITY_NAME_INVALID, e.getErrorKey()));
        }
    }

    /**
     * invalid county
     */
    @Test
    void testGetTemperature4ErrorCounty() {
        String province = "江苏";
        String city = "南京";
        String county = "上海";
        try {
            weatherServiceImpl.getTemperature(province, city, county);
        } catch (BizException e) {
            log.error(e.getErrorKeyAndValues());
            assertTrue(StringUtils.equals(DemoExceptionKeys.GEO_COUNTY_NAME_INVALID, e.getErrorKey()));
        }
    }

//    @Test
    void testGetTemperature4CurrentInnovation() throws InterruptedException {
        String province = "江苏";
        String city = "南京";
        String county = "六合";
        // simulate 10 thread to call
        for (int i = 0; i < 10; i++) {
            final long num = i;
            Thread th = new Thread(() -> {
                weatherServiceImpl.getTemperature(province, city, county);
            });
            th.setName("T-" + num);
            th.start();
        }
        Thread.sleep(50000);
        assertTrue(true);
    }

}
