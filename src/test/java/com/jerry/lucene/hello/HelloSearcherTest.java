package com.jerry.lucene.hello;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class HelloSearcherTest {

	public static void main(String[] args) throws ParseException {
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
//		HelloSearcherTest.searchByQueryParse(query, 10);
	}

}
