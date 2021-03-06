package com.jerry.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.jerry.lucene.analyzer.HelloAnalyzer;
import com.jerry.lucene.analyzer.MySameAnalyzer;
import com.jerry.lucene.analyzer.MyStopAnalyzer;
import com.jerry.lucene.analyzer.SameWordContext;
import com.jerry.lucene.analyzer.SimpleSameWordContext;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月26日
 */
public class HelloAnalyzerTest {

	@Test
	public void displayTokenTest() {
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
	
	@Test
	public void displayAllToTokenInfoTest() {
		Analyzer stamdarAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_35);
		Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer whitespaceAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
		
		String txt = "This is my house, I am come from hangzhou alibaba";
		HelloAnalyzer.displayAllToTokenInfo("content", txt, stamdarAnalyzer);
		HelloAnalyzer.displayAllToTokenInfo("content", txt, stopAnalyzer);
		HelloAnalyzer.displayAllToTokenInfo("content", txt, simpleAnalyzer);
		HelloAnalyzer.displayAllToTokenInfo("content", txt, whitespaceAnalyzer);
	}
	
	@Test
	public void myStopAnalyzerTest() {
		Analyzer myStopAnalyzer = new MyStopAnalyzer(new String[] {"I", "This", "is"});
		Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_35);
		String txt = "This is my house, I am come from hangzhou alibaba";

		HelloAnalyzer.displayToken("content", txt, myStopAnalyzer);
		HelloAnalyzer.displayToken("content", txt, stopAnalyzer);
	}
	
	@Test
	public void mmsegAnalyzerTest() {
		Analyzer mmsegAnalyzer = new MMSegAnalyzer();
		String txt = "我来自中国浙江省杭州市";
		HelloAnalyzer.displayToken("content", txt, mmsegAnalyzer);
	}
	
	@Test
	public void mySameAnalyzerTest() {
		SameWordContext sameWordContext = new SimpleSameWordContext();
		Analyzer mmsegAnalyzer = new MySameAnalyzer(sameWordContext);
		String txt = "我来自中国浙江省杭州市";
		HelloAnalyzer.displayToken("content", txt, mmsegAnalyzer);
	}
}
