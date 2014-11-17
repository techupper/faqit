package com.faqit.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.faqit.storage.exception.StoreEntryException;

public class FAQImporter {
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
}
