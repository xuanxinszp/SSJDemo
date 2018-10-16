package com.xuanxinszp.ssjdemo.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 数据转换
 *
 * @author 6213
 * @date 2018/3/29
 */
public class ConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);

    /**
     * 允许转换的类型
     */
    public static final List<Class> convertClassTypes = Lists.newArrayList(
            String.class,
            Integer.class,
            Double.class,
            Float.class,
            Boolean.class,
            Short.class,
            Long.class,
            Byte.class,
            Character.class,
            java.math.BigDecimal.class,
            Date.class,
            java.sql.Timestamp.class,
            int.class, long.class, char.class, byte.class, short.class, float.class, double.class, boolean.class);

    /**
     * 对象转换为map
     * <p>
     *     过滤掉值为null或空的属性
     * </p>
     * @param obj
     * @return
     */
    public static Map<String, String> convertBeanToMap(Object obj) {
        if(Objects.isNull(obj)) {
            return null;
        }
        Map<String, String> map = Maps.newHashMap();
        Class type = obj.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                try {
                    logger.info("属性：{}，值：{}", propertyName, descriptor.getPropertyType());
                    if (!propertyName.equals("class") && convertClassTypes.contains(descriptor.getPropertyType())) {
                        Method readMethod = descriptor.getReadMethod();
                        Object result = readMethod.invoke(obj, new Object[0]);
                        if (result != null && StringUtils.isNotBlank(result.toString())) {
                            map.put(propertyName, result + "");
                        }
                    }
                } catch (Exception e) {
                    logger.error("======================>>>>convertBeanToMap异常，参数名称：" + propertyName,e);
                }
            }
        } catch (Exception e) {
            logger.error("======================>>>>convertBeanToMap异常",e);
            return map;
        }
        return map;
    }

    /**
     * 对象转换为map
     * <p>
     *     过滤掉值为null或空的属性
     * </p>
     * @param obj
     * @return
     */
    public static Map<String, Object> convertBeanToMapObject(Object obj) {
        if(Objects.isNull(obj)) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        Class type = obj.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(obj, new Object[0]);
                    map.put(propertyName, result + "");
                }
            }
        } catch (Exception e) {
            logger.error("======================>>>>convertBeanToMapObject异常",e);
            return map;
        }
        return map;
    }


    /**
     * map转换为对象
     * @param map
     * @param cls
     * @return
     */
    public static <T> T convertMapToBean(Map<String,String> map,Class<T> cls) {
        T result = null;
        try {
            result = (T) cls.newInstance();
            if (map == null || map.size() == 0) {
                return result;
            }
            BeanInfo beanInfo = Introspector.getBeanInfo(result.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    setter.invoke(result, map.get(property.getName()));
                }
            }
        } catch (InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            logger.error("======================>>>>convertMapToBean异常",e);
        }
        return result;
    }

    /**
     * 移除map中空key或者value空值
     * @param map
     */
    public static void removeNullEntry(Map map){
        removeNullKey(map);
        removeNullValue(map);
    }

    /**
     * 移除map的空key
     * @param map
     * @return
     */
    public static void removeNullKey(Map map){
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Object obj = (Object) iterator.next();
            remove(obj, iterator);
        }
    }

    /**
     * 移除map中的value空值
     * @param map
     * @return
     */
    public static void removeNullValue(Map map){
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Object obj = (Object) iterator.next();
            Object value =(Object)map.get(obj);
            remove(value, iterator);
        }
    }

    /**
     * Iterator 是工作在一个独立的线程中，并且拥有一个 mutex 锁。
     * Iterator 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变，
     * 所以当索引指针往后移动的时候就找不到要迭代的对象，所以按照 fail-fast 原则 Iterator 会马上抛出 java.util.ConcurrentModificationException 异常。
     * 所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。
     * 但你可以使用 Iterator 本身的方法 remove() 来删除对象， Iterator.remove() 方法会在删除当前迭代对象的同时维护索引的一致性。
     * @param obj
     * @param iterator
     */
    private static void remove(Object obj,Iterator iterator){
        if(obj instanceof String){
            String str = (String)obj;
            if(StringUtil.isBlank(str)){
                iterator.remove();
            }
        }else if(obj instanceof Collection){
            Collection col = (Collection)obj;
            if(col==null||col.isEmpty()){
                iterator.remove();
            }

        }else if(obj instanceof Map){
            Map temp = (Map)obj;
            if(temp==null||temp.isEmpty()){
                iterator.remove();
            }

        }else if(obj instanceof Object[]){
            Object[] array =(Object[])obj;
            if(array.length<=0){
                iterator.remove();
            }
        }else{
            if(obj==null){
                iterator.remove();
            }
        }
    }


}
