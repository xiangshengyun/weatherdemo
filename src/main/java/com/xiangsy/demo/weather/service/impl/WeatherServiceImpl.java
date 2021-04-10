package com.xiangsy.demo.weather.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.xiangsy.demo.common.DemoConstants;
import com.xiangsy.demo.geo.bean.CityBean;
import com.xiangsy.demo.geo.service.GeoService;
import com.xiangsy.demo.weather.bean.WeatherInfo;
import com.xiangsy.demo.weather.bean.WeatherResult;
import com.xiangsy.demo.weather.service.WeatherService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 */
@Service("WeatherServiceImpl")
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private GeoService geoService;
    @Value("${url.weather.county}")
    private String weatherUrl;

    @Override
    public Optional<Float> getTemperature(String province, String city, String county) {
        log.debug(Thread.currentThread().getName() + ", getTemperature() execution start.");
        // get city from cache
        CityBean cityBean = geoService.getCity(province, city);
        // get county from remote
        String countyCode = geoService.getCountyCode(cityBean, county);

        return getTemperature(countyCode);
    }

    private Optional<Float> getTemperature(String countyCode) {
        try {
            ResponseEntity<String> responseEntity = retryTemplate
                    .execute(new RetryCallback<ResponseEntity<String>, RuntimeException>() {
                        @Override
                        public ResponseEntity<String> doWithRetry(RetryContext retryContext) throws RuntimeException {
                            String weatherTmpUrl = weatherUrl.replace(DemoConstants.PH_COUNTY_CODE, countyCode);
                            ResponseEntity<String> results = restTemplate.exchange(weatherTmpUrl, HttpMethod.GET, null,
                                    String.class);
                            return results;
                        }
                    }, new RecoveryCallback<ResponseEntity<String>>() {
                        @Override
                        public ResponseEntity<String> recover(RetryContext retryContext) throws Exception {
                            log.error("retry failed, retryContext={}", retryContext);
                            return null;
                        }
                    });
            log.info("getTemperature countyCode {},responseEntity={}", countyCode, responseEntity);

            if (responseEntity != null) {
                HttpStatus httpStatus = responseEntity.getStatusCode();
                if (httpStatus != null && httpStatus == HttpStatus.OK) {
                    String json = responseEntity.getBody();
                    WeatherResult weatherResult = JSON.parseObject(json, WeatherResult.class);
                    if (null != weatherResult) {
                        WeatherInfo weatherInfo = weatherResult.getWeatherInfo();
                        log.info("The temperature of {} is {} degrees.", weatherInfo.getCity(), weatherInfo.getTemp());
                        return Optional.ofNullable(weatherInfo.getTemp());
                    }
                }
            } else {
                log.error("getTemperature - empty responseEntity. ");
            }
        } catch (Exception e) {
            log.error("getTemperature - execute error: ", e);
        }
        log.debug(Thread.currentThread().getName() + ", getTemperature() execution end.");
        return Optional.ofNullable(null);
    }
}
