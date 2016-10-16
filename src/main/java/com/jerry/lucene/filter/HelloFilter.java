package com.jerry.lucene.filter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月16日
 */
public class HelloFilter extends Filter {

	private static final long serialVersionUID = 1L;
	
	private FilterAccessor accessor;
	
	public HelloFilter(FilterAccessor accessor) {
		this.accessor = accessor;
	}
	
	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		// 创建一个bit，默认所有的元素都应该是0
		OpenBitSet obs = new OpenBitSet(reader.maxDoc());
		if(accessor.set()) {
			set(reader, obs);
		} else {
			clear(reader, obs);
		}
		return obs;
	}
	
	private void set(IndexReader reader, OpenBitSet obs) throws IOException {
		int[] docs = new int[1];
		int[] freqs = new int[1];
		// 获取id所在的doc的位置，并且将其设置为0
		for (String value : accessor.values()) {
			// 获取TermDocs
			TermDocs tds = reader.termDocs(new Term(accessor.getField(), value));
			// 会 将查询出来的对象的位置存储到docs中，出现的频率存储到freqs中，返回获取的条数
			int count = tds.read(docs, freqs);
			if(count == 1) {
				obs.set(docs[0]);
			}
		}
	}
	
	private void clear(IndexReader reader, OpenBitSet obs) throws IOException {
		// 先把元素填满
		obs.set(0,reader.maxDoc() - 1);
		int[] docs = new int[1];
		int[] freqs = new int[1];
		// 获取id所在的doc的位置，并且将其设置为0
		for (String value : accessor.values()) {
			// 获取TermDocs
			TermDocs tds = reader.termDocs(new Term(accessor.getField(), value));
			// 会 将查询出来的对象的位置存储到docs中，出现的频率存储到freqs中，返回获取的条数
			int count = tds.read(docs, freqs);
			if(count == 1) {
				// 将这个位置的元素删除
				obs.clear(docs[0]);
			}
		}
	}
}
 