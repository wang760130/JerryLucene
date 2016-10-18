package com.jerry.lucene.plugin.tika;

import java.io.File;

import org.junit.Test;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月17日
 */
public class HelloTikaTest {
	
	private final String file = System.getProperty("user.dir") + File.separator + "data" + File.separator + "如何学习C语言.doc";
	
	@Test
	public void fileToTxtTest() {
		System.out.println(HelloTika.fileToTxt(new File(file)));
	}
	
	@Test
	public void tikaToolTest() {
		System.out.println(HelloTika.tikaTool(new File(file)));
	}
}
