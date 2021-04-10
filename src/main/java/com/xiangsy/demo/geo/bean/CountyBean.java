package com.xiangsy.demo.geo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 
*  @author: xiangsy 
*  @version: 2021-04-09 20:10
*  @description: 
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CountyBean extends BaseVO{
    private static final long serialVersionUID = 8519645491754179230L;
    
    private String code;
    private String name;
    private String cityCode;
    private String cityName;
    private String provinceCode;
    private String provinceName;
}
