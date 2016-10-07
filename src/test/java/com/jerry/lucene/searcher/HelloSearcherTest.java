package com.jerry.lucene.searcher;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.junit.Test;


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
	public void searchererByTermTest() {
		HelloSearcher.searcherByTerm("content","like",3);
	}
	
	@Test
	public void searcherByTermRangeTest() {
		//查询name以a开头和s结尾的
		HelloSearcher.searcherByTermRange("name","a","s",10);
		
		//由于attachs是数字类型，使用TermRange无法查询
//		HelloSearcher.searcherByTermRange("attach","2","10", 5);
	}
	
	@Test
	public void searcherByNumRange() {
		HelloSearcher.searcherByNumricRange("attachs", 2, 4, 5);
	}
	
	@Test
	public void searcherByPrefixTest() {
		HelloSearcher.searcherByPrefix("name", "j", 10);
	}
	
	@Test
	public void searcherByWildcardTest() {
		//匹配@itat.org结尾的所有字符
		HelloSearcher.searcherByWildcard("email", "*@itat.org", 10);
		//匹配j开头的有三个字符的name
		HelloSearcher.searcherByWildcard("name", "j???", 10);
	}
	
	@Test
	public void searcherByBooleanTest() {
		HelloSearcher.searcherByBoolean("name", "zhangsan", "content", "welcome", 10);
	}
	
	@Test
	public void searcherByPhraseTest() {
		HelloSearcher.searcherByPhrase("name", "zhangsan", "lisi", 2, 10);
	}
	
	@Test
	public void searcherByFuzzyTest() {
		HelloSearcher.searcherByFuzzy("name", "aohn", 0.3F, 0, 10);
	}
	
	public void searcherByQueryParseTest() throws ParseException {
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
		HelloSearcher.searcherByQueryParse(query, 10);
	}
	
	@Test
	public void searcherBySortTest() {
		//Sort.INDEXORDER通过doc的id进行排序
		HelloSearcher.searcherBySort("content", "java", Sort.INDEXORDER, 50);
		
		//使用默认的评分排序
		HelloSearcher.searcherBySort("content", "java", Sort.RELEVANCE, 50);
		
		HelloSearcher.searcherBySort("content", "java", null, 50);
		
		//通过文件的大小排序
		HelloSearcher.searcherBySort("content", "java", new Sort(new SortField("size",SortField.INT)), 50);
		//通过日期排序
		HelloSearcher.searcherBySort("content", "java", new Sort(new SortField("date",SortField.LONG)), 50);
		//通过文件名排序
		HelloSearcher.searcherBySort("content", "java", new Sort(new SortField("filename", SortField.STRING)), 50);
		
		//通过设置SortField最后一个参数设置是否反转排序
		HelloSearcher.searcherBySort("content", "java", new Sort(new SortField("filename", SortField.STRING,true)), 50);
	
		HelloSearcher.searcherBySort("content", "java", new Sort(new SortField("size",SortField.INT),SortField.FIELD_SCORE), 50);
	}

	@Test
	public void searcherByFilterTest() {
		Filter filter = new TermRangeFilter("filename", "java.hhh","java.she",true, true);
		filter = NumericRangeFilter.newIntRange("size",500,900,true,true);
		//可以通过一个Query进行过滤
		filter = new QueryWrapperFilter(new WildcardQuery(new Term("filename","*.txt")));
		HelloSearcher.searcherByFilter("content","java", filter, 50);
	}
	
	@Test
	public void searcherByQueryTest() {
		Query query = new WildcardQuery(new Term("filename","A*"));
		HelloSearcher.searcherByQuery(query, 50);
	}
}
