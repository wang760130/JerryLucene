package com.jerry.lucene.plugin.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Tika是2008年才产生的一个apache的项目，主要用于打开各种不同的文档
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月17日
 */
public class HelloTika {

	public static String fileToTxt(File file) {
		Parser parser = new AutoDetectParser();
		
		InputStream is = null;
		
		try {
			Metadata metadata = new Metadata();
			is = new FileInputStream(file);
			ContentHandler handler = new BodyContentHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			parser.parse(is, handler, metadata, context);
			
			for(String name : metadata.names()) {
				System.out.println(name + ":" + metadata.get(name));
			}
			
			return handler.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return "error!!";
	}	
	
}
