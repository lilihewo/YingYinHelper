package com.lili.videoplayer;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lili.data.Data;
import com.lili.yingyinhelper.R;

public class MyVideoActivity extends Activity implements OnPreparedListener, OnErrorListener, 
OnCompletionListener, OnInfoListener, OnBufferingUpdateListener {

	private VideoView mVitamio_vv; // VideoView
	
	private ProgressBar mProgressBar; // 缓冲进度条
	private TextView downloadRateView;
	private TextView loadRateView; // 网速和已经加载的百分比
	
	private TextView loading; //加载提示语
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(getApplicationContext());
        
        // 加载布局文件
        setContentView(R.layout.video_player_activity);
        
        initView(); // 初始化控件
        
        System.out.println(Data.path);
        if (Data.path != null) {
            mVitamio_vv.setVideoPath(Data.path);
    		mVitamio_vv.setMediaController(new MediaController(this));
    		mVitamio_vv.requestFocus();
            
    		/**---------------设置监听---------------**/
    		mVitamio_vv.setOnPreparedListener(this);
    		mVitamio_vv.setOnErrorListener(this);
    		mVitamio_vv.setOnCompletionListener(this);
    		
    		mVitamio_vv.setOnInfoListener(this);
    		mVitamio_vv.setOnBufferingUpdateListener(this);
        }
    }
    
    // 初始化控件
    private void initView() {
        // vitamio框架提供的VideoView
		mVitamio_vv = (VideoView) findViewById(R.id.vitamio_vv);
    	// 缓冲进度条
    	mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    	//
    	downloadRateView = (TextView) findViewById(R.id.download_rate);
    	//
    	loadRateView = (TextView) findViewById(R.id.load_rate);
    	
    	loading = (TextView) findViewById(R.id.loading);
    }

    // 在有警告或错误信息时调用
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		// 如果是开始缓存
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVitamio_vv.isPlaying()) {
				mVitamio_vv.pause();
				mProgressBar.setVisibility(View.VISIBLE);
				downloadRateView.setText("");
				loadRateView.setText("");
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);
			}
			break;

		case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓存完成，继续播放
			mVitamio_vv.start();
			mProgressBar.setVisibility(View.GONE);
			downloadRateView.setVisibility(View.GONE);
			loadRateView.setVisibility(View.GONE);
			// 不在显示提示语
			loading.setVisibility(View.GONE);
			break;
		
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED://显示 下载速度（KB/s）
			downloadRateView.setText("" + extra + "kb/s" + "  ");
			break;
			
		default:
			break;
		}
		
		
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	// 在网络视频流缓冲变化时调用
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText(percent + "%");
	}

	
	
}









