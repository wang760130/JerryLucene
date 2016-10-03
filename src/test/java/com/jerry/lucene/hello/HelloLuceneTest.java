package com.jerry.lucene.hello;

import org.junit.Test;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月3日
 */
public class HelloLuceneTest {

	@Test
	public void indexTest() {
		HelloLucene.index();
	}
	
	@Test
	public void searcherTest() {
		HelloLucene.searcher();
	}
	
	@Test
	public void deleteAllTest() {
		HelloLucene.deleteAll();
	}
	
}
