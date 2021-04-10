package com.xiangsy.demo.geo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.xiangsy.demo.common.DemoExceptionKeys;
import com.xiangsy.demo.common.DemoCache;
import com.xiangsy.demo.common.DemoConstants;
import com.xiangsy.demo.geo.bean.CityBean;
import com.xiangsy.demo.geo.bean.ProvinceBean;
import com.xiangsy.demo.geo.service.GeoService;
import com.xiangsy.demo.infrastructure.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description: service class for geography
 */
@Service
@Slf4j
public class GeoServiceImpl implements GeoService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.geo.china.province}")
    private String geoProvinceUrl;
    @Value("${url.geo.china.province.city}")
    private String geoCityUrl;
    @Value("${url.geo.china.province.city.county}")
    private String geoCountyUrl;

    @Override
    @Async
    public void preLoadCountiesAndCities() {
        loadProvinces();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadProvinces() {
        long startTime = System.currentTimeMillis();
        ResponseEntity<String> responsEntity = restTemplate.exchange(geoProvinceUrl, HttpMethod.GET, null,
                String.class);
        if (null != responsEntity) {
            String json = responsEntity.getBody();
            Map<String, String> provinceMap = JSON.parseObject(json, Map.class);
            provinceMap.keySet().stream().forEach(key -> {
                ProvinceBean provinceBean = new ProvinceBean();
                provinceBean.setCode((String) key);
                provinceBean.setName((String) provinceMap.get(key));
                DemoCache.provinceNameBeanMap.put(provinceBean.getName(), provinceBean);
                provinceBean.setCityList(getCitiesByProvince(provinceBean));
            });
        } else {
            throw new BizException(DemoExceptionKeys.GEO_PROIVNCES_NOT_FOUND);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("Loading Provinces&Cities Costs:{} ms", totalTime);
    }

    @SuppressWarnings("unchecked")
    private List<CityBean> getCitiesByProvince(ProvinceBean province) {
        List<CityBean> cityList = new ArrayList<CityBean>();
        String geoCityUrlTmp = geoCityUrl.replace(DemoConstants.PH_PROV_CODE, province.getCode());
        ResponseEntity<String> responsEntity = restTemplate.exchange(geoCityUrlTmp, HttpMethod.GET, null, String.class);
        if (null != responsEntity) {
            String json = responsEntity.getBody();
            Map<String, String> cityMap = JSON.parseObject(json, Map.class);
            cityMap.keySet().stream().forEach(key -> {
                CityBean cityBean = new CityBean();
                cityBean.setCode((String) key);
                cityBean.setName((String) cityMap.get(key));
                cityBean.setProvinceCode(province.getCode());
                cityBean.setProvinceName(province.getName());
                cityList.add(cityBean);
                // cityBean.setCountyList(getCounties(cityBean));
            });
        } else {
            throw new BizException(DemoExceptionKeys.GEO_CITIES_NOT_FOUND, province.getName());
        }
        return cityList;
    }

    @Override
    public CityBean getCity(String provinceName, String cityName) {
        ProvinceBean province = DemoCache.provinceNameBeanMap.get(provinceName);
        if (null == province) {
            throw new BizException(DemoExceptionKeys.GEO_PROVINCE_NAME_INVALID, provinceName);
        }
        if (!CollectionUtils.isEmpty(province.getCityList())) {
            Optional<CityBean> cityBeanOpt = province.getCityList().stream()
                    .filter(x -> StringUtils.equals(cityName, x.getName())).findFirst();
            if (cityBeanOpt.isPresent()) {
                return cityBeanOpt.get();
            } else {
                throw new BizException(DemoExceptionKeys.GEO_CITY_NAME_INVALID, cityName);
            }
        }
        throw new BizException(DemoExceptionKeys.GEO_CITY_NAME_INVALID, cityName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getCountyCode(CityBean city, String countyName) {
        String geoCountyUrlTmp = geoCountyUrl.replace(DemoConstants.PH_CITY_CODE, city.getProvinceCode() + city.getCode());
        ResponseEntity<String> responsEntity = restTemplate.exchange(geoCountyUrlTmp, HttpMethod.GET, null,
                String.class);
        if (null != responsEntity) {
            String json = responsEntity.getBody();
            Map<String, String> countyMap = JSON.parseObject(json, Map.class);
            String keyMatch = countyMap.keySet().stream()
                    .filter(key -> StringUtils.equals(countyName, (String) countyMap.get(key))).findFirst()
                    .orElse(null);
            if (null == keyMatch) {
                throw new BizException(DemoExceptionKeys.GEO_COUNTY_NAME_INVALID, countyName);
            }
            return city.getProvinceCode() + city.getCode() + keyMatch;
        } else {
            throw new BizException(DemoExceptionKeys.GEO_COUNTY_NAME_INVALID, countyName);
        }
    }

}
