package com.jerry.lucene.index;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月11日
 */
public class DataIndexHelper {

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
	private static IndexWriter writer = null;
	private static IndexReader reader = null;
	
	private static final String INDEX_FILE_PATH = System.getProperty("user.dir") + File.separator + "index" + File.separator;

	public static Directory getDirectory() {
		return directory;
	}
	
	public static IndexWriter getWriter() {
		return writer;
	}
	
	public static void index() {
		try {
			setScores();
			setDates();
			directory = FSDirectory.open(new File(INDEX_FILE_PATH));
			reader = IndexReader.open(directory, false);
			
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		
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
				
				writer.addDocument(document);
			}
			
			writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IndexSearcher getSearcher() {
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
	
	public static void print(ScoreDoc scoreDoc, Document document) {
		StringBuffer sb = new StringBuffer();
		sb.append("doc=").append(scoreDoc.doc).append(", ");
		sb.append("boost=").append(document.getBoost()).append(", ");
		sb.append("score=").append(scoreDoc.score).append(", ");
		sb.append("id=").append(document.get("id")).append(", ");
		sb.append("name=").append(document.get("name")).append(", ");
		sb.append("email=").append(document.get("email")).append(", ");
		sb.append("date=").append(sdf.format(new Date(Long.valueOf(document.get("date"))))).append(", ");
		sb.append("attachs=").append(document.get("attachs"));
		System.out.println(sb.toString());
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
}
