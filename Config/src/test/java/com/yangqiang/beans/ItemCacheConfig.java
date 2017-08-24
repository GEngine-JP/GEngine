/**
 * 创建日期:  2017年08月19日 14:13
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package com.yangqiang.beans;


import info.xiaomo.gameCore.config.IConfigCache;
import info.xiaomo.gameCore.config.annotation.Cache;
import info.xiaomo.gameCore.config.annotation.Config;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 *
 * @author YangQiang
 */
@Cache
public class ItemCacheConfig implements IConfigCache {
    public static void main(String[] args) {
        Annotation annotation = ItemCacheConfig.class.getAnnotation(Cache.class);
        System.out.println(annotation);
        System.out.println(ItemCacheConfig.class.isAnnotationPresent(Config.class));
        System.out.println(annotation.annotationType());
        System.out.println(annotation.annotationType().getName());
        System.out.println(Arrays.toString(annotation.annotationType().getAnnotations()));
        System.out.println(Arrays.toString(Cache.class.getAnnotations()));
    }
}
