package com.jerry.lucene.hello;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月26日
 */
public class HelloAnalyzer {
	
	public static void displayToken(String fieldName, String str, Analyzer analyzer) {
		try {
			TokenStream stream = analyzer.tokenStream(fieldName, new StringReader(str));
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			while(stream.incrementToken()) {
				System.out.print("["+cta+"]");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void displayAllToTokenInfo(String fieldName, String str, Analyzer analyzer) {
		try {
			TokenStream stream = analyzer.tokenStream(fieldName, new StringReader(str));
			PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
			OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			TypeAttribute ta = stream.addAttribute(TypeAttribute.class);
			
			for(;stream.incrementToken();) {
				System.out.print(pia.getPositionIncrement() + ":");
				System.out.print(cta + "[" +oa.startOffset()+ "]" + "-" + "[" + oa.endOffset() + "]" + "-" + "[" + ta.type() + "]");
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
