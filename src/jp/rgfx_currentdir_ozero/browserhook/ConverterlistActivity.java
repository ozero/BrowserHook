package jp.rgfx_currentdir_ozero.browserhook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ConverterlistActivity extends ListActivity {
	static Uri URI = null;
	static Boolean IS_STANDALONE = true;
	static String TAG = "bhcl";
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int INSERT_ID = R.id.menu_insert;
	private static final int EXPORT_ID = R.id.menu_export;
	private static final int IMPORT_ID = R.id.menu_import;
	private static final int HISTORY_ID = R.id.menu_history;
	private static final int INITIALIZE_ID = R.id.menu_initialize;
	private static final int DELETE_ID = 0;
	private Converter mDbHelper;
	
	// //////////////////////////////////////////////////////////////////////
	// GUI event dispatcher: activity
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// start
		super.onCreate(savedInstanceState);
		setContentView(R.layout.converterlist);
		mDbHelper = new Converter(this);
		mDbHelper.open();
		setTitle(R.string.apptitle_main_standalone);// タイトルを設定
		
		//bind widget with method
//		wdgDirectBtn = (Button) findViewById(R.id.ButtonDirect);
//		wdgDirectBtn.setOnClickListener(this);
        
		//build gui
		buildListView();
		registerForContextMenu(getListView());
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

	//右クリックメニューの設定
	public void onCreateContextMenu(
			ContextMenu menu, View v,
	        ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		return;
	}
	
	// //////////////////////////////////////////////////////////////////////
	// GUI event dispatcher : widget
	
	// リストアイテムがクリックされた際の処理
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//選択した変換アイテムの編集画面を開く
		Log.d(TAG, "onListItemClick():" + id);
		editItem(id);
		return;
	}
		
	// ボタンクリックのディスパッチ
	public void onClick(View v) {
		return;
	}

	// メニューがクリックされた際
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createItem();
			return true;
		case IMPORT_ID:
			//alertdialog("sorry", "not impremented yet");
			importItem();
			return true;
		case EXPORT_ID:
			exportItem();
			return true;
		case INITIALIZE_ID:
			initializeItem();
			return true;
		case HISTORY_ID:
			Log.d(TAG, "menu:history");
			startHistoryActivity();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	//右クリック時イベントの設定
	public boolean onContextItemSelected(MenuItem item) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	//builder.setTitle("タイトル");
    	
	    switch(item.getItemId()) {
	    case DELETE_ID:
	        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        mDbHelper.deleteItem(info.id);
	        buildListView();
	        return true;
	    }
	    
    	builder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
	        public void onClick(android.content.DialogInterface dialog,int whichButton) {
	            setResult(RESULT_OK);
	        }
	    });
    	builder.create();
    	builder.show();

	    
	    return super.onContextItemSelected(item);
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
	private void createItem() {
		Log.d(TAG, "ssa:createItem");
		Intent i = new Intent(this, SettingActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
		Log.d(TAG, "ssa:launch setting activity.");
		return;
	}

	// 履歴画面を開く
	private void startHistoryActivity() {
		Intent i = new Intent(this,HistoryActivity.class);
		startActivity(i);
		return;
	}

	
	// //////////////////////////////////////////////////////////////////////
	// misc logic
	
	// リストビューに項目を流し込む
	public void buildListView() {
		//
		Cursor itemCursor = mDbHelper.fetchAllItems();
		startManagingCursor(itemCursor);

		// 表示する列
		String[] from = new String[] { Converter.KEY_TITLE,Converter.KEY_URL };

		// 表示する列に関連付けるwidget
		int[] to = new int[] { R.id.text0, R.id.text1 };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.converterlistitem, itemCursor, from, to);
		setListAdapter(notes);
		
		return;
	}

	
	// 項目を初期化
	private void initializeItem() {
		// confirmダイアログを出す
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.alert_init_title);
		builder.setMessage(R.string.alert_init_msg);
		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						// OKなら
						setResult(RESULT_OK);
						mDbHelper.initdb();
						//build gui
						buildListView();
					
					}
				});
		builder.setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						// キャンセルなら
						setResult(RESULT_CANCELED);
						return;
					}
				});
		builder.create();
		builder.show();
		
		
	}


//	// メッセージを出す
//	private void alertdialog(String title, String msg) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle(title);
//		builder.setMessage(msg);
//		builder.setPositiveButton("OK",
//				new android.content.DialogInterface.OnClickListener() {
//					public void onClick(android.content.DialogInterface dialog,
//							int whichButton) {
//						setResult(RESULT_OK);
//					}
//				});
//		builder.create();
//		builder.show();
//	}
	
	
	//変換アイテムのエクスポート
	private void exportItem() {
		Log.d(TAG, "exportItem");
		String outstr = "";
		
		//全部取得してテキスト化
		Cursor c = mDbHelper.fetchAllItems();
		startManagingCursor(c);
		c.moveToFirst();
		for(int i = 0;i < c.getCount();i++){
			outstr += c.getString(1) + "\t"
			+ c.getString(2) + "\t"
			+ c.getString(3) + "\n";
			c.moveToNext();
		}
		Log.d(TAG, "ei:got conv items:"+c.getCount());

		//write
		try {
			String strPathSD = Environment.getExternalStorageDirectory().toString();
			File pathSD = new File(strPathSD);
		    if (!pathSD.canWrite()){
				Log.d(TAG, "ei:NO SDCARD:");
		    }
			
		    //mkdir
		    File pathSDdata = new File(
		    		strPathSD + "/data/"
			 );
		    if (!pathSDdata.canWrite()){
		    	pathSDdata.mkdir();
				Log.d(TAG, "ei:mkdir:"+pathSDdata.toString());
		    }
		    //
		    File pathSDdatapkg = new File(
		    		strPathSD + "/data/" + this.getPackageName() 
			);
		    if (!pathSDdatapkg.canWrite()){
		    	pathSDdatapkg.mkdir();
				Log.d(TAG, "ei:mkdir:"+pathSDdatapkg.toString());
		    }
		    //
		    String strPathDst = 
		    	strPathSD	+ "/data/" + this.getPackageName() + "/converters.txt";
		    File pathDst = new File(strPathDst);
	        FileWriter fw = new FileWriter(pathDst);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(outstr);
	        bw.close();
	        
	        //完了メッセージ
	        Toast.makeText(
	        		this, "Exported into : " + strPathDst, Toast.LENGTH_LONG
	        	).show();  
		    
		} catch (IOException e) {
		    Log.e(TAG, "Could not write file " + e.getMessage());
	        //エラーメッセージ
	        Toast.makeText(
	        		this, "No available SDcard found...", Toast.LENGTH_SHORT
	        	).show();  
		    return;
		}

		

		return;
	}
	
	
	// TODO:インポート処理
	private void importItem() {
		Log.d(TAG, "importItem:");
		ArrayList<String[]> getstr=new ArrayList<String[]>();
		
		String strPathSD = Environment.getExternalStorageDirectory().toString();
	    String strPathDst = 
	    	strPathSD	+ "/data/" + this.getPackageName() + "/converters.txt";
	    File pathDst = new File(strPathDst);
	    
	    boolean isAnyValidLine=false;
        try {
			FileReader fr = new FileReader(pathDst);
			BufferedReader br = new BufferedReader(fr,1024); 
			Log.d(TAG, "iI:br");
			
			//read
			try {
				String line;
				line = br.readLine();
			    while ( line != null) {
					Log.d(TAG, "iI:line");
			    	String[] tmp = line.split("\t");
			    	if(tmp.length == 3){
						//check
				    	getstr.add(tmp);
				    	isAnyValidLine = true;
						Log.d(TAG, "iI:line:ok");
			    	}
			    	line = br.readLine();
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!isAnyValidLine){
				Log.d(TAG, "iI:noline");
		        //エラーメッセージ
		        Toast.makeText(
		        		this, "No valid TSV.", Toast.LENGTH_LONG
		        	).show();  
				return;
			}
			
			//truncate
			mDbHelper.deleteAllItem();
			Log.d(TAG, "iI:truncate");
			
			//insert
			for(int i=0;i<getstr.size();i++){
				Log.d(TAG, "iI:insert:" + i + getstr.get(i)[0]
			    +getstr.get(i)[1]
                +getstr.get(i)[2]);
				mDbHelper.createItem(
						getstr.get(i)[0], 
						getstr.get(i)[1], 
						Integer.parseInt(getstr.get(i)[2]));
			}
			
			//rebuild gui
			buildListView();
	        Toast.makeText(
	        		this, "Setting was restored.", Toast.LENGTH_LONG
	        	).show();  


			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
	        //エラーメッセージ
	        Toast.makeText(
	        		this, "Do Export First. File Not Found.", Toast.LENGTH_LONG
	        	).show();  
			e.printStackTrace();
		}
	    
		return;
	}
	

}
