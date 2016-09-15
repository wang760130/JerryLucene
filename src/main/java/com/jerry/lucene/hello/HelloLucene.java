package com.jerry.lucene.hello;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月11日
 */
public class HelloLucene {

	public static final String DATA_FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;

	public static final String INDEX_FILE_PATH = System.getProperty("user.dir") + File.separator + "index" + File.separator;
	
	/**
	 * 建立索引
	 */
	public static void index() {
		try {
			// 1、创建Directory
			// 建立在内存中
//			Directory directory = new RAMDirectory();
			// 建立在本地文件中
			Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
			
			// 2、创建IndexWriter
			StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, standardAnalyzer);
			IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
			
			// 3、创建Document对象
			Document document = null;
			
			// 4、为Doucment添加Field
			File filePath = new File(DATA_FILE_PATH);
			for(File file : filePath.listFiles()) {
				document = new Document();
				document.add(new Field("content", new FileReader(file)));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				
				// 5、通过IndexWriter添加文档到索引中
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
	
	/**
	 * 搜索
	 */
	public static void searcher() {
		try {
			// 1、创建Directory
			Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
		
			// 2、创建IndexReader
			IndexReader indexReader = IndexReader.open(directory);
			
			// 3、根据IndexReader创建IndexSearcher
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			
			// 4、创建搜索的Query
			// 创建parser来确定要搜索文件的内容
			QueryParser queryPaeser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			// 创建query，表示搜索域为content中包含java 的文档
			Query query = queryPaeser.parse("java");
			
			// 5、根据seacher搜索并且返回TopDocs
			TopDocs topDocs = indexSearcher.search(query, 10);
			
			// 6、根据TopDocs获取ScoreDoc对象
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc scoreDoc : scoreDocs) {
				// 7、根据seacher和ScordDoc对象获取具体的Document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
			
				// 8、根据Document对象获取需要的值
				System.out.println("filename = " + document.get("filename") + ", path = " + document.get("path"));
			}
			
			indexReader.close();
			indexSearcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		HelloLucene.index();
		HelloLucene.searcher();
	}
}
