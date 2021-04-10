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
    public void preLoadCountiesAndCities();

    public void loadProvinces();

    public CityBean getCity(String provinceName, String cityName);

    public String getCountyCode(CityBean city, String countyName);
}
