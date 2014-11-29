package com.faqit.storage;

public class Entry implements Comparable<Entry>{
	private String id;
	private String domain;
	private String question;
	private String answer;
	
	private Float score;
	private Float baseScore; //based on if idf
	
	public Entry() {
		this.id = null;
		this.domain = null;
		this.question = null;
		this.answer = null;
		this.score = 0f;
		this.baseScore = 0f;
	}
	
	public Entry(String id, String domain, String question, String answer, Float baseScore){
		this.id = id;
		this.domain = domain;
		this.question = question;
		this.answer = answer;
		this.score = 0f;
		this.baseScore = baseScore;
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

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
	
	public Float getBaseScore() {
		return baseScore;
	}

	@Override
	public int compareTo(Entry compareEntry) {
		Float result = (Float)(compareEntry.getScore() - getScore());
		
		if(result < 0f){
			result = (float) Math.floor(result);
		}else{
			result = (float) Math.ceil(result);
		}
		
		return result.intValue();
	}
}
