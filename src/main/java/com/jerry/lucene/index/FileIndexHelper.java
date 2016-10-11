package com.jerry.lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
public class FileIndexHelper {

	private static Directory directory = null;
	private static IndexWriter writer = null;
	private static IndexReader reader = null;
	
	private static final String DATA_FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;

	private static final String INDEX_FILE_PATH = System.getProperty("user.dir") + File.separator + "index" + File.separator;
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Directory getDirectory() {
		return directory;
	}
	
	public static IndexWriter getWriter() {
		return writer;
	}
	
	public static void index() {
		try {
			directory = FSDirectory.open(new File(INDEX_FILE_PATH));
			reader = IndexReader.open(directory, false);
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			File file = new File(DATA_FILE_PATH);
			Document doc = null;
			Random ran = new Random();
			int index = 0;
			for(File f:file.listFiles()) {
				int score = ran.nextInt(600);
				doc = new Document();
				doc.add(new Field("id",String.valueOf(index++),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("content",new FileReader(f)));
				doc.add(new Field("filename",f.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("path",f.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new NumericField("date",Field.Store.YES,true).setLongValue(f.lastModified()));
				doc.add(new NumericField("size",Field.Store.YES,true).setIntValue((int)(f.length())));
				doc.add(new NumericField("score",Field.Store.NO,true).setIntValue(score));
				writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null) 
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	public static void print(ScoreDoc scoreDoc, Document document) {
		StringBuffer sb = new StringBuffer();
		sb.append("doc=").append(scoreDoc.doc).append(", ");
		sb.append("boost=").append(document.getBoost()).append(", ");
		sb.append("score=").append(scoreDoc.score).append(", ");
		sb.append("id=").append(document.get("id")).append(", ");
		sb.append("content=").append(document.get("content")).append(", ");
		sb.append("filename=").append(document.get("filename")).append(", ");
		sb.append("path=").append(document.get("path")).append(", ");
		sb.append("date=").append(sdf.format(new Date(Long.valueOf(document.get("date"))))).append(", ");
		sb.append("size=").append(document.get("size")).append(", ");
		sb.append("score=").append(document.get("score"));
		System.out.println(sb.toString());
	}
}
