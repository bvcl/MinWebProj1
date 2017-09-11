package search;

import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.text.ChangedCharSetException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
	
	private boolean stemming;
	private boolean stopwords;
	
	public QueryMatcher(boolean stemming, boolean stopwords) {
		this.stemming = stemming;
		this.stopwords = stopwords;
	}
	
	public TopDocs buildSearch(String query) throws Exception{
		IndexSearcher s = createSearcher();
		TopDocs fd = searchInContent(query, s);
		
		return fd;
	}
	
	private TopDocs searchInContent(String query, IndexSearcher searcher) throws Exception{
		
		Analyzer analyzer;
		
		if(stemming && stopwords) {
			analyzer = new EnglishAnalyzer();
		} else if (stemming) {
			analyzer = new EnglishAnalyzer(new CharArraySet(0, false));
		} else if (stopwords) {
			analyzer = new StandardAnalyzer();
		} else {
			analyzer = new StandardAnalyzer(new CharArraySet(0, false));
		}
		
		QueryParser qp = new QueryParser("contents", analyzer);
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
