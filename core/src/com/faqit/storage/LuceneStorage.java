package com.faqit.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LuceneStorage implements Storage {
	private static final int NUMBER_OF_HITS = 10;
	private static final String ID_FIELD = "id";
	private static final String DOMAIN_FIELD = "domain";
	private static final String QUESTION_FIELD = "question";
	private static final String ANSWER_FIELD = "answer";

	private Analyzer analyzer = null;
	private Directory directory = null;
	private IndexWriter indexWriter = null;
	private IndexWriterConfig config = null;
	private DirectoryReader directoryReader = null;
	private IndexSearcher indexSearcher = null;

	public LuceneStorage(String pathToStorage) throws IOException {

		analyzer = new StandardAnalyzer();

		if (pathToStorage != null) {
			// TODO validate path
			File file = new File(pathToStorage);
			directory = FSDirectory.open(file);
		} else {
			directory = new RAMDirectory();
		}

		config = new IndexWriterConfig(Version.LATEST, analyzer);
		indexWriter = new IndexWriter(directory, config);
	}

	public void storeEntry(Entry entry) throws StoreEntryException {

		try {

			Document doc = new Document();
			doc.add(new Field("id", entry.getId(), TextField.TYPE_STORED));
			doc.add(new Field("domain", entry.getDomain(),
					TextField.TYPE_STORED));
			doc.add(new Field("question", entry.getQuestion(),
					TextField.TYPE_STORED));
			doc.add(new Field("answer", entry.getAnswer(),
					TextField.TYPE_STORED));

			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (IOException e) {
			throw new StoreEntryException("Failed to store the entry with id="
					+ entry.getId() + ".");
		}
	}

	@SuppressWarnings("static-access")
	public List<Entry> RetrieveTopEntries(String userInput)
			throws RetrieveEntriesException {
		List<Entry> topEntries = new ArrayList<Entry>();

		try {
			Query query = new QueryParser(QUESTION_FIELD, analyzer)
					.parse(userInput);

			IndexReader indexReader = directoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);

			TopScoreDocCollector collector = TopScoreDocCollector.create(
					NUMBER_OF_HITS, true);

			indexSearcher.search(query, collector);

			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			//System.out.println("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; i++) {
				
				int docId = hits[i].doc;
				Document document = indexSearcher.doc(docId);
				
				//System.out.println((i + 1) + ". " + document.get(QUESTION_FIELD));
				
				topEntries.add(new Entry(document.get(ID_FIELD), document
						.get(DOMAIN_FIELD), document.get(QUESTION_FIELD),
						document.get(ANSWER_FIELD)));
			}

		} catch (ParseException e) {
			throw new RetrieveEntriesException("Parse Exception occurred.");
		} catch (IOException e) {
			throw new RetrieveEntriesException("Could not access the index.");
		}

		return topEntries;
	}
}
