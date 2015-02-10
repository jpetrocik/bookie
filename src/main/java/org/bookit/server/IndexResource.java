package org.bookit.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import jersey.repackaged.com.google.common.collect.ImmutableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bookit.index.Indexer;
import org.bookit.parsers.html.JsoupParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/indexer/")
public class IndexResource {
	private static Log log = LogFactory.getLog(IndexResource.class);
	
	@Autowired
	Indexer indexer;
	
	@POST
	@Path("index")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> index(@FormParam("url") String url, @FormParam("media") String media) throws Exception {

		JsoupParser jsoupParser = new JsoupParser();
		jsoupParser.process(url);
		
		indexer.index(jsoupParser);

		return new HashMap<String,String>(ImmutableMap.<String, String>builder().put("status", "successful").build());
	}
	
	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, String>> search(@QueryParam("q") String q) throws Exception {

		return indexer.search(q);
	}

}