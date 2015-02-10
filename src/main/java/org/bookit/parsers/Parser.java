package org.bookit.parsers;

import org.apache.lucene.document.Document;

public interface Parser {

	public Document generateDocument();
}
