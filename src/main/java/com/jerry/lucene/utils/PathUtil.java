package com.jerry.lucene.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public class PathUtil {
	
	/**
	 * 获取当前jar包所在目录 / 程序bin所在目录
	 */
	public static String getCurrentPath() {
		String path = System.getProperty("serviceframe.config.path");

		if (path == null || path.equalsIgnoreCase("")) {
			Class<?> caller = getCaller();
			if (caller == null) {
				caller = PathUtil.class;
			}
			path = getCurrentPath(caller);
		}
	    try {
			path = URLDecoder.decode(path,"utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return path;
	}


	private static Class<?> getCaller() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		if(stack.length < 3) {
			return PathUtil.class;
		}
		String className = stack[2].getClassName();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前class父目录
	 * @param cls
	 * @return 当前class父目录 URL
	 */
	public static String getCurrentPath(Class<?> cls) {
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replaceFirst("file:/", "");
		path = path.replaceAll("!/", "");
		if(path.lastIndexOf(File.separator) >= 0){
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		if(path.substring(0,1).equalsIgnoreCase("/")){
			String osName = System.getProperty("os.name").toLowerCase();
			if(osName.indexOf("window") >= 0){
				path = path.substring(1);
			}
		}
		return path;
	}
}