package com.xiangsy.demo.geo.service;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.xiangsy.demo.DemoApp;
import com.xiangsy.demo.common.DemoCache;

/**
 * @author: xiangsy
 * @version: 2021-04-09 20:21
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ControllerAdvice("com.xiangsy.demo")
@ActiveProfiles("dev")
class GeoServiceTest {
    @Autowired
    private GeoService geoService;

    @BeforeEach
    void setUp() throws Exception {
        // sleep 2s for application load province and cities
        Thread.sleep(2000);
    }

    @Test
    void testGetProvinces() {
        geoService.loadProvinces();
        assertTrue(null != DemoCache.provinceNameBeanMap && null != DemoCache.provinceNameBeanMap.keySet()
                && DemoCache.provinceNameBeanMap.keySet().size() == 34);
    }

}
