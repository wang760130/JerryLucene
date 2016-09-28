package com.jerry.lucene.analyzer;

import java.io.IOException;
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
		if(!input.incrementToken()) {
			return false;
		}
		
		if(cta.toString().equals("中国")) {
			cta.setEmpty();
			cta.append("大陆");
		}
//		System.out.println(cta);
		return true;
	}

}
