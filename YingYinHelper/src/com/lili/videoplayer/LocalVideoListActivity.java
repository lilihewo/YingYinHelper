package com.lili.videoplayer;

import io.vov.vitamio.Vitamio;
import android.app.Activity;
import android.os.Bundle;

// 本地视频列表Activity
public class LocalVideoListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(getApplicationContext());
		
		setContentView(0);
	}
}
