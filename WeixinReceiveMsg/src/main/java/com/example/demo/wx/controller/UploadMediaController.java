package com.example.demo.wx.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.wx.util.MessageUtil;
import com.example.demo.wx.util.UploadMediaApiUtil;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月14日 上午9:17:48
 * @ClassName 类名称
 * @Description 类描述
 */
@RestController
public class UploadMediaController {

	@RequestMapping("/UploadMediaServlet")
	public static String UploadMediaServlet()
			throws ServletException, IOException {

		UploadMediaApiUtil uploadMediaApiUtil = new UploadMediaApiUtil();
		String appId = "wxda6a4db6aa75279d";
		String appSecret = "fccdb9b903e853d0396455790ad52a31";
		String accessToken = uploadMediaApiUtil.getAccessToken(appId, appSecret);

		String filePath = "D:\\test.jpg";
		File file = new File(filePath);
		String type = "IMAGE";
		JSONObject jsonObject = uploadMediaApiUtil.uploadMedia(file, accessToken, type);
		String typeName = "media_id";
		if ("thumb".equals(type)) {
			typeName = type + "_media_id";
		}
		String mediaId = jsonObject.getString(typeName);
		return mediaId;
	}
}
