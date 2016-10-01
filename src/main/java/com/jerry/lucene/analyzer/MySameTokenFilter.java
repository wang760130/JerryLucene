package com.jerry.lucene.analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月28日
 */
public class MySameTokenFilter extends TokenFilter {
	
	private CharTermAttribute cta = null;
	private PositionIncrementAttribute pia = null;
	private AttributeSource.State current;
	private Stack<String> sames = null;
	
	protected MySameTokenFilter(TokenStream input) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
		pia = this.addAttribute(PositionIncrementAttribute.class);
		sames = new Stack<String>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		while(sames.size() > 0) {
			// 将元素出栈，并且获取这个同义词
			String str = sames.pop();
			// 还原状态
			restoreState(current);
			
			cta.setEmpty();
			cta.append(str);
			
			// 设置位置0
			pia.setPositionIncrement(0);
			return true;
		}
		
		if(!input.incrementToken()) {
			return false;
		}
		
		if(getSameWords(cta.toString())) {
			// 如果有同义词将当前状态先保存
			current = captureState();
		}
		
		return true;
	}

	private boolean getSameWords(String name) {
		Map<String, String[]> maps = new HashMap<String, String[]>();
		maps.put("中国",new String[]{"天朝","大陆"});
		maps.put("我",new String[]{"咱","俺"});
		String[] sws = maps.get(name);
		if(sws != null) {
			for(String str : sws) {
				sames.push(str);
			}
			return true;
		}
		return false;
	}
}
