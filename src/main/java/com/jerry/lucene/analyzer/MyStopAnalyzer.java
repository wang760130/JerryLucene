package com.jerry.lucene.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月27日
 */
public class MyStopAnalyzer extends Analyzer {
	
	@SuppressWarnings("rawtypes")
	private Set stops;
	
	@SuppressWarnings("unchecked")
	public MyStopAnalyzer() {
		// 将原有的停用词加入到现有停用词
		stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}
	
	@SuppressWarnings("unchecked")
	public MyStopAnalyzer(String[] sws) {
		// 会自动将字符串转换为set
		stops = StopFilter.makeStopSet(Version.LUCENE_35, sws, true);
		
		// 将原有的停用词加入到现有停用词
		stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		// 为这个分词器设定过滤链和Tokennizer
		return new StopFilter(Version.LUCENE_35, 
				new LowerCaseFilter(Version.LUCENE_35,
						new LetterTokenizer(Version.LUCENE_35, reader)), stops);
	}

}
