package com.xuanxinszp.ssjdemo.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuanxinszp.ssjdemo.common.util.BeanUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Benson on 2018/3/28.
 */
public class JpaStringProperty extends SimpleProperty<String> implements UserType {

    protected int[] TYPES = {Types.INTEGER};

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class returnedClass() {
        return getClass();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null && y == null) {
            return true;
        }
        if (x == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        Assert.isTrue(x != null, "");
        return x.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        String code = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names, session, owner);
        if (code == null) {
            return null;
        }
        return allProperty.get(getMapKey(code));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
        } else {
            Serializable code = (Serializable) ((JpaStringProperty) value).getCode();
            StandardBasicTypes.STRING.nullSafeSet(st, code, index, session);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return BeanUtil.deepCopy(value);
    }

    @Override
    @JsonIgnore
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
