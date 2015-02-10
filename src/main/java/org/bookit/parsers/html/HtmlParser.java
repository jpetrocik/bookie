package org.bookit.parsers.html;

import java.util.Map;

import org.bookit.parsers.Parser;

public interface HtmlParser extends Parser {
	
	public void process(String url) throws Exception;
	
	public String getTitle();
	
	public String getSummary();
	
	public String getText();
	
	public String getSource();
	
	public Map<String, String> getMetadata();
	

}
