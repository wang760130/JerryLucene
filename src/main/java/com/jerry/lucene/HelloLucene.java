package com.jerry.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月11日
 */
public class HelloLucene {

	/**
	 * 建立索引
	 */
	public void index() {
		IndexWriter indexWriter = null;
		try {
			// 1、创建Directory
			// 建立在内存中
			Directory directory = new RAMDirectory();
			// 建立在本地文件中
//			Directory directory = FSDirectory.open(new File(""));
			
			// 2、创建IndexWriter
			StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_35);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, standardAnalyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			
			// 3、创建Document对象
			Document document = null;
			
			// 4、为Doucment添加Field
			File filePath = new File("");
			for(File file : filePath.listFiles()) {
				document = new Document();
				document.add(new Field("content", new FileReader(file)));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				
			}
			
			// 5、通过IndexWriter添加文档到索引中
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
