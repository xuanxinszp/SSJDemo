package com.xuanxinszp.ssjdemo.common.util.ueditor.upload;


import com.xuanxinszp.ssjdemo.common.bean.oss.OSSManager;
import com.xuanxinszp.ssjdemo.common.enums.OssDirType;
import com.xuanxinszp.ssjdemo.common.util.SpringContextUtil;
import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.common.util.ueditor.PathFormat;
import com.xuanxinszp.ssjdemo.common.util.ueditor.define.AppInfo;
import com.xuanxinszp.ssjdemo.common.util.ueditor.define.BaseState;
import com.xuanxinszp.ssjdemo.common.util.ueditor.define.FileType;
import com.xuanxinszp.ssjdemo.common.util.ueditor.define.State;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {



	@Autowired
	OSSManager ossManager;

	public  final State save(HttpServletRequest request,
							 Map<String, Object> conf) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			if(multipartFile==null){
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			String tempFileName = StringUtil.getUUID() + suffix;
			InputStream is = multipartFile.getInputStream();
			if(ossManager==null){
				ossManager=SpringContextUtil.getBean(OSSManager.class);
			}

			String ossUrl = ossManager.upload(OssDirType.ARTICLE, Boolean.TRUE, is, tempFileName);
			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			State storageState = new BaseState(true);
			is.close();
			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(ossUrl));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
