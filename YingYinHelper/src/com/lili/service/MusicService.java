package com.lili.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.lili.config.Constants;
import com.lili.player.MediaUtils;

public class MusicService extends Service implements OnErrorListener, OnPreparedListener, OnCompletionListener {

	private MediaPlayer mPlayer;
	private Messenger mMessenger;
	private Timer mTimer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO
		return null;
	}

	@Override
	public void onCreate() {//多次启动service执行一次
		mPlayer = new MediaPlayer();
		//设置监听器
		mPlayer.setOnErrorListener(this);//设置资源的时候出错了
		mPlayer.setOnPreparedListener(this);//设置资源的时候出错了
		mPlayer.setOnCompletionListener(this);//设置资源的时候出错了
		mPlayer.setOnCompletionListener(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {//每次启动都会来到此方法
		String option = intent.getStringExtra("option");
		if (mMessenger == null) {
			mMessenger = (Messenger) intent.getExtras().get("messenger");
		}
		if ("playMusic".equals(option)) {
			String path = intent.getStringExtra("path");
			play(path);
		} else if ("pauseMusic".equals(option)) {
			pause();
		} else if ("toPlay".equals(option)) {
			continuePlay();
		} else if ("stop".equals(option)) {
			stop();
		} else if ("progress".equals(option)) {
			int progress = intent.getIntExtra("progress", -1);
			seekPlay(progress);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {//销毁
		// TODO
		super.onDestroy();
	}

	/**---------------封装音乐播放常见的方法 begin---------------**/

	//activity想要调用service中方法
	//1.bindService启动服务
	//2.aidl
	//3.发送广播
	//4.消息机制
	//5.利用service多次启动,会重复调用 onStartCommd()方法的特性

	/**
	 * 播放音乐
	 * @param path
	 */
	public void play(String path) {
		try {
			mPlayer.reset();//idle
			mPlayer.setDataSource(path);//设置歌曲的路径
			mPlayer.prepare();//开始准备,本地音乐使用同步准备就可以了
			mPlayer.start();//开始播放
			MediaUtils.CURSTATE = Constants.STATE_PLAY;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 暂停
	 */
	public void pause() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			MediaUtils.CURSTATE = Constants.STATE_PAUSE;
		}
	}

	/**
	 * 继续播放
	 */
	public void continuePlay() {
		if (mPlayer != null && !mPlayer.isPlaying()) {
			mPlayer.start();
			MediaUtils.CURSTATE = Constants.STATE_PLAY;
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		if (mPlayer != null) {
			mPlayer.stop();
			MediaUtils.CURSTATE = Constants.STATE_STOP;
		}
	}

	/**
	 * 进度播放
	 * @param progress
	 */
	public void seekPlay(int progress) {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.seekTo(progress);
		}
	}

	/**---------------封装音乐播放常见的方法 end---------------**/

	/**---------------相关的回调方法---------------**/
	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			Message msg = Message.obtain();
			msg.what = Constants.MSG_ONCOMPLETION;
			// 发送消息
			try {
				mMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					//1.准备好的时候.告诉activity,当前歌曲的总时长
					int currentPosition = mPlayer.getCurrentPosition();
					int totalDuration = mPlayer.getDuration();
					Message msg = Message.obtain();
					msg.what = Constants.MSG_ONPREPARED;
					msg.arg1 = currentPosition;
					msg.arg2 = totalDuration;

					//发送消息
					mMessenger.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);

	}

	
	
	

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(getApplicationContext(), "播放出错了", 0).show();
		return true;
	}
}
