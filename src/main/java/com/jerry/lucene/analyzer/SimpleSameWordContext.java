package com.jerry.lucene.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月1日
 */
public class SimpleSameWordContext implements SameWordContext {
	
	Map<String, String[]> maps = new HashMap<String, String[]>();

	public SimpleSameWordContext() {
		maps.put("中国",new String[]{"天朝","大陆"});
		maps.put("我",new String[]{"咱","俺"});
	}
	
	@Override
	public String[] getSameWords(String name) {
		return maps.get(name);
	}

}
