package com.xuanxinszp.ssjdemo.sys.dto;


import com.xuanxinszp.ssjdemo.common.dto.Dto;

/**
 * Created by Benson on 2018/3/13.
 */
public class OperLogDto extends Dto {

    private String id;
    /**操作app模块**/
    private String module;

    /**操作者id**/
    private String operatorId;

    /**请求方法**/
    private String method;

    /**请求路径**/
    private String uri;

    /**请求参数**/
    private String parameter;

    private String operateName;

    private String operatePhone;

    private String operateContent;


    public OperLogDto() {
    }

    public OperLogDto(String id, String module, String operatorId, String method, String uri, String parameter, String operateName, String operatePhone, String operateContent) {
        this.id = id;
        this.module = module;
        this.operatorId = operatorId;
        this.method = method;
        this.uri = uri;
        this.parameter = parameter;
        this.operateName = operateName;
        this.operatePhone = operatePhone;
        this.operateContent = operateContent;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getOperatePhone() {
        return operatePhone;
    }

    public void setOperatePhone(String operatePhone) {
        this.operatePhone = operatePhone;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
