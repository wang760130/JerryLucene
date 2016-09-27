package com.jerry.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.jerry.lucene.hello.HelloAnalyzer;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月27日
 */
public class MMSegTest {

	@Test
	public void test() {
		Analyzer mmsegAnalyzer = new MMSegAnalyzer();
		String txt = "我来自中国浙江省杭州市";
		HelloAnalyzer.displayToken("content", txt, mmsegAnalyzer);
	}
}
