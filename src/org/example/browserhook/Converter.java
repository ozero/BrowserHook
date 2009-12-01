package org.example.browserhook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import android.app.Activity;

//import net.arnx.jsonic.JSON;

/*
 * 変換候補の保持、およびシリアライザ・デシリアライザ担当。
 * disk i/oはこれを使うActivitiesが責を持っておこなうべし。
 * （コンテクストを盥回ししようとしてもうまい事いかなかったのよねー
 * 
 * データファイルの有無による初期化の呼び出しも。
 * 
 * データ構造の変換がめんどくさいので下手に多次元配列で書かないようにした。
 * 
 * 参考；http://www.masatom.in/pukiwiki/index.php?JSON%2FJson-lib%A4%F2%BB%C8%A4%A6#m889771d
 * http://www.masatom.in/pukiwiki/JSON/Json-lib%A4%F2%BB%C8%A4%A6/JSON%A4%AB%A4%E9Java%A4%D8%A4%A4%A4%ED%A4%F3%A4%CA%CA%D1%B4%B9/
 * 
 * */

public class Converter extends Activity {
	String TAG = "bh:cv";
	private static convertitem[] converters;
	private convertitem[] converterssrc = {
			new convertitem(
					"GWT+Browser",
					"http://www.google.co.jp/gwt/x?btnGo=Go&source=wax&ie=UTF-8&oe=UTF-8&u=",
					"com.android.browser",
					"com.android.browser.BrowserActivity"),
			new convertitem("* direct *", "", "com.android.browser",
					"com.android.browser.BrowserActivity"),
			new convertitem("bing+Browser",
					"http://d2c.infogin.com/ja-jp/lnk000/=",
					"com.android.browser",
					"com.android.browser.BrowserActivity"),
			new convertitem("pc2m+Dolphin",
					"http://rg0020.ddo.jp/p?_k_v=2&_k_c=100&_k_u=",
					"com.mgeek.android.DolphinBrowser",
					"com.mgeek.android.DolphinBrowser.BrowserActivity"),
			new convertitem("pc2m+Browser",
					"http://rg0020.ddo.jp/p?_k_v=2&_k_c=100&_k_u=",
					"com.android.browser",
					"com.android.browser.BrowserActivity") };
	
	class convertitem {
		String name;
		String url;
		String app;
		String activity;

		public convertitem(String name, String url, String app, String activity) {
		}
		
		public String[] toStringArray() {
			String[] retval={
				this.name,this.url,this.app, this.activity
			};
			return retval;
		}
	}
	

	// constructor
	public Converter() {
		return;
	}

	// 変換選択肢を全部返す
	public Object[] getConverters() {
		return converters;
	}

	// 変換選択肢を全部設定する
	public void setConverters(convertitem[] data) {
		converters = data;
		return;
	}

	// 変換選択肢を１つ設定する
	public void setConverter(String[] data, int idx) {
		converters[idx].name = data[0];
		converters[idx].url = data[1];
		converters[idx].app = data[2];
		converters[idx].activity = data[3];
		return;
	}

	// 変換選択肢の名前一覧を返す
	public String[] getConvertersName() {
		ArrayList<String> items_src = new ArrayList<String>();
		Log.d(TAG, "getConvertersName");

		// 選択ダイアログ用の選択肢一覧を生成
		for (convertitem tmp : converters) {
			items_src.add(tmp.name);
		}
		String[] items = (String[]) items_src.toArray(new String[0]);
		return items;
	}

	// 変換選択肢を返す
	public String[] getConverter(int which) {
		Log.d(TAG, "getConverter");
		String[] item = converters[which].toStringArray();
		return item;
	}

	// TODO: K-Vシリアライザ
	public String serialize() {
		Map map = new HashMap();
		int count =0;
		for(convertitem ci : converters){
			map.put(0, converters[count]);
			count++;
		}
		JSONObject jso = new JSONObject(map);
		return jso.toString();
	}

	// TODO: k-vデシリアライザ
	public int deserialize(String str) {
		//Parent p = (Parent) JSONObject.toBean(jsonObject, Parent.class);
		//JSONObject jsonObject = JSONObject.fromObject(this);		
		// 空のhashmapを渡されたなら初期化して返す。
		if (converters.length == 0) {
			cinit();
		}
		return converters.length;

	}

	// 変換候補の初期化
	public void cinit() {
		Log.d(TAG, "init.");
		// 初期化処理
		converters = converterssrc;
		return;
	}

}
