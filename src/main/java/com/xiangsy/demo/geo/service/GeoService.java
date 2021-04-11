package com.xiangsy.demo.geo.service;

import org.springframework.stereotype.Service;

import com.xiangsy.demo.geo.bean.CityBean;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description: service class for geography
 */

@Service
public interface GeoService {
    /**
     * load all provinces and cities after app started
     */
    public void preLoadProvincesAndCities();


    /**
     * get city from cache
     * 
     * @param provinceName
     * @param cityName
     * @return
     */
    public CityBean getCity(String provinceName, String cityName);

    /**
     * request remote api to get county code by city and province
     * 
     * @param city
     * @param countyName
     * @return
     */
    public String getCountyCode(CityBean city, String countyName);
}
