package com.jerry.lucene.hello;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
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

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月19日
 */
public class HelloSearcher {
	
	private Directory directory;
	private IndexReader reader;
	
	public HelloSearcher() {
		directory = new RAMDirectory();
	}
	
	public IndexSearcher getSearcher() {
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
	public void searchByTerm(String field, String name, int num) {
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
	public void searchByRange(String field, String name, String start, String end, int num) {
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
	public void searchByNumricRange(String field, int start, int end, int num) {
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
	public void searchByPrefix(String field, String value, int num) {
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
	public void searchByWildcard(String field, String value, int num) {
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
	
	
	public void searchByBoolean(String field1, String value1, String field2, String value2, int num) {
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
	
	public void searchByPhrase(String field1, String value1, String field2, String value2, int num) {
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
	public void searchByFuzzy(String field, String value, int num) {
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
} 
