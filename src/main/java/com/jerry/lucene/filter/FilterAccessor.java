package com.jerry.lucene.filter;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月16日
 */
public interface FilterAccessor {

	public String[] values();
	
	public String getField();
	
	public boolean set();
}
