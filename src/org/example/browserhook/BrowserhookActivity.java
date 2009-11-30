package org.example.browserhook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import org.example.browserhook.Converter;

/*
 * どないしょ。
 * 泥コードでいいかなこの規模だったら。
 * ほんとだったらConverter定義をクラスに切り出すんだけどな。
 * エンティティなんだし。
 * 
 * */

public class BrowserhookActivity extends Activity {
	Uri uri = null;
	String TAG = "bh:bha";
	Converter conv = new Converter();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// start
		super.onCreate(savedInstanceState);
		
		//check for intent
		if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			//if exists, get implicit one
			// http://bit.ly/1Fm2qH , http://bit.ly/1ckUv5
			uri = getIntent().getData();
			Log.d(TAG, "intent:got");
			// ask convert pattern
			dispSelectDialog();
			
		}else{
			// or none, display setting
			//setContentView(R.layout.main);
			
			//display setting
			startSettingActivity();
			finish();
		}
		
		return;
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////
	
	// 選択肢を表示
	private void dispSelectDialog() {
		Log.d(TAG, "init:dialog");
		String[] items = conv.getConvertersName();

		// 選択ダイアログを表示
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Choose your browser");
		dialog.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						openBrowser(which);
					}
				});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* Cancel ボタンをクリックした時の処理 */
						dialog.dismiss();
						Log.d(TAG, "cancelled");
						finish();
						return;
					}
				});
		dialog.show();
		Log.d(TAG, "init:dialog:show:done");
		return;
	}

	// 選択されたアイテムを処理
	private void openBrowser(int which) {
		String[] item = conv.getConverter(which);
		
		// modify intent
		Log.d(TAG, "urlsrc:" + uri);
		uri = Uri.parse(item[1] + uri.toString());
		Log.d(TAG, "urlmod:" + uri);

		// launch browser
		ComponentName comp = new ComponentName(item[2], item[3]);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setComponent(comp);
		startActivity(intent);
		finish();
		Log.d(TAG, "successfully launch browser.");
		return;
	}
	
	
	/////////////////////////////////////////////////////////////////
	
	//設定画面を開く
	private void startSettingActivity() {
		Intent i = new Intent(this, SettingActivity.class);
		startActivityForResult(i, 0);
		Log.d(TAG, "launch setting activity.");
		return;
	}
	
	
	
}