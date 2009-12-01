package org.example.browserhook;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.example.browserhook.Converter;
import org.example.browserhook.SettingeditorActivity;

public class SettingActivity extends Activity {
	// todo: load fromo tsv
	Uri uri = null;
	String TAG = "bh:sa";
	SharedPreferences sp;
	Converter conv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		// start
		super.onCreate(savedInstanceState);
		conv = new Converter();
		convLoad();

		// disp main layout
		setContentView(R.layout.main);

		// do
		dispSetting();

		return;
	}

	// メニュー定義
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, Menu.NONE, "Export").setShortcut('0', 'k');
		menu.add(0, 1, Menu.NONE, "Import").setShortcut('1', 's');
		menu.add(0, 2, Menu.NONE, "Initialize").setShortcut('2', 'u');
		return true;
	}

	// エディタから戻ってきたとき
	@SuppressWarnings("unused")
	private void onResume(Bundle savedInstanceState) {
		Log.d(TAG, "onResume");
		convSave();
		dispSetting();
	}

	// メニューアイテム選択時のイベント
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
		case 0:
			exportSD();
			return true;
		case 1:
			importSD();
			return true;
		case 2:
			conv.cinit();// init --force
			dispSetting();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// //////////////////////////////////////////////////////////////////////
	
	//sharedprefを読んでconvに設定
	private void convLoad() {
		Log.d(TAG, "convLoad()");
		sp = getSharedPreferences(BrowserhookActivity.FILENAME, MODE_PRIVATE);
		//TODO:fill stub
//		conv.deserialize(sp.getString(BrowserhookActivity.spkey, ""));
		return;
	}
	
	//convを読んでsharedPrefに設定
	private void convSave() {
		Log.d(TAG, "convSave()");
		sp = getSharedPreferences(BrowserhookActivity.FILENAME, MODE_PRIVATE);
		//TODO:fill stub
//		SharedPreferences.Editor editor = sp.edit();
//		String data =  conv.serialize();
//		editor.putString(BrowserhookActivity.spkey, data);
//		editor.commit();
//		Log.d(TAG, "convSave():commit");
		return;
	}
	
	

	// 設定画面を表示
	private void dispSetting() {
		Log.d(TAG, "dispSetting()");
		// get converter
		String[][] converters = conv.getConverters();

		// build adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		int cvlen = converters.length;
		for (int i = 0; i < cvlen; i++) {
			adapter.add(converters[i][0]);
		}

		// attach adapter
		final ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// クリックされたら終了
				setSelectedItem(parent, position);
				finish();
			}
		});
		Log.d(TAG, "dispSetting():done");
		return;
	}

	// リスト項目を選択した際のアクション
	private void setSelectedItem(AdapterView<?> parent, int position) {
		Log.d(TAG, "setSelectedItem():" + position);
		startSettingeditorActivity(position);
		return;
	}

	// 編集画面を開く
	private void startSettingeditorActivity(int idx) {
		Log.d(TAG, "launch setting activity():" + idx);
		Intent i = new Intent(this, SettingeditorActivity.class);
		i.putExtra("index", idx);
		startActivityForResult(i, 0);
		return;
	}

	// TODO:SDへのエクスポート
	public void exportSD() {
	}

	// TODO:SDからのインポート
	public void importSD() {
	}

}
