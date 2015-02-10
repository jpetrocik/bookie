package org.bookit.html.parsers;

import org.bookit.parsers.html.JsoupParser;
import org.junit.Before;
import org.junit.Test;

public class JsoupParserTest {

	JsoupParser jsoupParser;
	
	@Before
	public void setup(){
		jsoupParser = new JsoupParser();
	}
	
	@Test
	public void testParser() throws Exception {
		jsoupParser.process("http://jsoup.org/cookbook/extracting-data/attributes-text-html");
		
		System.out.println(jsoupParser.getTitle());
		System.out.println(jsoupParser.getText());
	}

}
