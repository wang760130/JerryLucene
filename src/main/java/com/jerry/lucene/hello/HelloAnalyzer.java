package com.jerry.lucene.hello;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

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
	
	public static void main(String[] args) {
		Analyzer stamdarAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_35);
		Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer whitespaceAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
		
		String txt = "This is my house, I am come from hangzhou alibaba";
		HelloAnalyzer.displayToken("content", txt, stamdarAnalyzer);
		HelloAnalyzer.displayToken("content", txt, stopAnalyzer);
		HelloAnalyzer.displayToken("content", txt, simpleAnalyzer);
		HelloAnalyzer.displayToken("content", txt, whitespaceAnalyzer);

		
	}
}
