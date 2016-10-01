package com.jerry.lucene.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月28日
 */
public class MySameAnalyzer extends Analyzer {

	private SameWordContext sameWordContext;
	
	public MySameAnalyzer(SameWordContext sameWordContext) {
		this.sameWordContext = sameWordContext;
	}
	
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		Dictionary dic = Dictionary.getInstance();
		return new MySameTokenFilter(
				new MMSegTokenizer(new MaxWordSeg(dic), reader), sameWordContext);
	}
	
	

}
