package test;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import search.QueryMatcher;
import indexbase.Indexer;

public class Teste {
	public static void main(String[] args) throws Exception {
		Indexer i = new Indexer(true, true);
		QueryMatcher q = new QueryMatcher(true, true);
		
		i.createBase();
		TopDocs td = q.buildSearch("aggreable");
        
        System.out.println("Total Results :: " + td.totalHits);
         
        for (ScoreDoc sd : td.scoreDocs)
        {
            Document d = q.getSearcher().doc(sd.doc);
            System.out.println("Path : "+ d.get("path") + ", Score : " + sd.score);
        }
	}
}
