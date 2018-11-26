package com.example.demo.wx.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.wx.accesstoken.AccessToken;
import com.example.demo.wx.accesstoken.AccessTokenInfo;
import com.example.demo.wx.util.MessageUtil;
import com.example.demo.wx.util.NetWorkUtil;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月6日 上午11:40:13
 * @ClassName 类名称
 * @Description 类描述
 */
@RestController
public class AccessTokenController {

	static Logger logger = LoggerFactory.getLogger(AccessTokenController.class);

	/*
	 * 自定义token, 用作生成签名,从而验证安全性
	 */
	static final String TOKEN = "houzhibo";
	static final String appId = "wxda6a4db6aa75279d";
	static final String appSecret = "fccdb9b903e853d0396455790ad52a31";
	boolean flag = false;

	@RequestMapping("/login")
	public Map<String, String> requestFailed() {

		Map<String, String> map = new HashMap<>();
		map.put("code", "200");
		map.put("msg", "success");
		return map;
	}

//	@GetMapping("/WxServlet")
//	public void receiveMsg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		receiveMessage(req, resp);
//
//	}

	@PostMapping("/wx")
	public void receiveMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		System.out.println("请求进入");
		String result = "";
		try {
			Map<String, String> map = MessageUtil.parseXml(req);

			System.out.println("开始构造消息");
			result = MessageUtil.buildXml(map);
			System.out.println(result);

			if (result.equals("")) {
				result = "未正确响应";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发生异常：" + e.getMessage());
		}
		resp.getWriter().println(result);
	}

	@RequestMapping("/getAccessToken")
	public String getAccessToken() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// 获取accessToken
						AccessTokenInfo.accessToken = getAccessToken(appId, appSecret);
						// 获取成功
						if (AccessTokenInfo.accessToken != null) {
							// 获取到access_token 休眠7000秒,大约2个小时左右
							Thread.sleep(7000 * 1000);
						} else {
							// 获取失败
							Thread.sleep(1000 * 3); // 获取的access_token为空 休眠3秒
						}
					} catch (Exception e) {
						System.out.println("发生异常：" + e.getMessage());
						e.printStackTrace();
						try {
							Thread.sleep(1000 * 10); // 发生异常休眠1秒
						} catch (Exception e1) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
		return "ok";
	}

	private AccessToken getAccessToken(String appId, String appSecret) {
		NetWorkUtil netHelper = new NetWorkUtil();
		/**
		 * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
		 */
		String Url = String.format(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId,
				appSecret);
		// 此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":7200}
		String result = netHelper.getHttpsResponse(Url, "");
		System.out.println("获取到的access_token=" + result);

		// 使用FastJson将Json字符串解析成Json对象
		JSONObject json = JSON.parseObject(result);
		AccessToken token = new AccessToken();
		token.setTokenName(json.getString("access_token"));
		token.setExpireSecond(json.getInteger("expires_in"));
		return token;
	}

	@GetMapping("/wx")
	public void weChatServlet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			System.out.println("-----开始校验签名-----");

			/**
			 * 接收微信服务器发送请求时传递过来的参数
			 */
			String signature = req.getParameter("signature");
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce"); // 随机数
			String echostr = req.getParameter("echostr");// 随机字符串

			/**
			 * 将token、timestamp、nonce三个参数进行字典序排序 并拼接为一个字符串
			 */
			String sortStr = sort(TOKEN, timestamp, nonce);
			/**
			 * 字符串进行shal加密
			 */
			String mySignature = shal(sortStr);
			/**
			 * 校验微信服务器传递过来的签名 和 加密后的字符串是否一致, 若一致则签名通过
			 */
			if (!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)) {
				System.out.println("-----签名校验通过-----" + echostr);
				resp.getWriter().write(echostr);
				flag = true;
			} else {
				System.out.println("-----校验签名失败-----");
			}

	}

	/**
	 * 参数排序
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 字符串进行shal加密
	 * 
	 * @param str
	 * @return
	 */
	public String shal(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	

}
