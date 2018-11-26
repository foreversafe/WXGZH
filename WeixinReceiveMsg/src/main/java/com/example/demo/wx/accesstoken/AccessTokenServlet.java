package com.example.demo.wx.accesstoken;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.wx.util.NetWorkUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月13日 下午5:45:58
 * @ClassName 类名称
 * @Description 类描述
 */
public class AccessTokenServlet extends HttpServlet {
    static Logger logger = LoggerFactory.getLogger(AccessTokenServlet.class);

    @Override
    public void init() throws ServletException {
        System.out.println("-----启动AccessTokenServlet-----");
        super.init();

//        final String appId = getInitParameter("appId");
//        final String appSecret = getInitParameter("appSecret");
//		测试号信息
//		appID wxda6a4db6aa75279d
//		appsecret fccdb9b903e853d0396455790ad52a31
		final String appId = "wxda6a4db6aa75279d";
		final String appSecret = "fccdb9b903e853d0396455790ad52a31";

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

}
