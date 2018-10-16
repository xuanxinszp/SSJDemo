package com.xuanxinszp.ssjdemo.manage.common.base;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author szp
 * @create 2018-03-07
 **/
public class BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * 带参重定向
     *
     * @param path
     * @return
     */
    protected String redirect(String path) {
        return "redirect:" + path;
    }

    /**
     * 不带参重定向
     *
     * @param response
     * @param path
     * @return
     */
    protected String redirect(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取分页请求
     * @return
     */
    protected PageRequest getPageRequest(){
        int page = 1;
        int size = 10;
        Sort sort = null;
        try {
            String sortName = request.getParameter("sortName");
            String sortOrder = request.getParameter("sortOrder");
            if(StringUtils.isNotBlank(sortName) && StringUtils.isNotBlank(sortOrder)){
                if(sortOrder.equalsIgnoreCase("desc")){
                    sort = new Sort(Sort.Direction.DESC, sortName);
                }else{
                    sort = new Sort(Sort.Direction.ASC, sortName);
                }
            }
            String pageNumber=request.getParameter("pageNumber");
            page = Integer.parseInt(pageNumber) - 1;
            size = Integer.parseInt(request.getParameter("pageSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return pageRequest;
    }

    /**
     * 获取分页请求
     * @param sort 排序条件
     * @return
     */
    protected PageRequest getPageRequest(Sort sort){
        int page = 0;
        int size = 10;
        try {
            String sortName = request.getParameter("sortName");
            String sortOrder = request.getParameter("sortOrder");
            if(StringUtils.isNotBlank(sortName) && StringUtils.isNotBlank(sortOrder)){
                if(sortOrder.equalsIgnoreCase("desc")){
                    sort.and(new Sort(Sort.Direction.DESC, sortName));
                }else{
                    sort.and(new Sort(Sort.Direction.ASC, sortName));
                }
            }
            page = Integer.parseInt(request.getParameter("pageNumber")) - 1;
            size = Integer.parseInt(request.getParameter("pageSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return pageRequest;
    }

}
