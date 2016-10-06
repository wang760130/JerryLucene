package com.jerry.lucene.analyzer;

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
			// 位置增量的属性，存储词汇单元之间的距离
			PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
			// 每个词汇单元的位置偏移量
			OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
			// 存储每一个词汇单元的信息（分词单元信息）
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			// 使用的分词器的类型信息
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
