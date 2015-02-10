package org.bookit.index;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.bookit.parsers.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Indexer implements Closeable {

	@Autowired
	Directory directory;

	TrackingIndexWriter writer;

	SearcherManager searchManger;

	TopScoreDocCollector collector;

	StandardAnalyzer analyzer;

	ControlledRealTimeReopenThread<IndexSearcher> realTimeThread;

	@PostConstruct
	public void init() throws IOException{
		analyzer = new StandardAnalyzer();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter _internalWriter = new IndexWriter(directory, config);
		_internalWriter.commit();

		writer = new TrackingIndexWriter(_internalWriter);


		searchManger = new SearcherManager(_internalWriter,
				true,
				null);

		realTimeThread = new ControlledRealTimeReopenThread<IndexSearcher>(writer,
				searchManger,
				60.00,   // when there is nobody waiting
				0.1);    // when there is someone waiting
		
		realTimeThread.start(); 
		
		
		collector = TopScoreDocCollector.create(10, true);

	}

	public void index(Parser parser) throws Exception{
		Document doc = parser.generateDocument();
		writer.addDocument(doc);
	}

	public List<Map<String, String>> search(String q) throws Exception{
		Query query = new QueryParser(Version.LUCENE_4_10_3, "title", analyzer).parse(q);

		IndexSearcher searcher = searchManger.acquire();
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		List<Map<String, String>> results = new ArrayList<>();
		for(int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
			Map<String, String> r = new HashMap<>();
			Document d = searcher.doc(docId);
			List<IndexableField> allFields = d.getFields();
			for (IndexableField field : allFields){
				r.put(field.name(), field.stringValue());
			}
			results.add(r);
		}
		return results;
	}

	@Override
	public void close() throws IOException {
		realTimeThread.close();
		writer.getIndexWriter().close();
		
		
	}


}
