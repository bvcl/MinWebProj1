package search;

import java.io.IOException;
import java.nio.file.Path;
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

import basesutil.BasesDirectoriesUtil;

public class QueryMatcher {
	
	private boolean stemming;
	private boolean filterstopwords;
	private IndexSearcher searcher;
	
	public QueryMatcher(boolean stemming, boolean filterstopwords) {
		this.stemming = stemming;
		this.filterstopwords = filterstopwords;
	}
	
	public TopDocs buildSearch(String query) throws Exception{
		IndexSearcher s = createSearcher();
		TopDocs fd = searchInContent(query, s);
		
		return fd;
	}
	
	private TopDocs searchInContent(String query, IndexSearcher searcher) throws Exception{
		
		Analyzer analyzer;
		
		if(stemming && filterstopwords) {
			analyzer = new EnglishAnalyzer();
		} else if (stemming) {
			analyzer = new EnglishAnalyzer(new CharArraySet(0, false));
		} else if (filterstopwords) {
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
		Path path;
		
		if(stemming && filterstopwords) {
			path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_STEMMING_FILTERSW);
		} else if (stemming) {
			path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_STEMMING_NOFILTERSW);
		} else if (filterstopwords) {
			path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_NOSTEM_FILTERSW);
		} else {
			path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_NOSTEM_NOFILTERSW);
		}
		
		Directory dir = FSDirectory.open(path);
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	public IndexSearcher getSearcher() {
		return this.searcher;
	}
}
