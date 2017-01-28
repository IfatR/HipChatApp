package com.at.test.hipchat.model;

public class Link {
	
	private String url;
	private String title;

	public Link(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}
	public Link(String url) {
		super();
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
