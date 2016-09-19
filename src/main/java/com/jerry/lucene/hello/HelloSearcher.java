package com.jerry.lucene.hello;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
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
} 
