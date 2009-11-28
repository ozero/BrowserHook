package org.example.browserhook;

import java.util.ArrayList;
import android.util.Log;

public class Converter {
	private static String[][] converters = new String[][] {
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
					"com.android.browser.BrowserActivity" },

	};
	String TAG = "BrowserHook";
	
	//constructor
	public Converter(){
		cload();
		return;
	}

	//変換選択肢を全部返す
	public String[][] getConverters(){
		return converters;
	}
	
	//変換選択肢を全部設定する
	public void setConverters(String[][] data){
		converters = data;
		csave();
		return;
	}
	
	//変換選択肢を１つ設定する
	public void setConverter(String[] data, int idx){
		converters[idx] = data;
		csave();
		return;
	}
	
	//変換選択肢の名前一覧を返す
	public String[] getConvertersName(){
		ArrayList<String> items_src = new ArrayList<String>();
		Log.d(TAG, "Converter:getConvertersName");
	
		// 選択ダイアログ用の選択肢一覧を生成
		for (String[] tmp : converters) {
			items_src.add(tmp[0]);
		}
		String[] items = (String[]) items_src.toArray(new String[0]);
		return items;
	}

	//変換選択肢を返す
	public String[] getConverter(int which){
		Log.d(TAG, "Converter:getConverter");
		String[] item = converters[which];
		return item;
	}
	
	//todo: 内蔵ストレージファイルの有無確認
	public int cchk(){
		int retval = 0;
		return retval;
	}
	
	//todo: 内蔵ストレージから読み出し
	private void cload(){}
	
	//todo: 内蔵ストレージに保存
	private void csave(){}
	
	//todo: 内蔵ストレージファイルの初期化
	public void cinit(){}
	
	//todo: sdcardからimport
	public void csload(){}
	
	//todo: sdcardに保存
	public void cssave(){}
	
	
	
	
}
