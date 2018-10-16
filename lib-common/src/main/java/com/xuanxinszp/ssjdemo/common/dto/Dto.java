package com.xuanxinszp.ssjdemo.common.dto;

import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.common.util.TimeUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Benson on 2018/3/28.
 */
public abstract class Dto implements Serializable{

    /** 默认第几页 */
    protected static final Integer PAGE_NUMBER = 1;

    /** 默认每页显示条数 */
    protected static final Integer PAGE_SIZE = 10;

    protected String blankWhenNull(Object o){
        return o == null ? null : o.toString();
    }

    protected Integer blankWhenNullOfInteger(Object o){
        return o != null && StringUtil.isNotNil(o.toString()) ? Integer.parseInt(o.toString()) : null;
    }


    protected Date blankWhenNullOfDate(Object o){
        return o == null ? null : (StringUtil.isBlank(o.toString()) ? null : (Date)o);
    }

    protected Date blankWhenNullOfDateTime(Object o){
        if (null == o) return null;
        return TimeUtil.parseDatetime(o.toString());
    }

    protected BigDecimal blankWhenNullOfBigDecimal(Object o) {
        return o == null ? null : new BigDecimal(o.toString());
    }

    protected Boolean blankWhenNullOfBoolean(Object o) {
        return null == o ? Boolean.FALSE : (Boolean) o;
    }

    protected Boolean blankWhenNullOfIntegerToBoolean(Object o) {
        boolean result = Boolean.FALSE;
        if (null != o && o instanceof Byte) {
            byte v = (byte) o;
            result = (v == 0 ? Boolean.FALSE : Boolean.TRUE);
        }
        return result;
    }

    protected Long blankWhenNullOfLong(Object o){
        return o != null && StringUtil.isNotNil(o.toString()) ? Long.parseLong(o.toString()) : null;
    }
}
