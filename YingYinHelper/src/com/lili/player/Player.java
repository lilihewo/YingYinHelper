package com.lili.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

// 播放器类
public class Player {
	// 上下文对象
	private Context context;
	// 媒体播放器对象
	private MediaPlayer mediaPlayer;
	
	// 构造函数
	public Player(Context context) {
		this.context = context; // 得到上下文对象的引用
	}
	
	// 播放音乐
	public void playMusic(String filePath) {
		playMusic(filePath, false);
	}
	// 播放音乐
	public void playMusic(String filePath, boolean isLoop) {
		mediaPlayer = MediaPlayer.create(context, Uri.parse("file://" + filePath));
		if (mediaPlayer != null) {
			mediaPlayer.setLooping(isLoop); // 设置是否循环，默认为不循环
			mediaPlayer.start();
		} else { // 如果mediaPlayer为空，可能是路径错误，也有可能是没有访问SD的权限
			// 检查是否有权限，如果没有就向用户申请
			
			// 检查filePath是否有文件
			System.out.println(mediaPlayer);
		}
	}
}
