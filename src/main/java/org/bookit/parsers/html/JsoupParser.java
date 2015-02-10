package org.bookit.parsers.html;

import java.util.Map;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupParser implements HtmlParser {

	String title;
	String text;
	String source;
	
	@Override
	public void process(String url) throws Exception {
		//TODO Look into HttpClient replacement
		Document doc = Jsoup.connect(url).get();
		
		source = url;
		title = processTitle(doc);
		processSummary(doc);
		processMetadata(doc);
		text = processText(doc);
	}

	private String processTitle(Document doc) {
		Elements titles = doc.select("meta [property$=og:title");
		if (titles != null && titles.size() > 0){
			title = titles.first().attr("content");
			return title;
		}
		
		titles = doc.select("title");
		if (titles != null){
			title = titles.get(0).ownText();
			return title;
		}

		return null;
	}

	private Map<String, String> processMetadata(Document doc) {
		return null;
	}

	private void processSummary(Document doc) {
	}

	private String processText(Document doc) {
		return doc.body().text();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getSummary() {
		return null;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public Map<String, String> getMetadata() {
		return null;
	}

	@Override
	public org.apache.lucene.document.Document generateDocument() {
		  org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
		  doc.add(new TextField("title", title, Field.Store.YES));
		  doc.add(new TextField("text", text, Field.Store.NO));
		  doc.add(new TextField("source", source, Field.Store.YES));
		  doc.add(new TextField("type", "HTML", Field.Store.YES));
		  return doc;
	}

	@Override
	public String getSource() {
		return source;
	}

}
