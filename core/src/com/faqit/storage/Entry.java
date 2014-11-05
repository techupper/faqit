package com.faqit.storage;

public class Entry {
	private String id;
	private String domain;
	private String question;
	private String answer;
	
	public Entry() {
		this.id = null;
		this.domain = null;
		this.question = null;
		this.answer = null;
	}
	
	public Entry(String id, String domain, String question, String answer){
		this.id = id;
		this.domain = domain;
		this.question = question;
		this.answer = answer;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String faqId) {
		this.id = faqId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
