package com.lili.videoplayer;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.lili.yingyinhelper.R;

// ���ز���
public class VitamioActivity extends Activity {
    private int mMaxVolume;//�������
	private int mVolumn = -1;//��ǰ����
	private float mBrightness = -1f;//��ǰ����
	private AudioManager mAudioManager;
	private MediaController mediaController;
	private GestureDetector mGestureDetector;
	private View mOperationVolumnBrightness;
	private VideoView mVideoView;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	
	private int layout = VideoView.VIDEO_LAYOUT_ZOOM;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(getApplicationContext());
		
        setContentView(R.layout.main);
        
        mVideoView = (VideoView) findViewById(R.id.surfaceview);
        mOperationVolumnBrightness = findViewById(R.id.operation_volumn_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        
        //����������
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //�������
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //������
        mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        
//        String path = Environment.getExternalStorageDirectory() + "/minion_10.mp4";
//        path = "rtsp://l.m.cztv.com:554/cctv5/live_ld";//��������
        
        String url = getIntent().getStringExtra("CHOOSE");
        if(url != null){
        	mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        	mVideoView.setVideoPath(url);
        }
        
        
//        mVideoView.setVideoPath(path);
        
//        mVideoView.setOnCompletionListener(new OnCompletionListener() {
//			
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				mVideoView.stopPlayback();
//				VitamioActivity.this.finish();
//				
//			}
//		});
//        
//        mGestureDetector = new GestureDetector(this, new MyGestureListener());
  
	
	}
	
//	class MyGestureListener extends SimpleOnGestureListener{
//		
//		//˫��
//		@Override
//		public boolean onDoubleTap(MotionEvent e) {
//			if(layout == VideoView.VIDEO_LAYOUT_ZOOM){
//				layout = VideoView.VIDEO_LAYOUT_ORIGIN;
//			} else {
//				layout++;
//			}
//			
//			if(mVideoView != null){
//				mVideoView.setVideoLayout(layout, 0);
//			}
//			
//			return false;
//		}
//
//		//����
//		@Override
//		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//			
//			float oldX = e1.getX();
//			float oldY = e1.getY();
//			float y = e2.getRawY();
//			
//			//��ȡ�ֻ���Ļ���
//			Display display = getWindowManager().getDefaultDisplay();
//			int windowWidth = display.getWidth();
//			int windowHeight = display.getHeight();
//			
//			if(oldX < windowWidth * 0.2){//��߻��� �ı�����
//				onBrightness((oldY - y) / windowHeight);
//			} else if(oldX > windowWidth * 0.8){//�ұ߻��� �ı�����
//				onVolumn((oldY - y) / windowHeight);
//			}
//			
//			
//			return super.onScroll(e1, e2, distanceX, distanceY);
//		}
//
//		/**
//		 * ���� �ı�����
//		 * @param percent
//		 */
//		private void onVolumn(float percent) {
//			if(mVolumn == -1){
//				//��ȡ��ǰ����
//				mVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//				
//				if(mVolumn < 0){
//					mVolumn = 0;
//				}
//				
//				//��ʾ
//				mOperationBg.setImageResource(R.drawable.video_volumn_bg);
//				mOperationVolumnBrightness.setVisibility(View.VISIBLE);
//			}
//			
//			int index = (int) (percent * mMaxVolume + mVolumn);
//			
//			if(index > mMaxVolume){
//				index = mMaxVolume;
//			} else if(index < 0){
//				index = 0;
//			}
//			
//			//��������
//			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
//			
//			LayoutParams lp = mOperationPercent.getLayoutParams();
//			lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
//			mOperationPercent.setLayoutParams(lp);
//		}
//
//		/**
//		 * ���� �ı�����
//		 * @param percent
//		 */
//		private void onBrightness(float percent) {
//			if(mBrightness == -1f){
//				mBrightness = getWindow().getAttributes().screenBrightness;
//				if(mBrightness < 0.01f){
//					mBrightness = 0.50f;
//				}
//				
//				//��ʾ
//				mOperationBg.setImageResource(R.drawable.video_brightness_bg);
//				mOperationVolumnBrightness.setVisibility(View.VISIBLE);
//				
//			}
//			
//			WindowManager.LayoutParams lpa = getWindow().getAttributes();
//			lpa.screenBrightness = mBrightness + percent;
//			
//			if(lpa.screenBrightness > 1.0f){
//				lpa.screenBrightness = 1.0f;
//			} else if(lpa.screenBrightness < 0.01f){
//				lpa.screenBrightness = 0.01f;
//			}
//			//�ı�����
//			getWindow().setAttributes(lpa);
//			
//			LayoutParams lp = mOperationPercent.getLayoutParams();
//			lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
//			mOperationPercent.setLayoutParams(lp);
//		}
//		
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if(mGestureDetector.onTouchEvent(event)){
//			return true;
//		}
//		
//		switch (event.getAction()) {
//		//�ɿ�
//		case MotionEvent.ACTION_UP:
//			endGesture();
//			break;
//
//		}
//		
//		return super.onTouchEvent(event);
//	}
//
//	/**
//	 * ��������ʶ��
//	 */
//	private void endGesture() {
//		mVolumn = -1;
//		mBrightness = -1;
//		
//		handler.removeMessages(0);
//		handler.sendEmptyMessageDelayed(0, 500);
//		
//	}
//	
//	private Handler handler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			mOperationVolumnBrightness.setVisibility(View.GONE);
//		}
//		
//	};
//	private String path;
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		if(mVideoView != null){
//			mVideoView.setVideoLayout(layout, 0);
//		}
//		super.onConfigurationChanged(newConfig);
//	}
	
	
}