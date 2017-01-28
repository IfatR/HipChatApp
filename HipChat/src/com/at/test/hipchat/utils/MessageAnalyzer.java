package com.at.test.hipchat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Patterns;

import com.at.test.hipchat.model.Link;
import com.at.test.hipchat.model.MessageContent;

public class MessageAnalyzer {
	
	public static MessageContent analyzeChatMessage(String chatMessage) {

		MessageContent chatContent = new MessageContent();

		List<Link> linksList = null;
		Matcher urlMatcher = Patterns.WEB_URL.matcher(chatMessage);

		String chatMessageNoUrl = chatMessage;
		while (urlMatcher.find()) {
			int matchStart = urlMatcher.start(1);
			int matchEnd = urlMatcher.end();
			if(linksList == null){
				linksList = new ArrayList<Link>();
			}
			String url = chatMessage.substring(matchStart, matchEnd);
			linksList.add(new Link(url));			
			// Remove the URL from the message to prevent conflict with other searched items (e.g. www.goog@le.com) 
			chatMessageNoUrl = chatMessageNoUrl.replace(url, "");
		}
		
		chatContent.setLinks(linksList);
		//Search for alphanumeric characters in brackets. Up to 15 characters
		List<String> emoticonsList = extractContent(chatMessageNoUrl, "\\((\\p{Alnum}{1,15})\\)"); 
		chatContent.setEmoticons(emoticonsList);
		
		//Search for @ following alphanumeric characters
		List<String> mentionsList = extractContent(chatMessageNoUrl, "@(\\p{Alnum}+)");
		chatContent.setMentions(mentionsList);
		
		return chatContent;
	}
	
	private static List<String> extractContent(String chatMessage, String regexString) {
		List<String> itemsList = null;
		Pattern pattern = Pattern.compile(regexString); 
		Matcher regexMatcher = pattern.matcher(chatMessage);
		while (regexMatcher.find()) {
			if(!regexMatcher.group(1).isEmpty()){
				if(itemsList == null){
					itemsList = new ArrayList<String>();
				}
				itemsList.add(regexMatcher.group(1));
			}
		}
		return itemsList;
	}

}
