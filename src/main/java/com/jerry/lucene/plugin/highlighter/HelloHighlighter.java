package com.jerry.lucene.plugin.highlighter;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月19日
 */
public class HelloHighlighter {

	public static void highlighter(String txt, String parse) {
		try {
			Query query = new QueryParser(Version.LUCENE_35, "field", new MMSegAnalyzer()).parse(parse);
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragment = new SimpleSpanFragmenter(scorer);
			Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
			Highlighter highlighter = new Highlighter(formatter,scorer);
			highlighter.setTextFragmenter(fragment);
			String str = highlighter.getBestFragment(new MMSegAnalyzer(), "field", txt);
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
