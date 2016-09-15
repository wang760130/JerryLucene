package com.jerry.lucene.hello;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月15日
 */
public class HelloIndex {

	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private String[] contents = {
			"welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball",
			"my name is cc I like game",
			"I like football",
			"I like football and I like basketball too",
			"I like movie and swim"
	};
	private Date[] dates = null;
	private int[] attachs = {2,3,1,4,5,5};
	private String[] names = {"zhangsan","lisi","john","jetty","mike","jake"};
	private Directory directory = null;
	private Map<String,Float> scores = new HashMap<String,Float>();
	private static IndexReader reader = null;
	
	public void index() {
		
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		
			Document document = null;
			
			for(int i = 0; i < ids.length; i++) {
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.ANALYZED));
				
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

}
