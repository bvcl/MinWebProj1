package search;

import java.io.IOException;
import java.nio.file.Paths;
 
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class QueryMatcher {
	
	public TopDocs buildSearch(String query) throws Exception{
		IndexSearcher s = createSearcher();
		TopDocs fd = searchInContent(query, s);
		
		return fd;
	}
	
	private TopDocs searchInContent(String query, IndexSearcher searcher) throws Exception{
		QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
		Query q = qp.parse(query);
		
		TopDocs hits = searcher.search(q, 10);
		return hits;
	}
	
	private IndexSearcher createSearcher() throws IOException{
		Directory dir = FSDirectory.open(Paths.get("indexedFiles"));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
