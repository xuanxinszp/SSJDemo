package com.xuanxinszp.ssjdemo.sys.service;

import com.google.common.collect.Lists;

import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.sys.assembler.OperLogAssembler;
import com.xuanxinszp.ssjdemo.sys.bean.OperLogReq;
import com.xuanxinszp.ssjdemo.sys.dto.OperLogDto;
import com.xuanxinszp.ssjdemo.sys.entity.OperLog;
import com.xuanxinszp.ssjdemo.sys.jpa.OperLogRepository;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created by Benson on 2018/3/13.
 */
@Service
public class OperLogService {

    @Autowired
    private OperLogRepository pubOperLogRepository;


    public OperLog findOne(String id) {
        return pubOperLogRepository.findOne(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public OperLog save(OperLogDto operLogDto) {
        Assert.notNull(operLogDto, "");

        OperLog pubOperLog;
        if (StringUtil.isNotNil(operLogDto.getId())) {  // edit
            pubOperLog = pubOperLogRepository.findOne(operLogDto.getId());
            pubOperLog = OperLogAssembler.convertToEntity(operLogDto, pubOperLog);
        } else {
            pubOperLog = OperLogAssembler.convertToEntity(operLogDto, null);
        }

        return pubOperLogRepository.save(pubOperLog);
    }


    public Page<OperLogDto> findPage(OperLogReq operLogReq) {
        PageRequest pageRequest = new PageRequest(operLogReq.getPageNumber(), operLogReq.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Specification<OperLog> spec = (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            // search by parameters
            if (StringUtils.isNotEmpty(operLogReq.getQueryLike())) {
                Path<String> name = root.get("uri");
                Predicate nickLike = builder.like(name, "%" + operLogReq.getQueryLike() + "%", '/');
                predicates.add(builder.or(nickLike));
            }
            query.orderBy(builder.desc(root.get("createTime")));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<OperLog> operLogPage = pubOperLogRepository.findAll(spec,pageRequest);
        Page<OperLogDto> dtoPage = new PageImpl<>(OperLogAssembler.convertToDtoList(operLogPage.getContent()), pageRequest,operLogPage.getTotalElements());
        return dtoPage;
    }
}
