package com.jerry.lucene.index;

import org.junit.Test;

import com.jerry.lucene.index.HelloIndex;

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
	public void writerDeleteTest() {
		HelloIndex.writerDelete();
	}
	
	@Test
	public void readerDeleteTest() {
		HelloIndex.readerDelete();
	}
	
	@Test
	public void undeleteTest() {
		HelloIndex.undelete();
	}

	@Test
	public void forceMergeDeletesTest() {
		HelloIndex.forceMergeDeletes();
	}
	
	@Test
	public void forceMergeTest() {
		HelloIndex.forceMerge();
	}
	
	@Test
	public void updateTest() {
		HelloIndex.update();
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
