package org.example.browserhook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingeditorActivity extends Activity {

	private static final String TAG = "bh:sea";
	SharedPreferences sp;
	Converter conv = new Converter();

	private Button ButtonOK;
	private Button ButtonCancel;
	private int idx;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingActivity:onCreate");
		// start
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(BrowserhookActivity.FILENAME, MODE_PRIVATE);
		conv = new Converter();
		convLoad();

		// get intent
		Bundle extras = getIntent().getExtras();
		idx = extras.getInt("index");
		String[] item = conv.getConverter(idx);

		// disp main layout
		setContentView(R.layout.settingeditor);
		if (extras != null) {
			setEdittext(item);
		}

		// bind
		ButtonOK = (Button) findViewById(R.id.ButtonOK);
		ButtonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			//save
			public void onClick(View v) {
				String[] data = getEdittext();
				conv.setConverter(data, idx);
				convSave();
				startSettingActivity();
				finish();
			}
		});
		ButtonCancel = (Button) findViewById(R.id.ButtonCancel);
		ButtonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			//cancel
			public void onClick(View v) {
				startSettingActivity();
				finish();
			}
		});

		return;
	}
	
	
	//sharedprefを読んでconvに設定
	private void convLoad() {
		//TODO:fill stub
		return;
	}
	
	//convを読んでsharedPrefに設定
	private void convSave() {
		//TODO:fill stub
		return;
	}
	
	
	
	
	//GUIに入力されたデータを取得
	public String[] getEdittext() {
		String[] item = { "", "", "", "" };
		EditText editNAME = (EditText) findViewById(R.id.EditTextNAME);
		item[0] = editNAME.getText().toString();
		EditText editURL = (EditText) findViewById(R.id.EditTextURL);
		item[1] = editURL.getText().toString();
		EditText editAPP = (EditText) findViewById(R.id.EditTextAPP);
		item[2] = editAPP.getText().toString();
		EditText editACTV = (EditText) findViewById(R.id.EditTextACTV);
		item[3] = editACTV.getText().toString();
		return item;
	}
	
	//データをGUIに設定する
	public void setEdittext(String[] item) {
		EditText editNAME = (EditText) findViewById(R.id.EditTextNAME);
		editNAME.setText((CharSequence) item[0]);
		EditText editURL = (EditText) findViewById(R.id.EditTextURL);
		editURL.setText((CharSequence) item[1]);
		EditText editAPP = (EditText) findViewById(R.id.EditTextAPP);
		editAPP.setText((CharSequence) item[2]);
		EditText editACTV = (EditText) findViewById(R.id.EditTextACTV);
		editACTV.setText((CharSequence) item[3]);
	}
	
	// 設定画面を開く
	private void startSettingActivity() {
		Intent i = new Intent(this, SettingActivity.class);
		startActivityForResult(i, 0);
		Log.d(TAG, "launch setting activity.");
		return;
	}
	
	

}
