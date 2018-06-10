package com.lili.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

// ��������
public class Player {
	// �����Ķ���
	private Context context;
	// ý�岥��������
	private MediaPlayer mediaPlayer;
	
	// ���캯��
	public Player(Context context) {
		this.context = context; // �õ������Ķ��������
	}
	
	// ��������
	public void playMusic(String filePath) {
		playMusic(filePath, false);
	}
	// ��������
	public void playMusic(String filePath, boolean isLoop) {
		mediaPlayer = MediaPlayer.create(context, Uri.parse("file://" + filePath));
		if (mediaPlayer != null) {
			mediaPlayer.setLooping(isLoop); // �����Ƿ�ѭ����Ĭ��Ϊ��ѭ��
			mediaPlayer.start();
		} else { // ���mediaPlayerΪ�գ�������·������Ҳ�п�����û�з���SD��Ȩ��
			// ����Ƿ���Ȩ�ޣ����û�о����û�����
			
			// ���filePath�Ƿ����ļ�
			System.out.println(mediaPlayer);
		}
	}
}
