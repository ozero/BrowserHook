package jp.rgfx_currentdir_ozero.browserhook;

import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoryActivity extends ListActivity {
	static Uri URI = null;
	static String clickeduri = null;
	static Boolean IS_STANDALONE = true;
	static String TAG = "his";
	private History mDbHelper;
	
	// //////////////////////////////////////////////////////////////////////
	// GUI event dispatcher: activity
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// start
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historylist);
		mDbHelper = new History(this);
		mDbHelper.open();
		setTitle(R.string.apptitle_history);// タイトルを設定
		buildListView();
		return;
	}

	// 編集画面から戻ってきた際に実行される。
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
	}


	// //////////////////////////////////////////////////////////////////////
	// GUI defs
	

	// //////////////////////////////////////////////////////////////////////
	// GUI event dispatcher : widget
	
	// リストアイテムがクリックされた際の処理
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//選択した変換アイテムの編集画面を開く
		Log.d(TAG, "onListItemClick():" + id);
		openItem(id);
		return;
	}
		
	
	// //////////////////////////////////////////////////////////////////////
	// GUI traisition

	// 項目を使う
	private void openItem(long id) {
		Log.d(TAG, "ssa:openSetting:" + id);
		
		//get clicked content
		Cursor c = mDbHelper.fetchItem(id);
		startManagingCursor(c);
		c.moveToFirst();
		HistoryActivity.clickeduri = c.getString(1);
		
		//build choice dialog
		Builder b = new Builder(this);
		String[] bitem = {getString(R.string.menu_browser),
				getString(R.string.menu_share)};
		b.setSingleChoiceItems(bitem, 0,
            new DialogInterface.OnClickListener(){
	        	public void onClick(DialogInterface dialog, int which) {
	        		Intent i = new Intent();
	        		if(which==0){
	        			//browser
	        			i.setAction(Intent.ACTION_VIEW);
	        			i.setData(Uri.parse(clickeduri));
	        			finish();
	        			
	        		}else{
	        			//share
	        			i.setAction(Intent.ACTION_SEND);
	        			i.setType("text/plain");
	        			i.putExtra(Intent.EXTRA_TEXT, clickeduri);
	        			finish();
	        		}
	        		startActivity(i);
	        	}
	        }		
		);
		b.setNegativeButton(R.string.ButtonCancel, 
            new DialogInterface.OnClickListener(){
	        	public void onClick(DialogInterface dialog, int which) {
	        	}
	        }				
		);
		b.show();
		
		
		//
		Log.d(TAG, "ssa:launch setting activity.");
		return;
	}

	
	// //////////////////////////////////////////////////////////////////////
	// misc logic
	
	// リストビューに項目を流し込む
	public void buildListView() {
		//
		Cursor itemCursor = mDbHelper.fetchAllItems();
		startManagingCursor(itemCursor);
		Log.d(TAG, "bld:" + itemCursor.getCount());

		// 表示する列
		String[] from = new String[] { History.KEY_URL, History.KEY_TIMESTAMP };

		// 表示する列に関連付けるwidget
		int[] to = new int[] { R.id.text0, R.id.text1 };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.historylistitem, itemCursor, from, to);
		setListAdapter(notes);
		
		return;
	}

	

}