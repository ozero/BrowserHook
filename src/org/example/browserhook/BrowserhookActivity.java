package org.example.browserhook;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class BrowserhookActivity extends Activity {
	//�u���E�U�̑I���ǂ��������ˁB
	//�\������A�C�e���Q
	private static final String[][] converters = new String[][] {
		{
			"pc2m+Browser",
			"http://rg0020.ddo.jp/p?_k_v=2&_k_c=100&_k_u=",
			"com.android.browser",
			"com.android.browser.BrowserActivity"
		},
		{
			"* direct *",
			"",
			"com.android.browser",
			"com.android.browser.BrowserActivity"
		},
		{
			"GWT+Browser",
			"http://www.google.co.jp/gwt/x?btnGo=Go&source=wax&ie=UTF-8&oe=UTF-8&u=",
			"com.android.browser",
			"com.android.browser.BrowserActivity"
		},
		{
			"bing+Browser",
			"http://d2c.infogin.com/ja-jp/lnk000/=",
			"com.android.browser",
			"com.android.browser.BrowserActivity"
		}

	};
	Uri uri = null;
	String TAG = "BrowserHook";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//start
		super.onCreate(savedInstanceState);
		
		//get implicit intent
		// http://bit.ly/1Fm2qH , http://bit.ly/1ckUv5
		if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			uri = getIntent().getData();
		} else {
			finish();
			return;
		}
		Log.d(TAG, "intent:got");

		//disp main layout
		setContentView(R.layout.main);
		Log.d(TAG, "layout:done");
		
		//ask convert pattern
		selectDialog();
		return;
	}
	
	//�I������\��
	private void selectDialog() {
		ArrayList<String> items_src = new ArrayList<String>();
		Log.d(TAG, "init:dialog");
		
		//�I���_�C�A���O�p�̑I�����ꗗ�𐶐�
		for (String[] tmp : converters){
			items_src.add(tmp[0]);
		}
		Log.d(TAG, "init:dialog:items:done");
		String[] items = (String[])items_src.toArray(new String[0]);
		
		//�I���_�C�A���O��\��
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Choose your browser");
		dialog.setSingleChoiceItems(items, 0,
			new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				openBrowser(which);
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				/* Cancel �{�^�����N���b�N�������̏��� */
				dialog.dismiss();
				finish();
				return;
			}
		});
		dialog.show();
		Log.d(TAG, "init:dialog:show:done");
		return;
	}
	
	//�I����������
	private void openBrowser(int which) {
		String[] item = converters[which];
				
		//modifi intent
		Log.d(TAG, "urlsrc:" + uri);
		uri = Uri.parse(item[1] + uri.toString());
		Log.d(TAG, "urlmod:" + uri);
		
		//launch browser
		ComponentName comp = new ComponentName(item[2],item[3]);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setComponent(comp);
		startActivity(intent);
		finish();
		return;
	}

}