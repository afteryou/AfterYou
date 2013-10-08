package com.app.afteryou.customview;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.app.afteryou.utils.WindowUtils;

public class SingleSearchView extends SearchView {
	private View mVoiceButton;
	private static final String TAG = "SingleSearchView";
	private static boolean isAllowCustomEvents = false;

	public SingleSearchView(Context context) {
		super(context);
		init();
	}

	public SingleSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		int maxWidthOffset = WindowUtils.dip2px(getContext(), 44);
		setMaxWidth(WindowUtils.getScreenWidth(getContext()) - maxWidthOffset);
		setOnSuggestionListener(new OnSuggestionListener() {
			
			@Override
			public boolean onSuggestionSelect(int position) {
				doSearch(position);
				return true;
			}
			
			@Override
			public boolean onSuggestionClick(int position) {
				doSearch(position);
				return true;
			}
			
			private void doSearch(int position){
			    Cursor cur = getSuggestionsAdapter().getCursor();
			    cur.moveToPosition(position);
			    String suggestion = cur.getString(cur.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
			    setQuery(suggestion, true);
			}
		});
		
		if(!isAllowCustomEvents){
			return;
		}
		int id = this.getContext().getResources().getIdentifier("android:id/search_voice_btn", null, null);
		setFocusable(false);
		setFocusableInTouchMode(false);
		mVoiceButton = findViewById(id);
		mVoiceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(getContext());
				sr.setRecognitionListener(new MyRecognitionListener());
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
				sr.startListening(intent);
			}
		});
		// this.setImeOptions(EditorInfo.IME_ACTION_SEARCH|EditorInfo.);
		// hackToShowVoiceButton();
	}

	protected void hackToShowVoiceButton() {
		try {
			Field field = SearchView.class.getDeclaredField("mVoiceButtonEnabled");
			if (field != null) {
				field.setAccessible(true);
				field.setBoolean(this, true);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}

	class MyRecognitionListener implements RecognitionListener {
		public void onReadyForSpeech(Bundle params) {
			Log.d(TAG, "onReadyForSpeech");
		}

		public void onBeginningOfSpeech() {
			Log.d(TAG, "onBeginningOfSpeech");
		}

		public void onRmsChanged(float rmsdB) {
			Log.d(TAG, "onRmsChanged");
		}

		public void onBufferReceived(byte[] buffer) {
			Log.d(TAG, "onBufferReceived");
		}

		public void onEndOfSpeech() {
			Log.d(TAG, "onEndofSpeech");
		}

		public void onError(int error) {
			Log.d(TAG, "error " + error);
		}

		public void onResults(Bundle results) {
			String str = new String();
			Log.d(TAG, "onResults " + results);
			ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			for (int i = 0; i < data.size(); i++) {
				Log.d(TAG, "result " + data.get(i));
				str += data.get(i);
			}
		}

		public void onPartialResults(Bundle partialResults) {
			Log.d(TAG, "onPartialResults");
		}

		public void onEvent(int eventType, Bundle params) {
			Log.d(TAG, "onEvent " + eventType);
		}
	}

	@Override
	public void onActionViewExpanded() {
		super.onActionViewExpanded();
		hackToShowVoiceButton();
	}

}
