package com.lili.musicplayer;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lili.config.Constants;
import com.lili.player.LrcUtil;
import com.lili.player.MediaUtils;
import com.lili.service.MusicService;
import com.lili.views.LyricShow;
import com.lili.views.MyAdapter;
import com.lili.views.ScrollableViewGroup;
import com.lili.views.ScrollableViewGroup.OnCurrentViewChangedListener;
import com.lili.yingyinhelper.R;

public class MusicPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mInstance = this; // ��ǰactivity������
        
    	initView();
		initData();
		initListener();
    }
    
	private TextView mTv_curduration;
//	private TextView mTv_minilrc;
	private TextView mTv_totalduration;
	private SeekBar mSk_duration;
	private ImageView mIv_bottom_play;
	private ListView mLv_list;
	private ScrollableViewGroup mSvg_main;
	private LyricShow mTv_lyricShow;
	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mTv_curduration = (TextView) findViewById(R.id.tv_curduration);
//		mTv_minilrc = (TextView) findViewById(R.id.tv_minilrc);
		mTv_totalduration = (TextView) findViewById(R.id.tv_totalduration);
		mSk_duration = (SeekBar) findViewById(R.id.sk_duration);
		mIv_bottom_play = (ImageView) findViewById(R.id.iv_bottom_play);
		mLv_list = (ListView) findViewById(R.id.lv_list);
		mSvg_main = (ScrollableViewGroup) findViewById(R.id.svg_main);
		
		// �������View
		mTv_lyricShow = (LyricShow) findViewById(R.id.tv_lrc);
		// Ĭ����ʾ�б�
		mSvg_main.setCurrentView(0);
	}
	
	/**
	 * ���ݵļ���
	 */
	private void initData() {
		MediaUtils.initSongList(this);
		mLv_list.setAdapter(new MyAdapter(this));
	}
	
	/**
	 * ��ʼ������
	 */
	private void initListener() {
		// ��������
		mSvg_main.setOnCurrentViewChangedListener(new OnCurrentViewChangedListener() {

			@Override
			public void onCurrentViewChanged(View view, int currentview) {

			}
		});
	
		// listView����
		mLv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//1.�޸�curposition
				changeColorWhite();
				MediaUtils.CURPOSITION = position;
				changeColorGreen();
				//2.playMusic
				startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
				//3.�޸�ͼ��
				mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
			}
		});

		// ����������
		mSk_duration.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {//ֹͣ��ק
				mSk_duration.setProgress(seekBar.getProgress());
				startMediaService("progress", seekBar.getProgress());
				//���ֲ�����,��ת��ָ����λ�ò���
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {//��������ק��ť
				// TODO

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//���ȸı�
				// TODO

			}
		});
	}
	
    // ��������
    public void playMusic(View view) {
    	System.out.println("playMusic");
    	
		//��������.�����÷��񲥷�����
		if (MediaUtils.CURSTATE == Constants.STATE_STOP) {//Ĭ����ֹͣ,����ͱ䲥��
			startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
			//�޸�ͼ��
			mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
		} else if (MediaUtils.CURSTATE == Constants.STATE_PLAY) {//�ڶ��ε����ʱ��.��ǰ��״̬�ǲ���
			startMediaService("pauseMusic");
			//�޸�ͼ��
			mIv_bottom_play.setImageResource(R.drawable.img_playback_bt_play);
		} else if (MediaUtils.CURSTATE == Constants.STATE_PAUSE) {//�����ε����ʱ��.��ǰ��״̬����ͣ
			startMediaService("toPlay");
			//�޸�ͼ��
			mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
		}
    }
    
    // ��ʾ����б�
    public void showList(View view) {
		mSvg_main.setCurrentView(0);
    }
    
    // ��ʾ���
    public void showLrc(View view) {
    	mSvg_main.setCurrentView(1);
    }

    // ������һ��
    public void last(View view) {
    	changeColorWhite(); // ��ԭ����������ɫ��Ϊ��ɫ
    	
    	if (MediaUtils.CURPOSITION > 0) {
			MediaUtils.CURPOSITION--;
		} else { // ����ǵ�һ�ף���ô�������һ��
			MediaUtils.CURPOSITION = MediaUtils.songList.size() - 1;
		}
		changeColorGreen(); // Ҫ���ŵĸ��������ӵ���ɫ��Ϊgreen
		//2.playMusic
		startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
		//3.�޸�ͼ��
		mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
    }
    
    // ������һ��
    public void next(View view) {
    	playNextSong();
    }
    
    // ������һ�׸�
    public void playNextSong() {
    	changeColorWhite(); // ��ԭ����������ɫ��Ϊ��ɫ
    	
		if (MediaUtils.CURPOSITION < MediaUtils.songList.size() - 1) {//MediaUtils.songList.size() - 1
			MediaUtils.CURPOSITION++;
		} else { // ��������ˣ�˵���������һ�ף���ô�Ͳ��ŵ�һ��
			MediaUtils.CURPOSITION = 0; // ��һ��
		}
		
		changeColorGreen(); // Ҫ���ŵĸ��������ӵ���ɫ��Ϊgreen
		//2.playMusic
		startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
		//3.�޸�ͼ��
		mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
    }
    
    
	public void startMediaService(String option, String path) {
		Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
		service.putExtra("option", option);
		service.putExtra("messenger", new Messenger(handler));
		service.putExtra("path", path);
		startService(service);
	}
	
	public void startMediaService(String option) {
		Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
		service.putExtra("messenger", new Messenger(handler));
		service.putExtra("option", option);
		startService(service);
	}

	// ����������
	public void startMediaService(String option, int progress) {
		Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
		service.putExtra("option", option);
		service.putExtra("messenger", new Messenger(handler));
		service.putExtra("progress", progress);
		startService(service);
	}
    
    private MusicPlayerActivity mInstance;
    private LrcUtil mLrcUtil;
    private Handler handler = new Handler() {//���ս��,ˢ��ui
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.MSG_ONPREPARED:
				int currentPosition = msg.arg1;
				int totalDuration = msg.arg2;
				
				mTv_curduration.setText(MediaUtils.duration2Str(currentPosition));
				mTv_totalduration.setText(MediaUtils.duration2Str(totalDuration));
				mSk_duration.setMax(totalDuration);
				mSk_duration.setProgress(currentPosition);
				
				// ��ʸ���
				if (mLrcUtil == null) {
					mLrcUtil = new LrcUtil(mInstance);
				}
				//���л����
				File f = MediaUtils.getLrcFile(MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
				mLrcUtil.ReadLRC(f);
				//ʹ�ù���
//				mLrcUtil.RefreshLRC(currentPosition);
				
				// �������
				//1. ���ü���
				mTv_lyricShow.SetTimeLrc(LrcUtil.lrclist);
				//2. ���¹������
				mTv_lyricShow.SetNowPlayIndex(currentPosition);
				break;
			
			// �����������
			case Constants.MSG_ONCOMPLETION:
				playNextSong(); // ������һ�׸�
				break;

			default:
				break;
			}
		};
	};    
	
	
//	/**
//	 * �޸�minilrc���ı�
//	 * @param lrcString
//	 */
//	public void setMiniLrc(String lrcString) {
//		mTv_minilrc.setText(lrcString);
//	}
	
	/**
	 * �޸���ɫ.ֻҪ���ǵ�curPostion�޸���.��ô��ɫֵ����Ҫ�޸�
	 * @param color
	 */
	public void changeColorWhite() {
		TextView tv = (TextView) mLv_list.findViewWithTag(MediaUtils.CURPOSITION);
		if (tv != null) {
			tv.setTextColor(Color.WHITE);
		}
	}

	public void changeColorGreen() {
		TextView tv = (TextView) mLv_list.findViewWithTag(MediaUtils.CURPOSITION);
		if (tv != null) {
			tv.setTextColor(Color.GREEN);
		}
	}
	
	// �û����·��ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("���������棿");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					System.exit(0);
					
//					// ֹͣ����
//					startMediaService("stop");
//					// ���ٵ�ǰActivity
//					MusicPlayerActivity.this.finish();
				}
			});


			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});


			//    ��ʾ���öԻ���
			builder.show();
		}

		return false;
	}

    
}












