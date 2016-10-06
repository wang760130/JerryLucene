package com.jerry.lucene.searcher;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.jerry.lucene.searcher.HelloSearcher;

public class HelloSearcherTest {

	@Test
	public void deleteAllTest() {
		HelloSearcher.deleteAll();
	}
	
	@Test
	public void indexTest() {
		HelloSearcher.index();
	}
	
	@Test
	public void searchByTermTest() {
		HelloSearcher.searchByTerm("content","like",3);
	}
	
	@Test
	public void searchByTermRangeTest() {
		//查询name以a开头和s结尾的
		HelloSearcher.searchByTermRange("name","a","s",10);
		
		//由于attachs是数字类型，使用TermRange无法查询
//		HelloSearcher.searchByTermRange("attach","2","10", 5);
	}
	
	@Test
	public void searchByNumRange() {
		HelloSearcher.searchByNumricRange("attachs", 2, 4, 5);
	}
	
	@Test
	public void searchByPrefixTest() {
		HelloSearcher.searchByPrefix("name", "j", 10);
	}
	
	@Test
	public void searchByWildcardTest() {
		//匹配@itat.org结尾的所有字符
		HelloSearcher.searchByWildcard("email", "*@itat.org", 10);
		//匹配j开头的有三个字符的name
		HelloSearcher.searchByWildcard("name", "j???", 10);
	}
	
	@Test
	public void searchByBooleanTest() {
		HelloSearcher.searchByBoolean("name", "zhangsan", "content", "welcome", 10);
	}
	
	@Test
	public void searchByPhraseTest() {
		HelloSearcher.searchByPhrase("name", "zhangsan", "lisi", 2, 10);
	}
	
	@Test
	public void searchByFuzzyTest() {
		HelloSearcher.searchByFuzzy("name", "aohn", 0.3F, 0, 10);
	}
	
	public void searchByQueryParseTest() throws ParseException {
		// 创建QueryParser对象
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		
		// 改变空格的默认操作符，以下可以改成AND
		parser.setDefaultOperator(Operator.AND);
		
		// 索搜content中包含like的
		Query query = parser.parse("like");
		
		// 空格默认就是OR
		query = parser.parse("backetball football");
		
		// 改变索搜域为name为mike
		query = parser.parse("name:mike");
		
		// 同样可以使用*和？来进行通配符匹配
		query = parser.parse("name:j*");
		
		// 开启第一个字符的通配符匹配，默认关闭，应为效率不高
		parser.setAllowLeadingWildcard(true);
		// 通配符默认不能放在首位
		query = parser.parse("emial:*@itat.org");
		
		// 匹配name中没有mike但是content中必须有football的，+和-要放置到域说明前面
		query = parser.parse("name:mike - football+");
		
		// 匹配一个区间，主要：TO必须是大写
		query = parser.parse("id:[1 TO 3]");
		// 闭区间
		query = parser.parse("id:(1 TO 3)");
		
		// 完全匹配
		query = parser.parse("\"I like football\"");
		
		// 距离一个单词
		query = parser.parse("\"I football\"~1");
		
		// 模糊查询
		query = parser.parse("name:make~");
		HelloSearcher.searchByQueryParse(query, 10);
	}
	
	

}
