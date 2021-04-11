package com.xiangsy.demo.infrastructure.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.xiangsy.demo.geo.service.GeoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description: pre load static data to cache after initialize
 */
@Service
@Slf4j
public class DemoAppListener implements ApplicationListener<ApplicationEvent> {
    private static boolean started = false;

    @Autowired
    private GeoService geoService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        synchronized (applicationEvent) {
            if (!started) {
                started = true;
                log.info("[Spring started!]");
                initCache();
            }
        }
    }

    private void initCache() {
        geoService.preLoadProvincesAndCities();
    }

}