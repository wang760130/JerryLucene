package com.jerry.lucene.plugin.highlighter;

import org.junit.Test;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月19日
 */
public class HelloHighlighterTest {

	@Test
	public void lighter() {
		String txt = "我爱北京天安门，天安门上彩旗飞,伟大领袖毛主席，指引我们向前进，向前进！！！想起身离开东京法律思考的机会那个上的讲话那伟大的个圣诞那是肯定激发了深刻的机会拉萨宽带计费了那个傻大姐华纳公司的机会节贺卡就是对话框那是国天安门际  北京电话卡开始觉啊 北京得人们大会堂  北京！！！！";
		String parse = "北京 伟大";
		HelloHighlighter.highlighter(txt, parse);
	}
}
