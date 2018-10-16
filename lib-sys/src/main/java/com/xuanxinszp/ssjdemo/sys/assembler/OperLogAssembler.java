package com.xuanxinszp.ssjdemo.sys.assembler;



import com.xuanxinszp.ssjdemo.sys.dto.OperLogDto;
import com.xuanxinszp.ssjdemo.sys.entity.OperLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benson on 2018/3/13.
 */
public class OperLogAssembler {

    public static OperLog convertToEntity(OperLogDto operLogDto, OperLog entity){
        if(entity==null){
            entity = new OperLog();
        }
        entity.setId(operLogDto.getId());
        entity.setOperatorId(operLogDto.getOperatorId());
        entity.setMethod(operLogDto.getMethod());
        entity.setParameter(operLogDto.getParameter());
        entity.setUri(operLogDto.getUri());
        entity.setModule(operLogDto.getModule());
        entity.setOperatePhone(operLogDto.getOperatePhone());
        entity.setOperateContent(operLogDto.getOperateContent());
        entity.setOperateName(operLogDto.getOperateName());
        return entity;
    }


    public static OperLogDto convertToDto(OperLog entity){
        if (null==entity) return null;

        OperLogDto dictDto = new OperLogDto(entity.getId(),entity.getModule(),entity.getOperatorId(),entity.getMethod(),entity.getUri(),entity.getParameter(), entity.getOperateName(), entity.getOperatePhone(), entity.getOperateContent());

        return dictDto;
    }


    public static List<OperLogDto> convertToDtoList(List<OperLog> operLogs){
        List<OperLogDto> dtos= new ArrayList<>();
        operLogs.forEach(entity -> dtos.add(convertToDto(entity)));
        return dtos;
    }
    
}
