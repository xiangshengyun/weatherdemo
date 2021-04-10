package com.xiangsy.demo.weather.service.impl;

import java.util.Optional;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangsy.demo.weather.service.WeatherService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-10 15:24
 * @description: proxy class to prevent current Limit more than 100/s
 */
@Service("WeatherServiceProxy")
@Slf4j
public class WeatherServiceProxy implements WeatherService {
    @Autowired
    private WeatherServiceImpl weatherServiceImpl;
    private static Semaphore semaphore = new Semaphore(100);

    @Override
    public Optional<Float> getTemperature(String province, String city, String county) {
        if (semaphore.availablePermits() == 0) {
            log.info("The number of inonvation exceeds the maximum, please wait patiently");
        }

        try {
            semaphore.acquire();
            return weatherServiceImpl.getTemperature(province, city, county);
        } catch (Exception e) {
            log.error("weatherServiceImpl.getTemperature() access error: ", e);
        } finally {
            semaphore.release();
        }
        return Optional.ofNullable(null);
    }

}
