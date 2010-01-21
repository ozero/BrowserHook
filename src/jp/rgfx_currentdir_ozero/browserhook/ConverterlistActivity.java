package jp.rgfx_currentdir_ozero.browserhook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ConverterlistActivity extends ListActivity {
	static Uri URI = null;
	static Boolean IS_STANDALONE = true;
	static String TAG = "bhcl";
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int INSERT_ID = R.id.menu_insert;
	private static final int EXPORT_ID = R.id.menu_export;
	private static final int IMPORT_ID = R.id.menu_import;
	private static final int INITIALIZE_ID = R.id.menu_initialize;

	//gui
	private Converter mDbHelper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// start
		super.onCreate(savedInstanceState);
		setContentView(R.layout.converterlist);
		mDbHelper = new Converter(this);
		mDbHelper.open();
		
		//bind
//		wdgDirectBtn = (Button) findViewById(R.id.ButtonDirect);
//		wdgDirectBtn.setOnClickListener(this);
		
		//build gui
		
		return;
	}

	// 編集画面から戻ってきた際に実行される。
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
	}


	// //////////////////////////////////////////////////////////////////////
	// GUI event dispatcher
		
	// ボタンクリックのディスパッチ
	public void onClick(View v) {
		return;
	}

	// メニューを作成
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// メニューインフレーターを取得
		MenuInflater inflater = getMenuInflater();
		// xmlのリソースファイルを使用してメニューにアイテムを追加
		inflater.inflate(R.menu.converterlist, menu);
		// できたらtrueを返す
		return true;
	}

	// メニューがクリックされた際
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			return true;
		case IMPORT_ID:
			return true;
		case EXPORT_ID:
			return true;
		case INITIALIZE_ID:
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	// //////////////////////////////////////////////////////////////////////
	// GUI traisition

	// 設定画面を開く：項目を編集する
	private void editItem(long id) {
		Log.d(TAG, "ssa:openSetting:" + id);
		Intent i = new Intent(this, SettingActivity.class);
		// クリックされた行のIDをintentに埋める。これで項目ID取れるのなー
		i.putExtra(Converter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
		Log.d(TAG, "ssa:launch setting activity.");
		return;
	}

	// 設定画面を開く：項目を編集する
	private void createItem(long id) {
		Log.d(TAG, "ssa:openSetting:" + id);
		Intent i = new Intent(this, SettingActivity.class);
		// クリックされた行のIDをintentに埋める。これで項目ID取れるのなー
		i.putExtra(Converter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_CREATE);
		Log.d(TAG, "ssa:launch setting activity.");
		return;
	}

	// //////////////////////////////////////////////////////////////////////
	// misc logic
		
	
	// TODO:エクスポート処理
	// TODO:インポート処理
	

}
