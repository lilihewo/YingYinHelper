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
	public void onCreate() {//�������serviceִ��һ��
		mPlayer = new MediaPlayer();
		//���ü�����
		mPlayer.setOnErrorListener(this);//������Դ��ʱ�������
		mPlayer.setOnPreparedListener(this);//������Դ��ʱ�������
		mPlayer.setOnCompletionListener(this);//������Դ��ʱ�������
		mPlayer.setOnCompletionListener(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {//ÿ���������������˷���
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
	public void onDestroy() {//����
		// TODO
		super.onDestroy();
	}

	/**---------------��װ���ֲ��ų����ķ��� begin---------------**/

	//activity��Ҫ����service�з���
	//1.bindService��������
	//2.aidl
	//3.���͹㲥
	//4.��Ϣ����
	//5.����service�������,���ظ����� onStartCommd()����������

	/**
	 * ��������
	 * @param path
	 */
	public void play(String path) {
		try {
			mPlayer.reset();//idle
			mPlayer.setDataSource(path);//���ø�����·��
			mPlayer.prepare();//��ʼ׼��,��������ʹ��ͬ��׼���Ϳ�����
			mPlayer.start();//��ʼ����
			MediaUtils.CURSTATE = Constants.STATE_PLAY;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ͣ
	 */
	public void pause() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			MediaUtils.CURSTATE = Constants.STATE_PAUSE;
		}
	}

	/**
	 * ��������
	 */
	public void continuePlay() {
		if (mPlayer != null && !mPlayer.isPlaying()) {
			mPlayer.start();
			MediaUtils.CURSTATE = Constants.STATE_PLAY;
		}
	}

	/**
	 * ֹͣ����
	 */
	public void stop() {
		if (mPlayer != null) {
			mPlayer.stop();
			MediaUtils.CURSTATE = Constants.STATE_STOP;
		}
	}

	/**
	 * ���Ȳ���
	 * @param progress
	 */
	public void seekPlay(int progress) {
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.seekTo(progress);
		}
	}

	/**---------------��װ���ֲ��ų����ķ��� end---------------**/

	/**---------------��صĻص�����---------------**/
	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			Message msg = Message.obtain();
			msg.what = Constants.MSG_ONCOMPLETION;
			// ������Ϣ
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
					//1.׼���õ�ʱ��.����activity,��ǰ��������ʱ��
					int currentPosition = mPlayer.getCurrentPosition();
					int totalDuration = mPlayer.getDuration();
					Message msg = Message.obtain();
					msg.what = Constants.MSG_ONPREPARED;
					msg.arg1 = currentPosition;
					msg.arg2 = totalDuration;

					//������Ϣ
					mMessenger.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);

	}

	
	
	

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(getApplicationContext(), "���ų�����", 0).show();
		return true;
	}
}
