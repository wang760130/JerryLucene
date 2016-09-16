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
		
			Document document = new Document();
			
			for(int i = 0; i < ids.length; i++) {
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				document.add(new Field("content",contents[i],Field.Store.NO,Field.Index.ANALYZED));
				document.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
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
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			indexWriter.deleteAll();
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delete() {
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			// 参数是一个选项，可以是一个Query，也可以是一个term，term是一个精确查找的值
			// 此时删除的文档并不会被完全删除，而是存储在一个回收站中的，可以恢复
			indexWriter.deleteDocuments(new Term("id", "1"));
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void undelete() {
		// 使用IndexReader进行恢复
		try {
			// 恢复时， 必须把IndexReader的只读（readOnly）设置为false
			IndexReader indexReader = IndexReader.open(directory, false);
			indexReader.undeleteAll();
			indexReader.clone();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void forceMergeDeletes() {
		// 使用IndexReader进行恢复
		try {
			// 恢复时， 必须把IndexReader的只读（readOnly）设置为false
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			indexWriter.forceMergeDeletes();
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void forceMerge() {
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			// 会将索引合并为两断，这两段中的被删除的数据会被清空
			// 特别注意：此处Lucene在3.5之后不建议使用，因为会消耗大量的开销，Luceue会根据情况自动处理
			indexWriter.forceMerge(2);
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void update() {
		try {
			IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35)));
			
			// Luceue并没有提供更新，这里的更新操作其实是如下两个操作的集合
			// 先删除之后在添加
			
			Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			document.add(new Field("email",emails[0],Field.Store.YES,Field.Index.NOT_ANALYZED));
			document.add(new Field("content",contents[0],Field.Store.NO,Field.Index.ANALYZED));
			document.add(new Field("name",names[0],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			indexWriter.updateDocument(new Term("id","1"), document);
			
			indexWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void query() {
		try {
			IndexReader indexReader = IndexReader.open(directory);
			// 通过reader可以有效的获取到文档的数量
			System.out.println("numDocs:" + indexReader.numDocs());
			System.out.println("maxDocs:" + indexReader.maxDoc());
			System.out.println("deleteDocs:" + indexReader.numDeletedDocs());
			indexReader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		HelloIndex.deleteAll();
		HelloIndex.index();
		HelloIndex.query();
	}
	
}
