package com.jerry.lucene.searcher;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年10月6日
 */
public class CustomParser extends QueryParser{

	public CustomParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
	}

	@Override
	protected org.apache.lucene.search.Query getWildcardQuery(String field,
			String termStr) throws ParseException {
		throw new ParseException("由于性能原因，已经禁用了通配符查询，请输入更加精确的信息");
	}

	@Override
	protected org.apache.lucene.search.Query getFuzzyQuery(String field,
			String termStr, float minSimilarity) throws ParseException {
		throw new ParseException("由于性能原因，已经禁用了模糊查询，请输入更加精确的信息");
	}

	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field,
			String part1, String part2, boolean inclusive) throws ParseException{
		
		if(field.equals("size")) {
			return NumericRangeQuery.newIntRange(field, Integer.parseInt(part1), Integer.parseInt(part2), inclusive, inclusive);
		} else if(field.equals("date")) {
			String dataType = "yyyy-MM-dd";
			Pattern pattern = Pattern.compile("\\d(4)-\\d(2)-\\d(2)");
			if(pattern.matcher(part1).matches() && pattern.matcher(part2).matches() ) {
				SimpleDateFormat sdf = new SimpleDateFormat(dataType);
				try {
					long start = sdf.parse(part1).getTime();
					long end = sdf.parse(part2).getTime();
					return NumericRangeQuery.newLongRange(field, start, end, inclusive, inclusive);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				
			} else {
				throw new ParseException("要检索的日期格式不正确，请使用"+dataType+"这种格式");
			}
		}
		
		return new TermRangeQuery(field, part1, part2, inclusive, inclusive);
	}
}
