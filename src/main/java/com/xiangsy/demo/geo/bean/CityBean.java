package com.xiangsy.demo.geo.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author: xiangsy
 * @version: 2021-04-09 20:10
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CityBean extends BaseVO {
    private static final long serialVersionUID = -2813764866690859852L;

    private String code;
    private String name;
    private String provinceCode;
    private String provinceName;
    private List<CountyBean> countyList = new ArrayList<CountyBean>();
}
