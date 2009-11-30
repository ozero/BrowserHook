package org.example.browserhook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;
import android.app.Activity;

import org.json.JSONStringer;

/*
 * 変換候補の保持、およびload/save/import/export/init担当
 * 
 * */

public class Converter extends Activity {
	String TAG = "BrowserHook";
	JSONStringer json = null;
	public static final String FILENAME = "preference";
	private SharedPreferences sp;
	private String prefkey = "key";
	private static String[][] converters;
	private String[][] converterssrc = {
			{
					"GWT+Browser",
					"http://www.google.co.jp/gwt/x?btnGo=Go&source=wax&ie=UTF-8&oe=UTF-8&u=",
					"com.android.browser",
					"com.android.browser.BrowserActivity" },
			{ "* direct *", "", "com.android.browser",
					"com.android.browser.BrowserActivity" },
			{ "bing+Browser", "http://d2c.infogin.com/ja-jp/lnk000/=",
					"com.android.browser",
					"com.android.browser.BrowserActivity" },
			{ "pc2m+Dolphin", "http://rg0020.ddo.jp/p?_k_v=2&_k_c=100&_k_u=",
					"com.mgeek.android.DolphinBrowser",
					"com.mgeek.android.DolphinBrowser.BrowserActivity" },
			{ "pc2m+Browser", "http://rg0020.ddo.jp/p?_k_v=2&_k_c=100&_k_u=",
					"com.android.browser",
					"com.android.browser.BrowserActivity" }, };

	// constructor
	public Converter() {
		// データファイルがなければ初期化する
		if (cchk() > 0) {
			cinit(0);
		}
		cload();
		return;
	}

	// 変換選択肢を全部返す
	public String[][] getConverters() {
		return converters;
	}

	// 変換選択肢を全部設定する
	public void setConverters(String[][] data) {
		converters = data;
		csave();
		return;
	}

	// 変換選択肢を１つ設定する
	public void setConverter(String[] data, int idx) {
		converters[idx] = data;
		csave();
		return;
	}

	// 変換選択肢の名前一覧を返す
	public String[] getConvertersName() {
		ArrayList<String> items_src = new ArrayList<String>();
		Log.d(TAG, "getConvertersName");

		// 選択ダイアログ用の選択肢一覧を生成
		for (String[] tmp : converters) {
			items_src.add(tmp[0]);
		}
		String[] items = (String[]) items_src.toArray(new String[0]);
		return items;
	}

	// 変換選択肢を返す
	public String[] getConverter(int which) {
		Log.d(TAG, "getConverter");
		String[] item = converters[which];
		return item;
	}

	// TODO: 内蔵ストレージファイルの有無確認
	public int cchk() {
		Log.d(TAG, "cchk");
		int retval = 0;
		return retval;
	}

	// 内蔵ストレージファイルの初期化
	public void cinit(int force) {
		Log.d(TAG, "cinit");

		// 設定内容があれば初期化しない
		if (cload() == 0) {
			return;
		}else{
			// 初期化--forceがなければ初期化しない
			if (force == 0) {
				return;
			}
		}
		
		// 初期化処理
		converters = converterssrc;
		csave();
		return;
	}

	// TODO: 内蔵ストレージから読み出し
	private int cload() {
		Log.d(TAG, "cload");
		converters = null;
		Object obj;
		String[] objpath;
		String expr = "_";
		int count = 0;
		//スキャン用にキャスト //TODO: ぬるぽ。
		sp = getSharedPreferences(FILENAME, MODE_PRIVATE);
		Map<String, ?> hashmap = sp.getAll();
		Iterator<String> it = hashmap.keySet().iterator();
		//スキャン
		while (it.hasNext()) { // 次の要素があるならブロック内を実行
			obj = it.next(); // 次の要素名を取り出す
			// System.out.println("\t" + obj + ": " + hashmap.get(obj));
			objpath = obj.toString().split(expr);
			converters[Integer.parseInt(objpath[1])][Integer
					.parseInt(objpath[2])] = hashmap.get(obj).toString();
			Log.d(TAG, "cload[" + Integer.parseInt(objpath[1]) + "]["
					+ Integer.parseInt(objpath[2]) + "]:"
					+ hashmap.get(obj).toString());
			count++;
		}

		return count;
	}

	// TODO: 内蔵ストレージに保存
	private void csave() {
		Log.d(TAG, "csave");
		//ハンドラを得る
		SharedPreferences sp = getSharedPreferences(FILENAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		//全要素書き込み
		for (int i = 0; i < converters.length; i++) {
			for (int j = 0; j < converters[i].length; j++) {
				editor.putString(prefkey + "_" + i + "_" + j, converters[i][j]);
				Log.d(TAG, "csave[" + i + "][" + j + "]:" + converters[i][j]);
			}
		}
		editor.commit();
		return;
	}

	// TODO: sdcardからimport
	public void cloadSD() {
		Log.d(TAG, "cloadSD");
	}

	// TODO: sdcardに保存
	public void csaveSD() {
		Log.d(TAG, "csaveSD");
	}

}
