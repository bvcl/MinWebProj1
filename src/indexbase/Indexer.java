package indexbase;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import basesutil.*;

public class Indexer {

		private boolean stemming;
		private boolean filterstopwords;
		private IndexWriter writer;
		
		public Indexer(boolean stemming, boolean filterstopwords) {
			this.stemming = stemming;
			this.filterstopwords = filterstopwords;
		}
		
		public void createBase(){
			Analyzer analyzer;
			Path path;
			try {
				
				if(stemming && filterstopwords) {
					analyzer = new EnglishAnalyzer();
					path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_STEMMING_FILTERSW);
				} else if (stemming) {
					analyzer = new EnglishAnalyzer(new CharArraySet(0, false));
					path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_STEMMING_NOFILTERSW);
				} else if (filterstopwords) {
					analyzer = new StandardAnalyzer();
					path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_NOSTEM_FILTERSW);
				} else {
					analyzer = new StandardAnalyzer(new CharArraySet(0, false));
					path = Paths.get(BasesDirectoriesUtil.INDEXED_BASE_NOSTEM_NOFILTERSW);
				}

				
				Directory outputDir = FSDirectory.open(path);
				IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
				this.writer = new IndexWriter(outputDir, iwc);
				index(this.writer, Paths.get(BasesDirectoriesUtil.DOCS_DIRECTORY));
				this.writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		
		public void index(final IndexWriter writer, Path path) throws IOException{
			if(Files.isDirectory(path)){
				Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
					public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
						try{
							indexDoc(writer, file, attr.lastModifiedTime().toMillis());
						} catch(IOException e){
							e.printStackTrace();
						}
						return FileVisitResult.CONTINUE;
					}
				});
			} else {
				indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
			}
		}
		
		public void indexDoc(IndexWriter writer, Path file, long time) throws IOException{
			try(InputStream stream = Files.newInputStream(file)){
				Document doc = new Document();
				
				doc.add(new StringField("path", file.toString(), Field.Store.YES));
				doc.add(new LongPoint("modified", time));
				doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));
				
				
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
		
}
