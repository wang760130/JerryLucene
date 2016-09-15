package com.jerry.lucene.hello;

import java.io.File;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月15日
 */
public class HelloIndex {

	private static final String INDEX_FILE_PATH = System.getProperty("user.dir") + File.separator + "index" + File.separator;

	private static final String[] ids = {"1","2","3","4","5","6"};
	private static final String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private static final String[] contents = {
			"welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball",
			"my name is cc I like game",
			"I like football",
			"I like football and I like basketball too",
			"I like movie and swim"
	};
	private static final Date[] dates = null;
	private static final int[] attachs = {2,3,1,4,5,5};
	private static final String[] names = {"zhangsan","lisi","john","jetty","mike","jake"};
	private static Directory directory = null;
	private static final Map<String,Float> scores = new HashMap<String,Float>();
	private static final IndexReader reader = null;
	
	static {
		try {
			directory = FSDirectory.open(new File(INDEX_FILE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void index() {
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		
			indexWriter.deleteAll();
			
			Document document = new Document();
			
			for(int i = 0; i < ids.length; i++) {
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("id","11",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email",emails[0],Field.Store.YES,Field.Index.NOT_ANALYZED));
				document.add(new Field("content",contents[0],Field.Store.NO,Field.Index.ANALYZED));
				document.add(new Field("name",names[0],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
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
	
	public static void query() {
		try {
			IndexReader indexReader = IndexReader.open(directory);
			System.out.println("numDocs:" + indexReader.numDocs());
			System.out.println("maxDocs:" + indexReader.maxDoc());
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		HelloIndex.index();
		HelloIndex.query();
	}
	
}
