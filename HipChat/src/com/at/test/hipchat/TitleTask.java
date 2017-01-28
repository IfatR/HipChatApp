package com.at.test.hipchat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;
import android.util.Log;

public class TitleTask extends AsyncTask<String, Void, String> {

	private static final String TAG = TitleTask.class.getSimpleName();
	private ITitleTask titleListener;
	private String currentUrl; 
	
	public TitleTask(ITitleTask titleListener) {
		this.titleListener = titleListener;
	}

	@Override
	protected String doInBackground(String... url) {
		currentUrl = url[1]; 
		String formattedUrl = url[0];
		String title = null;
		try {
			Document document = Jsoup.connect(formattedUrl).get();
			if(document != null){
				title = document.title();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} 

		return title;
	}
	@Override
	protected void onPostExecute(String title) {
		titleListener.onTitleReceived(title, currentUrl);
	}
}
