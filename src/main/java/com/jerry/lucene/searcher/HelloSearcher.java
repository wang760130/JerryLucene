package com.jerry.lucene.searcher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月19日
 */
public class HelloSearcher {
	
	private final static String INDEX_FILE_PATH = System.getProperty("user.dir") + File.separator + "index" + File.separator;

	private final static String[] ids = {"1","2","3","4","5","6"};
	private final static String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private final static int[] attachs = {2,3,1,4,5,5};
	private final static String[] names = {"zhangsan","lisi","john","jetty","mike","jake"};
	private final static String[] contents = {
			"welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball",
			"my name is cc I like game",
			"I like football",
			"I like football and I like basketball too",
			"I like movie and swim"
	};
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Date[] dates = null;
	private static Map<String,Float> scores = new HashMap<String,Float>();
	private static Directory directory = null;
	private static IndexReader reader = null;
	
	static {
		try {
			setScores();
			setDates();
			directory = FSDirectory.open(new File(INDEX_FILE_PATH));
			reader = IndexReader.open(directory, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setScores() {
		scores.put("itat.org", 2.0f);
		scores.put("zttc.edu", 1.5f);
	}
	
	private static void setDates()  {
		dates = new Date[ids.length];
		try {
			dates[0] = sdf.parse("2010-02-19");
			dates[1] = sdf.parse("2012-01-11");
			dates[2] = sdf.parse("2011-09-19");
			dates[3] = sdf.parse("2010-12-22");
			dates[4] = sdf.parse("2012-01-01");
			dates[5] = sdf.parse("2011-05-19");
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static IndexSearcher getSearcher() {
		try {
			if(reader == null) {
				reader = IndexReader.open(directory, false);
			} else {
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(tr != null) {
					reader.close();
					reader = tr;
				}
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new IndexSearcher(reader);
	}
	
	private static void print(ScoreDoc scoreDoc, Document document) {
		StringBuffer sb = new StringBuffer();
		sb.append("doc=").append(scoreDoc.doc).append(", ");
		sb.append("boost=").append(document.getBoost()).append(", ");
		sb.append("score=").append(scoreDoc.score).append(", ");
		sb.append("id=").append(document.get("id")).append(", ");
		sb.append("name=").append(document.get("name")).append(", ");
		sb.append("email=").append(document.get("email")).append(", ");
		sb.append("date=").append(sdf.format(new Date(Long.valueOf(document.get("date"))))).append(", ");
		sb.append("attachs=").append(document.get("attachs")).append(", ");
		System.out.println(sb.toString());
	}
	
	public static void index() {
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		
			Document document = null;
			
			for(int i = 0; i < ids.length; i++) {
				document = new Document();
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				document.add(new Field("content",contents[i],Field.Store.NO,Field.Index.ANALYZED));
				document.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				
				// 数字索引
				document.add(new NumericField("attachs", Field.Store.YES, true).setIntValue(attachs[i]));
				
				// 日期索引
				document.add(new NumericField("date", Field.Store.YES, true).setLongValue(dates[i].getTime()));
				
				// 权重
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				if(scores.containsKey(et)) {
					document.setBoost(scores.get(et));
				} else {
					document.setBoost(0.5f);
				}
				
				indexWriter.addDocument(document);
			}
			
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAll() {
		try {
			IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.deleteAll();
			writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 全匹配搜索
	 * @param field
	 * @param name
	 * @param num
	 */
	public static void searcherByTerm(String field, String name, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermQuery(new Term(field, name));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按范围搜索
	 * @param field
	 * @param start
	 * @param end
	 * @param num
	 */
	public static void searcherByTermRange(String field, String start, String end, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new TermRangeQuery(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
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
	public static void searcherByNumricRange(String field, int start, int end, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = NumericRangeQuery.newIntRange(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
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
	public static void searcherByPrefix(String field, String value, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new PrefixQuery(new Term(field, value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
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
	public static void searcherByWildcard(String field, String value, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			// 在传入的value中可以使用通配符：?和*，?表示匹配一个字符，*表示匹配任意多个字符
			Query query = new WildcardQuery(new Term(field, value));
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherByBoolean(String field1, String value1, String field2, String value2, int num) {
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
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherByPhrase(String field, String value1,  String value2,  int slop, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			PhraseQuery query = new PhraseQuery();
			query.add(new Term(field, value1));
			query.add(new Term(field, value2));
			query.setSlop(slop);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
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
	public static void searcherByFuzzy(String field, String value, float minimumSimilarity, int prefixLength, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			Query query = new FuzzyQuery(new Term(field, value), minimumSimilarity, prefixLength);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherByQueryParse(Query query, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了：" + tds.totalHits);
			
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherPage(String content, int pageIndex, int pageSize) {
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
				print(sds[i], document);
			}
			
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherPageByAfter(String field, String content, ScoreDoc after, int pageSize) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse(content);
			TopDocs tds = searcher.searchAfter(after, query, pageSize);
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据页码和分页大小获取上一次的最后一个ScoreDoc
	 * @param pageIndex
	 * @param pageSize
	 * @param query
	 * @param searcher
	 * @return
	 * @throws IOException
	 */
	private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException {
		if(pageIndex == 1) {
			return null;
		}
		int num = pageSize * (pageIndex - 1);
		TopDocs tds = searcher.search(query, num);
		return tds.scoreDocs[num - 1];
	}
	
	public static void searcherPageByAfter(String field, String content, int pageIndex, int pageSize) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse(content);
			// 先获取上一页的最后一个元素
			ScoreDoc lastSocreDoc = getLastScoreDoc(pageIndex, pageSize, query, searcher);
			// 通过最后一个元素搜索下页的pageSize个元素
			TopDocs tds = searcher.searchAfter(lastSocreDoc, query, pageSize);
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherByFilter(String field, String content, Filter filter, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse(content);
			TopDocs tds = null;
			if(filter!=null)
				tds = searcher.search(query, filter, num);
			else {
				tds = searcher.search(query, num);
			}
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			searcher.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherBySort(String field, String content, Sort sort, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse(content);
			TopDocs tds = null;
			if(sort!=null)
				tds = searcher.search(query, num, sort);
			else {
				tds = searcher.search(query, num);
			}
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			searcher.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void searcherByQuery(Query query, int num) {
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs tds = null;
			tds = searcher.search(query, num);
			for(ScoreDoc scoreDoc : tds.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				print(scoreDoc, document);
			}
			searcher.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
} 
