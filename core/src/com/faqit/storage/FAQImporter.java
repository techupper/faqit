package com.faqit.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.faqit.similarity.Ranker;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;
import com.faqit.storage.exception.RetrieveEntriesException;
import com.faqit.storage.exception.StoreEntryException;

public class FAQImporter {
	private static final int CHARS_PER_LINE = 100;
	
	public static void importFAQ(Storage storage) throws XMLStreamException,
			FileNotFoundException, StoreEntryException {
		Entry currEntry = null;
		String tagContent = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory
				.createXMLStreamReader(new FileInputStream(
						"corpus/_ENG_faq.xml"));

		while (reader.hasNext()) {
			int event = reader.next();

			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				if ("FAQ".equals(reader.getLocalName())) {
					currEntry = new Entry();
				}
				if ("FAQS".equals(reader.getLocalName())) {
					// empList = new ArrayList<>();
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				tagContent = reader.getText().trim();
				break;

			case XMLStreamConstants.END_ELEMENT:
				switch (reader.getLocalName()) {
				case "FAQ":
					storage.storeEntry(currEntry);
					break;
				case "FAQID":
					currEntry.setId(tagContent);
					break;
				case "DOMAIN":
					currEntry.setDomain(tagContent);
					break;
				case "QUESTION":
					currEntry.setQuestion(tagContent);
					break;
				case "ANSWER":
					currEntry.setAnswer(tagContent);
					break;
				}
				break;

			case XMLStreamConstants.START_DOCUMENT:
				// empList = new ArrayList<>();
				break;
			}

		}
	}
	
	public static void produceL2RInput(Storage storage, String filePath, int numFeatures) throws XMLStreamException, IOException, RetrieveEntriesException, SimilarityMeasureException{
		int idCounter = 1;
		int lineOrderPerQuery = storage.NUMBER_OF_HITS;
		String query = null;
		String tagContent = null;
		StringBuilder sb = null; //line example: 3 qid:1 1:1 2:1 3:0 4:0.2
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory
				.createXMLStreamReader(new FileInputStream(filePath));

		File l2rInput = new File("l2r.in");
		BufferedWriter writer = new BufferedWriter(new FileWriter(l2rInput));
		
		System.out.println("File is being written to " + l2rInput.getCanonicalPath());
		
		while (reader.hasNext()) {
			int event = reader.next();

			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				switch(reader.getLocalName()){
				case "ENGLISH":
					sb = new StringBuilder(CHARS_PER_LINE).append("qid:");
					sb.append(idCounter);
					sb.append(" ");
					break;
				case "SMS":
					lineOrderPerQuery = storage.NUMBER_OF_HITS;
					break;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				tagContent = reader.getText().trim();
				break;
			case XMLStreamConstants.END_ELEMENT:
				switch (reader.getLocalName()) {
				case "SMS":
					writer.flush();
					idCounter++;
					break;
				case "SMS_QUERY_ID":
					break;
				case "SMS_TEXT":
					query = tagContent;
					break;
				case "MATCHES":
					break;
				case "ENGLISH":
					sb.insert(0, lineOrderPerQuery-- + " ");
					sb.append(getFeatures(query, storage.getQuestionByFaqId(tagContent), numFeatures));
					sb.append("\n");
					writer.write(sb.toString());
					break;
				}
				break;
			case XMLStreamConstants.START_DOCUMENT:
				break;
			}
		}
		writer.close();
	}
	
	private static String getFeatures(String st1, String st2, int numFeatures) throws SimilarityMeasureException{
		StringBuilder sb = new StringBuilder(CHARS_PER_LINE);
		
		for(int i=0;i<numFeatures;i++){
			sb.append(i+1);
			sb.append(":");
			sb.append(Ranker.getFeature(st1, st2, i+1));
			sb.append(" ");
		}
		
		return sb.toString();
	}
}
