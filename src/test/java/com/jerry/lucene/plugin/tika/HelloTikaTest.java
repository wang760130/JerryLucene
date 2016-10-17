package com.jerry.lucene.plugin.tika;

import java.io.File;

import org.junit.Test;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月17日
 */
public class HelloTikaTest {
	
	@Test
	public void fileToTxtTest() {
		System.out.println(HelloTika.fileToTxt(new File("F:\\ACM\\如何学习C语言.doc")));
	}

}
