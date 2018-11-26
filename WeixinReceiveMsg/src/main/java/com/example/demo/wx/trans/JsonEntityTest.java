package com.example.demo.wx.trans;

import com.alibaba.fastjson.JSON;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月14日 下午12:43:10
 * @ClassName 类名称
 * @Description 类描述
 */
public class JsonEntityTest {

	public static void main(String[] args) {
//		String str = "{\"tSpeakUrl\":\"http://openapi.youdao.com/ttsapi?q=%E4%BD%A0%E5%A5%BD&langType=zh-CHS&sign=300814F1EEEB28FE04197F783D34151C&salt=1542170556199&voice=4&format=mp3&appKey=7b3ed1f310426247\",\"web\":[{\"value\":[\"你好\",\"您好\",\"哈啰\",\"喂\"],\"key\":\"Hello\"},{\"value\":[\"凯蒂猫\",\"昵称\",\"匿称\",\"最想当的商品代言人\"],\"key\":\"Hello Kitty\"},{\"value\":[\"哈乐哈乐\",\"乐扣乐扣\"],\"key\":\"Hello Bebe\"}],\"query\":\"hello\",\"translation\":[\"你好\"],\"errorCode\":\"0\",\"dict\":{\"url\":\"yddict://m.youdao.com/dict?le=eng&q=hello\"},\"webdict\":{\"url\":\"http://m.youdao.com/dict?le=eng&q=hello\"},\"basic\":{\"exam_type\":[\"初中\"],\"us-phonetic\":\"helˈō\",\"phonetic\":\"həˈləʊ\",\"uk-phonetic\":\"həˈləʊ\",\"wfs\":[{\"wf\":{\"name\":\"复数\",\"value\":\"hellos或helloes\"}},{\"wf\":{\"name\":\"过去式\",\"value\":\"helloed\"}},{\"wf\":{\"name\":\"过去分词\",\"value\":\"helloed\"}},{\"wf\":{\"name\":\"现在分词\",\"value\":\"helloing\"}}],\"uk-speech\":\"http://openapi.youdao.com/ttsapi?q=hello&langType=en&sign=AAD9F352176DD79972053E7482801557&salt=1542170556199&voice=5&format=mp3&appKey=7b3ed1f310426247\",\"explains\":[\"int. 喂；哈罗\",\"n. 表示问候， 惊奇或唤起注意时的用语\",\"n. (Hello)人名；(法)埃洛\"],\"us-speech\":\"http://openapi.youdao.com/ttsapi?q=hello&langType=en&sign=AAD9F352176DD79972053E7482801557&salt=1542170556199&voice=6&format=mp3&appKey=7b3ed1f310426247\"},\"l\":\"EN2zh-CHS\",\"speakUrl\":\"http://openapi.youdao.com/ttsapi?q=hello&langType=en&sign=AAD9F352176DD79972053E7482801557&salt=1542170556199&voice=4&format=mp3&appKey=7b3ed1f310426247\"}";
//		String str = "{\"tSpeakUrl\":\"http://openapi.youdao.com/ttsapi?q=%E4%BD%A0%E5%A5%BD&langType=zh-CHS&sign=300814F1EEEB28FE04197F783D34151C&salt=1542170556199&voice=4&format=mp3&appKey=7b3ed1f310426247\"}";
		
		String str="{\n" + 
				"	\"tSpeakUrl\": \"http://openapi.youdao.com/ttsapi?q=æç±ä½ &langType=zh-CHS&sign=05C7B9850A991B5D033B09E4B23F01D9&salt=1542168414295&voice=4&format=mp3&appKey=7b3ed1f310426247\",\n" + 
				"	\"web\": [{\n" + 
				"		\"value\": [\"我爱你\", \"爱老虎油\", \"王若琳\", \"爱很简单\"],\n" + 
				"		\"key\": \"I Love You\"\n" + 
				"	}, {\n" + 
				"		\"value\": [\"真的爱你\", \"其实很爱你\", \"我是真的爱你\", \"我真的爱你\"],\n" + 
				"		\"key\": \"I really love you\"\n" + 
				"	}, {\n" + 
				"		\"value\": [\"我是爱你的\", \"真的爱你\", \"爱你我该怎么办\", \"我爱你\"],\n" + 
				"		\"key\": \"I Do love you\"\n" + 
				"	}],\n" + 
				"	\"query\": \"i love you\",\n" + 
				"	\"translation\": [\"我爱你\"],\n" + 
				"	\"errorCode\": \"0\",\n" + 
				"	\"dict\": {\n" + 
				"		\"url\": \"yddict://m.youdao.com/dict?le=eng&q=i+love+you\"\n" + 
				"	},\n" + 
				"	\"webdict\": {\n" + 
				"		\"url\": \"http://m.youdao.com/dict?le=eng&q=i+love+you\"\n" + 
				"	},\n" + 
				"	\"basic\": {\n" + 
				"		\"uk-speech\": \"http://openapi.youdao.com/ttsapi?q=i+love+you&langType=en&sign=F2B916D4EB683569667A37103CAE0804&salt=1542168414295&voice=5&format=mp3&appKey=7b3ed1f310426247\",\n" + 
				"		\"explains\": [\"我爱你。\"],\n" + 
				"		\"us-speech\": \"http://openapi.youdao.com/ttsapi?q=i+love+you&langType=en&sign=F2B916D4EB683569667A37103CAE0804&salt=1542168414295&voice=6&format=mp3&appKey=7b3ed1f310426247\"\n" + 
				"	},\n" + 
				"	\"l\": \"EN2zh-CHS\",\n" + 
				"	\"speakUrl\": \"http://openapi.youdao.com/ttsapi?q=i+love+you&langType=en&sign=F2B916D4EB683569667A37103CAE0804&salt=1542168414295&voice=4&format=mp3&appKey=7b3ed1f310426247\"\n" + 
				"}";
		TranslateResponseDto dto = new TranslateResponseDto();
		dto = JSON.parseObject(str, TranslateResponseDto.class);
		System.out.println(dto);
	}
}
