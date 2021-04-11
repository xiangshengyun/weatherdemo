package com.xiangsy.demo.geo.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.xiangsy.demo.common.DemoConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: xiangsy
 * @version: 2021-04-09 20:10
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoQueryBean {

    @NotBlank(message = "province name cannot be empty")
    @Size(max = DemoConstants.PROVINCE_MAX_LENGTH, message = "the lenght of province name must be less than 10")
    private String province;

    @NotBlank(message = "city name cannot be empty")
    @Size(max = DemoConstants.CITY_MAX_LENGTH, message = "the lenght of province name must be less than 20")
    private String city;

    @NotBlank(message = "county name cannot be empty")
    @Size(max = DemoConstants.COUNTY_MAX_LENGTH, message = "the lenght of province name must be less than 20")
    private String county;
}
