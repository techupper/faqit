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
import org.apache.lucene.index.Term;
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

import com.faqit.storage.exception.RetrieveEntriesException;
import com.faqit.storage.exception.StoreEntryException;
import com.faqit.storage.exception.SumTotalTermFreqException;
import com.faqit.storage.exception.TotalTermFreqException;

public class LuceneStorage implements Storage {
	private static final String ID_FIELD = "id";
	private static final String DOMAIN_FIELD = "domain";
	private static final String QUESTION_FIELD = "question";
	private static final String ANSWER_FIELD = "answer";

	private Analyzer analyzer = null;
	private Directory directory = null;
	private IndexWriter indexWriter = null;
	private IndexWriterConfig config = null;
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
			throw new StoreEntryException(entry.getId());
		}
	}

	public List<Entry> RetrieveTopEntries(String userInput)
			throws RetrieveEntriesException {
		List<Entry> topEntries = new ArrayList<Entry>();

		try {
			Query query = new QueryParser(QUESTION_FIELD, analyzer)
					.parse(userInput);

			IndexReader indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);

			TopScoreDocCollector collector = TopScoreDocCollector.create(
					NUMBER_OF_HITS, true);

			indexSearcher.search(query, collector);

			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			for (int i = 0; i < hits.length; i++) {

				int docId = hits[i].doc;
				Document document = indexSearcher.doc(docId);

				if (document.get(QUESTION_FIELD).compareTo("") != 0
						&& document.get(ANSWER_FIELD).compareTo("") != 0) {
					topEntries.add(new Entry(document.get(ID_FIELD), document
							.get(DOMAIN_FIELD), document.get(QUESTION_FIELD),
							document.get(ANSWER_FIELD), hits[i].score));
				}

			}

		} catch (ParseException | IOException e) {
			throw new RetrieveEntriesException(e.getMessage());
		}
		
		return topEntries;
	}
	
	public String getQuestionByFaqId(String faqid) throws RetrieveEntriesException{
		String result = null;
		try {
			IndexReader indexReader;
			Query query = new QueryParser(ID_FIELD, analyzer).parse(faqid);
			indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(NUMBER_OF_HITS, true);
			indexSearcher.search(query, collector);
			result = indexSearcher.doc(collector.topDocs().scoreDocs[0].doc).get(QUESTION_FIELD);
		} catch (IOException | ParseException e) {
			throw new RetrieveEntriesException(e.getMessage());
		}
		return result;
	}

	@Override
	public Long getSumTotalTermFreq() throws SumTotalTermFreqException {
		try {
			IndexReader indexReader = DirectoryReader.open(directory);
			return indexReader.getSumTotalTermFreq(ANSWER_FIELD)
					+ indexReader.getSumTotalTermFreq(QUESTION_FIELD);
		} catch (IOException e) {
			throw new SumTotalTermFreqException();
		}
	}

	@Override
	public Long getTotalTermFreq(String termText) throws TotalTermFreqException {
		try {
			IndexReader indexReader = DirectoryReader.open(directory);
			return indexReader.totalTermFreq(new Term(ANSWER_FIELD, termText))
					+ indexReader.totalTermFreq(new Term(QUESTION_FIELD, termText));
		} catch (IOException e) {
			throw new TotalTermFreqException();
		}
	}
}
