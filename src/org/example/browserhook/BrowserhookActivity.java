package org.example.browserhook;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import org.example.browserhook.Converter;

public class BrowserhookActivity extends ListActivity {
	static Uri uri = null;
	static Boolean standalone = true;
	static String TAG = "bh";
	static String spkey = "convkey";
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	private static final int INSERT_ID = R.id.menu_insert;
	private static final int EXPORT_ID = R.id.menu_export;
	private static final int IMPORT_ID = R.id.menu_import;
	private static final int INITIALIZE_ID = R.id.menu_initialize;
	private static final int EDIT_ID = ContextMenu.FIRST;
	private static final int DELETE_ID = ContextMenu.FIRST + 1;
	private Converter mDbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// start
		super.onCreate(savedInstanceState);
		mDbHelper = new Converter(this);
		mDbHelper.open();
		registerForContextMenu(getListView());//右クリックメニューを登録
        //インテントが渡されたか単体起動かを判別
		if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
			setTitle(R.string.apptitle_main);//タイトルを設定
			standalone = false;
			uri = getIntent().getData();
			Log.d(TAG, "intent:got");
		}else{
			setTitle(R.string.apptitle_main_standalone);//タイトルを設定
			standalone = true;
			Log.d(TAG, "intent:none");
		}
		// ask convert pattern
		dispSelectDialog();

		return;
	}

	// //////////////////////////////////////////////////////////////////////

	// 編集画面から戻ってきた際に実行される。
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		dispSelectDialog();
	}

	// アイテムがクリックされた際の処理
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick():" + id);
		super.onListItemClick(l, v, position, id);
		if (standalone) {
			// 直接起動だった場合はアイテムの編集を
			startSettingActivity(id);
		} else {
			// intentつき起動だった場合はurlの加工を
			startBrowserApp(id);
		}
		return;
	}

	// メニューを作成
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
    	//メニューインフレーターを取得
    	MenuInflater inflater = getMenuInflater();
    	//xmlのリソースファイルを使用してメニューにアイテムを追加
    	inflater.inflate(R.menu.main, menu);
    	//できたらtrueを返す
		return true;
	}

	// メニューがクリックされた際
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createItem();
			return true;
		case IMPORT_ID:
			alertdialog("sorry","not impremented yet.");
			return true;
		case EXPORT_ID:
			alertdialog("sorry","not impremented yet.");
			return true;
		case INITIALIZE_ID:
			initializeItem();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

    //右クリックメニューが呼ばれた際
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

    //右クリックメニューの項目がクリックされた際
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
    	case EDIT_ID:
    		startSettingActivity(info.id);
	        return true;
    	case DELETE_ID:
	        mDbHelper.deleteItem(info.id);
	        dispSelectDialog();
	        return true;
		}
		return super.onContextItemSelected(item);
	}	
	
	
	// //////////////////////////////////////////////////////////////////////

	// このactivityに据えてるListViewWidgetにノート一覧を流し込む
	private void dispSelectDialog() {
		Log.d(TAG, "init:dialog");
		
		//
		Cursor itemCursor = mDbHelper.fetchAllItems();
		startManagingCursor(itemCursor);

		// 表示するノート名一覧
		String[] from = new String[] { Converter.KEY_ORDER, Converter.KEY_TITLE };

		// 表示するノート名に関連付けるwidgetのリスト
		int[] to = new int[] { R.id.text0 ,R.id.text1 };

		// TODO: figure out this
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.item, itemCursor, from, to);
		setListAdapter(notes);

		Log.d(TAG, "init:dialog:show:done");
		return;
	}

	// URLを加工してブラウザを起動する。
	private void startBrowserApp(long id) {
		Log.d(TAG, "openBrowser:" + id);
		Cursor itemCursor = mDbHelper.fetchItem(id);
		startManagingCursor(itemCursor);

		uri = Uri.parse(itemCursor.getString(itemCursor
				.getColumnIndexOrThrow(Converter.KEY_URL))
				+ uri.toString());
		Log.d(TAG, "urlmod:" + uri);

		ComponentName comp = new ComponentName(itemCursor.getString(itemCursor
				.getColumnIndexOrThrow(Converter.KEY_APP)), itemCursor
				.getString(itemCursor
						.getColumnIndexOrThrow(Converter.KEY_ACTIVITY)));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setComponent(comp);

		startActivity(intent);
		finish();
		Log.d(TAG, "successfully launch browser.");
		return;
	}

	// 設定画面を開く
	private void startSettingActivity(long id) {
		Log.d(TAG, "openSetting:" + id);
		Intent i = new Intent(this, SettingActivity.class);
		// クリックされた行のIDをintentに埋める。これで項目ID取れるのなー
		i.putExtra(Converter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
		Log.d(TAG, "launch setting activity.");
		return;
	}

	
    //項目を新規作成
    private void createItem() {
        Intent i = new Intent(this, SettingActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    //項目を初期化
    private void initializeItem() {
    	mDbHelper.initdb();
    	dispSelectDialog();
    }
    
    //メッセージを出す
    private void alertdialog(String title,String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(title);
    	builder.setMessage(msg);
    	builder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
	        public void onClick(android.content.DialogInterface dialog,int whichButton) {
	            setResult(RESULT_OK);
	        }
	    });
    	builder.create();
    	builder.show();
    }
    
    
	
}