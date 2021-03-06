

package jp.rgfx_currentdir_ozero.browserhook;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


//編集画面アクティビティ
public class SettingActivity extends Activity {

	private EditText mTitleText;
    private EditText mUrlText;
    private EditText mOrderText;
    private Long mRowId;
    private Converter mDbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new Converter(this);
        mDbHelper.open();
        setContentView(R.layout.settingeditor);
        
        //タイトルの設定
		setTitle(R.string.apptitle_edit);
        
        //ウィジェットへのインスタンス
        mTitleText = (EditText) findViewById(R.id.EditTextTITLE);
        mUrlText = (EditText) findViewById(R.id.EditTextURL);
        mOrderText = (EditText) findViewById(R.id.EditTextORDER);
        Button confirmButton = (Button) findViewById(R.id.ButtonOK);
        
        //バックグラウンドから帰ってきた際に行IDを知る
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(Converter.KEY_ROWID) 
                							: null;
		Log.d("hoge","aa");
        //インテントを受け取って編集する行IDを知る
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(Converter.KEY_ROWID) 
					: null;
			if (mRowId == null) {
				Log.d("hoge","null");
                saveState();
			}
		}
		//ウィジェットに対象データを表示する
		populateFields();
		//イベントの設定
        confirmButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                saveState();
        	    setResult(RESULT_OK);
        	    finish();
        	}
        });
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Converter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
	//アクティビティが破棄される際に。
	protected void onDestroy(int requestCode, int resultCode,
			Intent intent) {
		super.onDestroy();
		mDbHelper.close();
	}
    
    //////////////////////////////////////////////////////////////////
    
    //widgetをデータで埋める
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchItem(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
    	            note.getColumnIndexOrThrow(Converter.KEY_TITLE)));
            mUrlText.setText(note.getString(
                    note.getColumnIndexOrThrow(Converter.KEY_URL)));
            mOrderText.setText(note.getString(
                    note.getColumnIndexOrThrow(Converter.KEY_ORDER)));
        }
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mUrlText.getText().toString();
        String order = mOrderText.getText().toString();

        if (mRowId == null) {
        	Log.d("save","insert");
            long id = mDbHelper.createItem("new item", "", 10);
            if (id > 0) {
                mRowId = id;
            	Log.d("save","insert:ok");
            }
        } else {
            mDbHelper.updateItem(mRowId, title, body, Integer.parseInt(order));
        }
    }
    
    
}
