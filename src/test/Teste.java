package test;


import indexbase.Indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import search.QueryMatcher;

public class Teste {
	public static void main(String[] args) throws Exception {
		Indexer i = new Indexer(true, false);
		QueryMatcher q = new QueryMatcher(true, false);
		
		i.createBase();
		TopDocs td = q.buildSearch("do");
        
        System.out.println("Total Results :: " + td.totalHits+"\n");
         
        IndexSearcher searcher = q.getSearcher();
        
        for (ScoreDoc sd : td.scoreDocs)
        {
            Document d = searcher.doc(sd.doc);
            System.out.println("Path : "+  d.get("path") + ", Score : " + sd.score);
        }
	}
}
