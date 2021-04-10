package com.xiangsy.demo.weather.service;

import java.util.Optional;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description: service for weather
 */
public interface WeatherService {

    /**
     * fetch and return the temperature of one certain county in China
     * @param province
     * @param city
     * @param county
     * @return
     */
    public Optional<Float> getTemperature(String province, String city, String county);

}
