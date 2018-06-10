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
        
        mInstance = this; // 当前activity的引用
        
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
	 * 初始化控件
	 */
	private void initView() {
		mTv_curduration = (TextView) findViewById(R.id.tv_curduration);
//		mTv_minilrc = (TextView) findViewById(R.id.tv_minilrc);
		mTv_totalduration = (TextView) findViewById(R.id.tv_totalduration);
		mSk_duration = (SeekBar) findViewById(R.id.sk_duration);
		mIv_bottom_play = (ImageView) findViewById(R.id.iv_bottom_play);
		mLv_list = (ListView) findViewById(R.id.lv_list);
		mSvg_main = (ScrollableViewGroup) findViewById(R.id.svg_main);
		
		// 滚动歌词View
		mTv_lyricShow = (LyricShow) findViewById(R.id.tv_lrc);
		// 默认显示列表
		mSvg_main.setCurrentView(0);
	}
	
	/**
	 * 数据的加载
	 */
	private void initData() {
		MediaUtils.initSongList(this);
		mLv_list.setAdapter(new MyAdapter(this));
	}
	
	/**
	 * 初始化监听
	 */
	private void initListener() {
		// 滑动监听
		mSvg_main.setOnCurrentViewChangedListener(new OnCurrentViewChangedListener() {

			@Override
			public void onCurrentViewChanged(View view, int currentview) {

			}
		});
	
		// listView监听
		mLv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//1.修改curposition
				changeColorWhite();
				MediaUtils.CURPOSITION = position;
				changeColorGreen();
				//2.playMusic
				startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
				//3.修改图标
				mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
			}
		});

		// 进度条监听
		mSk_duration.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {//停止拖拽
				mSk_duration.setProgress(seekBar.getProgress());
				startMediaService("progress", seekBar.getProgress());
				//音乐播放器,跳转到指定的位置播放
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {//触摸到拖拽按钮
				// TODO

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//进度改变
				// TODO

			}
		});
	}
	
    // 播放音乐
    public void playMusic(View view) {
    	System.out.println("playMusic");
    	
		//启动服务.而且让服务播放音乐
		if (MediaUtils.CURSTATE == Constants.STATE_STOP) {//默认是停止,点击就变播放
			startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
			//修改图标
			mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
		} else if (MediaUtils.CURSTATE == Constants.STATE_PLAY) {//第二次点击的时候.当前的状态是播放
			startMediaService("pauseMusic");
			//修改图标
			mIv_bottom_play.setImageResource(R.drawable.img_playback_bt_play);
		} else if (MediaUtils.CURSTATE == Constants.STATE_PAUSE) {//第三次点击的时候.当前的状态是暂停
			startMediaService("toPlay");
			//修改图标
			mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
		}
    }
    
    // 显示歌词列表
    public void showList(View view) {
		mSvg_main.setCurrentView(0);
    }
    
    // 显示歌词
    public void showLrc(View view) {
    	mSvg_main.setCurrentView(1);
    }

    // 播放上一首
    public void last(View view) {
    	changeColorWhite(); // 将原来的文字颜色改为白色
    	
    	if (MediaUtils.CURPOSITION > 0) {
			MediaUtils.CURPOSITION--;
		} else { // 如果是第一首，那么播放最后一首
			MediaUtils.CURPOSITION = MediaUtils.songList.size() - 1;
		}
		changeColorGreen(); // 要播放的歌曲的名子的颜色改为green
		//2.playMusic
		startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
		//3.修改图标
		mIv_bottom_play.setImageResource(R.drawable.appwidget_pause);
    }
    
    // 播放下一首
    public void next(View view) {
    	playNextSong();
    }
    
    // 播放下一首歌
    public void playNextSong() {
    	changeColorWhite(); // 将原来的文字颜色改为白色
    	
		if (MediaUtils.CURPOSITION < MediaUtils.songList.size() - 1) {//MediaUtils.songList.size() - 1
			MediaUtils.CURPOSITION++;
		} else { // 如果大于了，说明到达最后一首，那么就播放第一首
			MediaUtils.CURPOSITION = 0; // 第一首
		}
		
		changeColorGreen(); // 要播放的歌曲的名子的颜色改为green
		//2.playMusic
		startMediaService("playMusic", MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
		//3.修改图标
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

	// 进度条播放
	public void startMediaService(String option, int progress) {
		Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
		service.putExtra("option", option);
		service.putExtra("messenger", new Messenger(handler));
		service.putExtra("progress", progress);
		startService(service);
	}
    
    private MusicPlayerActivity mInstance;
    private LrcUtil mLrcUtil;
    private Handler handler = new Handler() {//接收结果,刷新ui
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.MSG_ONPREPARED:
				int currentPosition = msg.arg1;
				int totalDuration = msg.arg2;
				
				mTv_curduration.setText(MediaUtils.duration2Str(currentPosition));
				mTv_totalduration.setText(MediaUtils.duration2Str(totalDuration));
				mSk_duration.setMax(totalDuration);
				mSk_duration.setProgress(currentPosition);
				
				// 歌词更新
				if (mLrcUtil == null) {
					mLrcUtil = new LrcUtil(mInstance);
				}
				//序列化歌词
				File f = MediaUtils.getLrcFile(MediaUtils.songList.get(MediaUtils.CURPOSITION).path);
				mLrcUtil.ReadLRC(f);
				//使用功能
//				mLrcUtil.RefreshLRC(currentPosition);
				
				// 滚动歌词
				//1. 设置集合
				mTv_lyricShow.SetTimeLrc(LrcUtil.lrclist);
				//2. 更新滚动歌词
				mTv_lyricShow.SetNowPlayIndex(currentPosition);
				break;
			
			// 如果播放完了
			case Constants.MSG_ONCOMPLETION:
				playNextSong(); // 播放下一首歌
				break;

			default:
				break;
			}
		};
	};    
	
	
//	/**
//	 * 修改minilrc的文本
//	 * @param lrcString
//	 */
//	public void setMiniLrc(String lrcString) {
//		mTv_minilrc.setText(lrcString);
//	}
	
	/**
	 * 修改颜色.只要我们的curPostion修改了.那么颜色值就需要修改
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
	
	// 用户按下返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("返回主界面？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					System.exit(0);
					
//					// 停止播放
//					startMediaService("stop");
//					// 销毁当前Activity
//					MusicPlayerActivity.this.finish();
				}
			});


			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});


			//    显示出该对话框
			builder.show();
		}

		return false;
	}

    
}












