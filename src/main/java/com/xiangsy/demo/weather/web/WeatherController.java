package com.xiangsy.demo.weather.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xiangsy.demo.common.DemoConstants;
import com.xiangsy.demo.common.DemoExceptionKeys;
import com.xiangsy.demo.geo.bean.GeoQueryBean;
import com.xiangsy.demo.infrastructure.exception.BizException;
import com.xiangsy.demo.weather.service.WeatherService;

import lombok.AllArgsConstructor;

/**
 * @author: xiangsy
 * @version: 2021-04-11 11:59
 * @description:
 */
@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class WeatherController {
    private final WeatherService weatherServiceImpl;

    @PostMapping(value = "/county", consumes = DemoConstants.MEDIATYPE)
    @ResponseStatus(HttpStatus.OK)
    public float getTemperature(@RequestBody GeoQueryBean geoQueryBean) {
        Optional<Float> tempOpt = weatherServiceImpl.getTemperature(geoQueryBean.getProvince(), geoQueryBean.getCity(),
                geoQueryBean.getCounty());
        if (tempOpt.isPresent()) {
            return tempOpt.get();
        } else {
            throw new BizException(DemoExceptionKeys.SYS_UNEXPECTED_ERROR);
        }
    }
}
