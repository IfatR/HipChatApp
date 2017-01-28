package com.at.test.hipchat;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.at.test.hipchat.model.Link;
import com.at.test.hipchat.model.MessageContent;
import com.at.test.hipchat.utils.MessageAnalyzer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends Activity implements ITitleTask{

	private static final String HTTP = "http";
	private Gson gson;
	private HashMap<String, String> urlsTitlesMap;
	private TextView textMessageView;
	private ProgressBar spinner;
	private Context context;
	private MessageContent chatContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		urlsTitlesMap = new HashMap<String, String>(); 
		gson = new GsonBuilder().setPrettyPrinting().create();
		spinner = (ProgressBar)findViewById(R.id.progressBar);
		spinner.setVisibility(View.GONE);
	}

	public void sendMessage(View view) {
		EditText chatBox = (EditText)findViewById(R.id.chatBox);
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(chatBox.getWindowToken(), 0);
		spinner.setVisibility(View.VISIBLE);

		String chatMessage = chatBox.getText().toString();

		if(chatMessage != null && !chatMessage.isEmpty()){
			textMessageView = (TextView)findViewById(R.id.textMessage);
			textMessageView.setText("");
			urlsTitlesMap.clear();
			analyzeChatMessage(chatMessage);
		} else {
			spinner.setVisibility(View.GONE);
			Toast.makeText(this, getResources().getString(R.string.error_no_message), Toast.LENGTH_LONG).show();
		}
	}

	private void analyzeChatMessage(String chatMessage) {

		chatContent = MessageAnalyzer.analyzeChatMessage(chatMessage);
		List<Link> linksList = chatContent.getLinks();
		if(linksList != null && !linksList.isEmpty()){

			for (Link link : linksList) {
				TitleTask task = new TitleTask(this);
				String formattedUrl = link.getUrl();
				if(!formattedUrl.toLowerCase(Locale.US).startsWith(HTTP)){
					formattedUrl = HTTP + "://" + formattedUrl; // In case the URL does not contain protocol add 'http' to the URL
				} 
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, formattedUrl, link.getUrl());
			}
		} else {
			updateTextView(chatContent);
		}
	}

	private void updateTextView(final MessageContent chatContent) {
		String jsonResult = gson.toJson(chatContent, MessageContent.class);

		textMessageView.setMovementMethod(new ScrollingMovementMethod());
		textMessageView.setText(jsonResult);
		spinner.setVisibility(View.GONE);
	}
	
	@Override
	public void onTitleReceived(String title, String currentUrl) {
		if(title == null){
			urlsTitlesMap.put(currentUrl, getResources().getString(R.string.error_cannot_get_title));
		} else {
			urlsTitlesMap.put(currentUrl, title);
		}
		if (chatContent.getLinks().size() == urlsTitlesMap.size()){
			for (Link link : chatContent.getLinks()) {
				link.setTitle(urlsTitlesMap.get(link.getUrl()));
			}
			chatContent.setLinks(chatContent.getLinks());
			updateTextView(chatContent);
		}
	}
	
}
