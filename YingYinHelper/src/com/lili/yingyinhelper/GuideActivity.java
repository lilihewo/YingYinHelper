package com.lili.yingyinhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lili.musicplayer.MusicPlayerActivity;
import com.lili.videoplayer.ChooseActivity;
import com.lili.videoplayer.VideoListActivity;

// ����Activity
public class GuideActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_activity);
	}
	
	// ���ű�����Ƶ
	public void playLocalVideo(View view) {
		Intent intent = new Intent(this, ChooseActivity.class);
		this.startActivity(intent);
	}
	
	// ���ű�������
	public void playLocalMusic(View view) {
		Intent intent = new Intent(this, MusicPlayerActivity.class);
		this.startActivity(intent);
	}
	
	// ����������Ƶ
	public void playOnlineVideo(View view) {
		// �л���������Ƶ�����б�
		Intent intent = new Intent(this, VideoListActivity.class);
		this.startActivity(intent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.about) {
			
			Intent intent = new Intent(this, AboutActivity.class);
			this.startActivity(intent);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}













