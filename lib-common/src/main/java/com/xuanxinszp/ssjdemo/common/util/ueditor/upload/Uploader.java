package com.xuanxinszp.ssjdemo.common.util.ueditor.upload;



import com.xuanxinszp.ssjdemo.common.util.ueditor.define.State;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;
	BinaryUploader binaryUploader = null;
	Base64Uploader base64Uploader=null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
		this.binaryUploader=new BinaryUploader();
		this.base64Uploader=new Base64Uploader();
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = base64Uploader.save(this.request.getParameter(filedName),this.conf);
		} else {
			state = binaryUploader.save(this.request, this.conf);
		}

		return state;
	}
}
