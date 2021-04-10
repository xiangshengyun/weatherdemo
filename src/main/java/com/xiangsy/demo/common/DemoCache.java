package com.xiangsy.demo.common;

import java.util.HashMap;
import java.util.Map;

import com.xiangsy.demo.geo.bean.ProvinceBean;

/**
 * @author: xiangsy
 * @version: 2021-04-09 21:17
 * @description: application-level cache
 */
public class DemoCache {

    public static Map<String, ProvinceBean> provinceNameBeanMap = new HashMap<String, ProvinceBean>();
}
