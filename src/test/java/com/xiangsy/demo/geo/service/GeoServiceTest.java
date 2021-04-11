package com.xiangsy.demo.geo.service;

import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
    @Before
    void setUp() throws Exception {
        // sleep 3s for application load province and cities
        // Thread.sleep(3000);
    }

    @Test
    void testGetProvinces() {
        assertTrue(null != DemoCache.provinceNameBeanMap
                || CollectionUtils.isNotEmpty(DemoCache.provinceNameBeanMap.keySet())
                        && DemoCache.provinceNameBeanMap.keySet().size() == 34);
    }

}
