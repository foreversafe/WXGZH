package com.example.demo.wx.accesstoken;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.wx.util.UploadMediaApiUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月14日 上午9:09:22
 * @ClassName 类名称
 * @Description 类描述上传素材servlet
 */
@WebServlet(name = "UploadMediaServlet")
public class UploadMediaServlet extends HttpServlet {
	final String appId = "wxda6a4db6aa75279d";
	final String appSecret = "fccdb9b903e853d0396455790ad52a31";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UploadMediaApiUtil uploadMediaApiUtil = new UploadMediaApiUtil();
//		String appId = "wx0aa26453a7ec9df7";
//		String appSecret = "2819f0c98199daef39cb6220b4d01b96";
		String accessToken = uploadMediaApiUtil.getAccessToken(appId, appSecret);

		String filePath = "D:\\testVideo.mp4";
		File file = new File(filePath);
		String type = "IMAGE";
		JSONObject jsonObject = uploadMediaApiUtil.uploadMedia(file, accessToken, type);
//		{"access_token":"15_db7772-9LyOd0EnKkfect7pWerKTdv7Hr7AsCsgfwZITiT661zTeZKCi7nH2QuBLIANZFVyLm-p8VPa32WriXA8ojFkqi2C1mPf8ZmnsW3v0Q_-kecZgbn93ntGUvQuxKGj_Yh6fgAsUYbBkJTRhABAVFD","expires_in":7200}
//		{"created_at":1542158545,"media_id":"CJ80vflpDFLwxU1bMPt7Qkxs_hF10-zL1BMEHxJ2D6f2dccEHRGM3ExgWaXA1-Ir","type":"image"}
		System.out.println(jsonObject.toString());
	}
}
