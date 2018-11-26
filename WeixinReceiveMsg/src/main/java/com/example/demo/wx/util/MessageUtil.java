package com.example.demo.wx.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.example.demo.wx.controller.UploadMediaController;
import com.example.demo.wx.trans.YouDaoAPI;
import com.example.demo.wx.weather.WeatherInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月13日 下午6:00:37
 * @ClassName 类名称
 * @Description 类描述
 */
public class MessageUtil {
	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return map
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		System.out.println("获取输入流");
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			System.out.println(e.getName() + "|" + e.getText());
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	/**
	 * 根据消息类型 构造返回消息
	 */
	public static String buildXml(Map<String, String> map) {
		String result;
		String msgType = map.get("MsgType").toString();
		System.out.println("MsgType:" + msgType);
		if (msgType.toUpperCase().equals("TEXT")) {
			String content = map.get("Content").toString();
			// 过滤表情
			EmojiUtil emojiUtil = new EmojiUtil();
			String unicodeEmoji = emojiUtil.filterEmoji(content); // unicode编码的Emoji
			if (content.equals("音乐")) {
				result = buildMusicMessage(map);
				return result;
			} else if (content.equals("文本")) {
				result = buildTextMessage(map, "Cherry的小小窝, 请问客官想要点啥?");
				return result;
			} else if (content.equals("图文")) {
				result = buildNewsMessage(map);
				return result;
			} else if (content.equals("图片")) {
				result = buildTextMessage(map, "请客官发送图片");
				return result;
			} else if (content.equals("语音")) {
				result = buildTextMessage(map, "请客官发送语音");
				return result;
			} else if (content.equals("视频")) {
				result = buildTextMessage(map, "请客官发送视频");
				return result;
			} else if (content.contains("天气") && !"".equals(content)) {
				/* 查询天气 */

				if (content.contains(":") || content.contains("：")) {
					String cityName = null;
					if (content.contains(":")) {
						cityName = content.substring(content.lastIndexOf(":") + 1, content.length());
					} else if (content.contains("：")) {
						cityName = content.substring(content.lastIndexOf("：") + 1, content.length());
					}
					WeatherInfo weather = new WeatherInfo();
					String weaInfo = weather.getWeatherInfo(cityName);
					result = buildTextMessage(map, weaInfo);
					return result;
				} else {
					String notice = "查询天气的正确姿势: 天气:城市名\n请客官输入正确的格式哟~";
					result = buildTextMessage(map, notice);
					return result;
				}
			} else if (content.contains("翻译") && !"".equals(content)) {
				if (content.contains(":")) {
					String word = content.substring(content.lastIndexOf(":") + 1, content.length()).trim();
					YouDaoAPI translateInfo = new YouDaoAPI();
					String weaInfo = null;
					try {
						weaInfo = translateInfo.translate(word);
					} catch (Exception e) {
						e.printStackTrace();
					}
					result = buildTextMessage(map, weaInfo);
					return result;
				} else {
					String notice = "翻译的正确姿势: 翻译:翻译文本\n请客官输入正确的格式哟~";
					result = buildTextMessage(map, notice);
					return result;
				}
			} else if (content.contains("/:") || content.contains("\\:")
					|| content.contains("[") && content.contains("]") || unicodeEmoji.contains("\\")) {
				result = buildTextMessage(map, "客官发送的内容很特别哟~/:heart\n" + content);
				return result;
			} else {
				result = buildTextMessage(map, "");
			}
			result = buildOtherMessage(map);
		} else if (msgType.toUpperCase().equals("IMAGE")) {
			result = buildImageMessage(map);
		} else if (msgType.toUpperCase().equals("VOICE")) {
			result = buildVoiceMessage(map);
		} else if (msgType.toUpperCase().equals("VIDEO")) {
			result = buildVideoMessage(map);
		} else {
			result = buildOtherMessage(map);
		}
		return result;
	}

	private static String getUtcTime() {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm");// 设置显示格式
		String nowTime = df.format(dt);
		long dd = (long) 0;
		try {
			dd = df.parse(nowTime).getTime();
		} catch (Exception e) {

		}
		return String.valueOf(dd);
	}

	/**
	 * 构造文本消息
	 *
	 * @param map
	 * @param content
	 * @return
	 */
	public static String buildTextMessage(Map<String, String> map, String content) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 文本消息XML数据格式
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), content);
	}

	/**
	 * 构建图片消息
	 * 
	 * @param map
	 * @param picUrl
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public static String buildImageMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		/* 返回指定的图片(该图片是上传为素材的,获得其media_id) */
//		String media_id = "CKrCrX0Qm4PX2JEbTIK26hdQtS6efnjD3KviLOP3-V11wpgVwzS6DWCOF-DjHF_s";

		/* 返回用户发过来的图片 */
		String media_id = map.get("MediaId");
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[image]]></MsgType>" + "<Image>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Image>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id);
	}

	/**
	 * 构造语音消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildVoiceMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		/* 返回用户发过来的语音 */
		String media_id = map.get("MediaId");
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[voice]]></MsgType>" + "<Voice>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "</Voice>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id);
	}

	/**
	 * 回复视频消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildVideoMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title = "客官发过来的视频哟~~";
		String description = "客官您呐,现在肯定很开心,对不啦 嘻嘻��";
		/* 返回用户发过来的视频 */
//		 String media_id = map.get("MediaId");
		String media_id = "YsiDU4aHfEB4jxSbDXTf81iI3kCH6ps4mJAaJD6MaocHJD7VvBDsqPssx67LMZBL";
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[video]]></MsgType>" + "<Video>"
						+ "   <MediaId><![CDATA[%s]]></MediaId>" + "   <Title><![CDATA[%s]]></Title>"
						+ "   <Description><![CDATA[%s]]></Description>" + "</Video>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), media_id, title, description);
	}

	/**
	 * 回复其他消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildOtherMessage(Map<String, String> map) {
		String result;
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		result = String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), "请回复如下关键词：\n文本\n图片\n语音\n音乐\n图文\n天气:(城市名)\n翻译:(日文)こんにちは");
		return result;
	}

	/**
	 * 回复音乐消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildMusicMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title = "亲爱的路人";
		String description = "多听音乐 心情棒棒 嘻嘻��";
		String hqMusicUrl = "http://www.kugou.com/song/20qzz4f.html?frombaidu#hash=20C16B9CCCCF851D1D23AF52DD963986&album_id=0";
		return String.format("<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>" + "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[music]]></MsgType>" + "<Music>" + "   <Title><![CDATA[%s]]></Title>"
				+ "   <Description><![CDATA[%s]]></Description>" + "   <MusicUrl>< ![CDATA[%s] ]></MusicUrl>" + // 非必须项
																												// 音乐链接
				"   <HQMusicUrl><![CDATA[%s]]></HQMusicUrl>" + // 非必须项 高质量音乐链接，WIFI环境优先使用该链接播放音乐
				"</Music>" + "</xml>", fromUserName, toUserName, getUtcTime(), title, description, hqMusicUrl,
				hqMusicUrl);
	}

	/**
	 * 返回图文消息
	 * 
	 * @param map
	 * @return
	 */
	public static String buildNewsMessage(Map<String, String> map) {
		String fromUserName = map.get("FromUserName");
		String toUserName = map.get("ToUserName");
		String title1 = "HAP审计的实现和使用";
		String description1 = "由于HAP框架用的是Spring+SpringMVC+Mybatis，其中Mybatis中的拦截器可以选择在被拦截的方法前后执行自己的逻辑。所以我们通过拦截器实现了审计功能，当用户对某个实体类进行增删改操作时，拦截器可以拦截，然后将操作的数据记录在审计表中，便于用户以后审计。";
		String picUrl1 = "http://upload-images.jianshu.io/upload_images/7855203-b9e9c9ded8a732a1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
		String textUrl1 = "http://blog.csdn.net/a1786223749/article/details/78330890";

		String title2 = "KendoUI之Grid的问题详解";
		String description2 = "kendoLov带出的值出现 null和undefined";
		String picUrl2 = "https://demos.telerik.com/kendo-ui/content/shared/images/theme-builder.png";
		String textUrl2 = "http://blog.csdn.net/a1786223749/article/details/78330908";

		return String.format("<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>"
				+ "<FromUserName><![CDATA[%s]]></FromUserName>" + "<CreateTime>%s</CreateTime>"
				+ "<MsgType><![CDATA[news]]></MsgType>" + "<ArticleCount>2</ArticleCount>" + // 图文消息个数，限制为8条以内
				"<Articles>" + // 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
				"<item>" + "<Title><![CDATA[%s]]></Title> " + "<Description><![CDATA[%s]]></Description>"
				+ "<PicUrl><![CDATA[%s]]></PicUrl>" + // 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
				"<Url><![CDATA[%s]]></Url>" + // 点击图文消息跳转链接
				"</item>" + "<item>" + "<Title><![CDATA[%s]]></Title>" + "<Description><![CDATA[%s]]></Description>"
				+ "<PicUrl><![CDATA[%s]]]></PicUrl>" + "<Url><![CDATA[%s]]]></Url>" + "</item>" + "</Articles>"
				+ "</xml>", fromUserName, toUserName, getUtcTime(), title1, description1, picUrl1, textUrl1, title2,
				description2, picUrl2, textUrl2);
	}

	/**
	 * 天气
	 * 
	 * @param map
	 * @return
	 */
	public static String buildXmlWeather(Map<String, String> map) {
		String result = "";
		String msgType = map.get("MsgType").toString().toUpperCase();
		String content = map.get("Content");
		if ("TEXT".equals(msgType)) {
			/* 查询天气 */
			if (content.contains("天气") && !"".equals(content)) {
				if (content.contains(":")) {
					String cityName = content.substring(content.lastIndexOf(":") + 1, content.length());
					WeatherInfo weather = new WeatherInfo();
					String weaInfo = weather.getWeatherInfo(cityName);
					result = buildTextMessage(map, weaInfo);
				} else {
					String notice = "查询天气的正确姿势: 天气:城市名\n请客官输入正确的格式哟~";
					result = buildTextMessage(map, notice);
				}
			} else {
				result = buildTextMessage(map, "");
			}
		}
		return result;
	}
}
