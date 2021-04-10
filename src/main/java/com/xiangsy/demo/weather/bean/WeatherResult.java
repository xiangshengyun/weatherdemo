package com.xiangsy.demo.weather.bean;

import com.xiangsy.demo.geo.bean.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 
*  @author: xiangsy 
*  @version: 2021-04-09 22:55
*  @description: 
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class WeatherResult extends BaseVO{
    private static final long serialVersionUID = -7921800749921441571L;
    private WeatherInfo weatherInfo;
}
