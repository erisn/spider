package com.acuity.rdso.sbe_user.model;

import java.time.LocalDateTime;

public class MovieModel {
	private String sourceUrl;
	private String body;
	private String searchFieldValue;  ///imdb
	private String documentName;
	private String source;
	private String creationDate;
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String wikiUrl) {
		this.sourceUrl = wikiUrl;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreationDt() {
		return creationDate;
	}
	public void setCreationDt(String creationDt) {
		this.creationDate = creationDt;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSearchFieldValue() {
		return searchFieldValue;
	}
	public void setSearchFieldValue(String searchFieldValue) {
		this.searchFieldValue = searchFieldValue;
	}
	

}
