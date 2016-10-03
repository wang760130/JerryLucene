package com.jerry.lucene.hello;

import org.junit.Test;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月3日
 */
public class HelloIndexTest {
	
	@Test
	public void indexTest() {
		HelloIndex.index();
	}
	
	@Test
	public void deleteAllTest() {
		HelloIndex.deleteAll();
	}
	
	@Test
	public void queryTest() {
		HelloIndex.query();
	}
	
	@Test
	public void searchTest() {
		HelloIndex.search();
	}
	
}
