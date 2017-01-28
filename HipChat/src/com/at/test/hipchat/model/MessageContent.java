package com.at.test.hipchat.model;

import java.util.List;

public class MessageContent {
	
	private List<String> mentions;
	private List<String> emoticons;
	private List<Link> links;
	
	public List<String> getMentions() {
		return mentions;
	}
	public void setMentions(List<String> mentions) {
		this.mentions = mentions;
	}
	public List<String> getEmoticons() {
		return emoticons;
	}
	public void setEmoticons(List<String> emoticons) {
		this.emoticons = emoticons;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}

}
