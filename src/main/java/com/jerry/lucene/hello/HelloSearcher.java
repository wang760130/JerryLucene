package com.jerry.lucene.hello;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月19日
 */
public class HelloSearcher {
	
	private static Directory directory;
	private static IndexReader reader;
	
	static {
		directory = new RAMDirectory();
	}
	
	public static IndexSearcher getSearcher() {
		try {
			if(reader == null) {
				reader = IndexReader.open(directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(tr != null) {
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 全匹配搜索
	 * @param field
	 * @param name
	 * @param num
	 */
	public static void searchByTerm(String field, String name, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermQuery(new Term(field, name));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按范围搜索
	 * @param field
	 * @param name
	 * @param start
	 * @param end
	 * @param num
	 */
	public static void searchByRange(String field, String name, String start, String end, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermRangeQuery(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按数据范围搜索
	 * @param field
	 * @param start
	 * @param end
	 * @param num
	 */
	public static void searchByNumricRange(String field, int start, int end, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = NumericRangeQuery.newIntRange(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 前缀搜索
	 * @param field
	 * @param value
	 * @param num
	 */
	public static void searchByPrefix(String field, String value, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new PrefixQuery(new Term(field, value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通配符搜索
	 * @param filed
	 * @param value
	 * @param num
	 */
	public static void searchByWildcard(String field, String value, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			// 在传入的value中可以使用通配符：?和*，?表示匹配一个字符，*表示匹配任意多个字符
			Query query = new WildcardQuery(new Term(field, value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searchByBoolean(String field1, String value1, String field2, String value2, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			BooleanQuery query = new BooleanQuery();
			/**
			 * BooleanQuery可以连接多个子查询
			 * Occur.MUST 表示必须出现
			 * Occur.SHOULD 表示可以出现
			 * Occur.MUSE_NOT 表示不能出现
			 */
			query.add(new TermQuery(new Term(field1, value1)), Occur.MUST);
			query.add(new TermQuery(new Term(field2, value2)), Occur.MUST);

			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searchByPhrase(String field1, String value1, String field2, String value2, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			PhraseQuery query = new PhraseQuery();
			query.add(new Term(field1, value1));
			query.add(new Term(field2, value2));
			query.setSlop(1);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 模糊查询
	 * @param field
	 * @param value
	 * @param num
	 */
	public static void searchByFuzzy(String field, String value, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new FuzzyQuery(new Term(field, value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searchByQueryParse(Query query, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
		HelloSearcher.searchByQueryParse(query, 10);
	}
	
	public static void searchPage(String content, int pageIndex, int pageSize) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse(content);
			TopDocs tds = searcher.search(query, 500);
			ScoreDoc[] sds = tds.scoreDocs;
			
			int start = (pageIndex - 1) * pageSize + pageSize;
			int end = pageIndex * pageSize;
			for(int i = start; i < end; i++ ) {
				Document document = searcher.doc(sds[i].doc);
				System.out.println(document.get("id") + ", " + document.get("name") + ", " + document.get("email"));
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
} 
