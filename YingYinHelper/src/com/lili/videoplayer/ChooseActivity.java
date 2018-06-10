package com.lili.videoplayer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lili.yingyinhelper.R;

// ������Ƶ�����б�
public class ChooseActivity extends ListActivity {

	private Cursor cursor;
	private static ArrayList<VideoInfo> videoList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_list);
		
		init();
	}

	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Intent intent = new Intent();
		String path = videoList.get(position).videoPath;
		intent.putExtra("CHOOSE", path);
		intent.setClass(ChooseActivity.this, VitamioActivity.class);
		startActivity(intent);

//		super.onListItemClick(l, v, position, id);
	}



	private void init() {
		String[] thumbColumns = new String[]{
				MediaStore.Video.Thumbnails.VIDEO_ID,
				MediaStore.Video.Thumbnails.DATA//����ͼ·��
		};
		
		String[] MediaColums = new String[]{
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE,//��Ƶ����
				MediaStore.Video.Media.DATA,//��Ƶ·��
//				MediaStore.Video.Media.DURATION,
//				MediaStore.Video.Media.SIZE
		};
		
//		cursor = this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaColums, null, null, null);
		cursor = this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaColums, null, null, null);
		
		videoList = new ArrayList<VideoInfo>();
		System.out.println(1234);
		if(cursor.moveToFirst()){
			do{
				VideoInfo info = new VideoInfo();
				//��ȡ��Ƶ����
				info.videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
				//��ȡ��Ƶ��·��
				info.videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				
				//��ȡ��Ƶ����ͼ��·��
				
				String selection = MediaStore.Video.Media._ID + "=?"; 
				
				int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
				String[] selectionArgs = new String[]{
						id+""
				};
				
				Cursor thumbCursor = this.managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
				
				if(thumbCursor != null){
					//��ɻ�ȡ��Ƶ����ͼ��·��
					info.thumbPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
				}
				
				videoList.add(info);
				
			} while(cursor.moveToNext());
		}
		
		System.out.println(7890);
		
		VideoAdapter adapter = new VideoAdapter(this, videoList);
		getListView().setAdapter(adapter);
		
	}
	
	static class VideoInfo {
		String thumbPath;//����ͼ·��
		String videoTitle;//��Ƶ����
		String videoPath;//��Ƶ��·��
	}
	
	class VideoAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<VideoInfo> videoItems;

		public VideoAdapter(Context context, ArrayList<VideoInfo> data) {
			this.context = context;
			this.videoItems = data;
		}

		@Override
		public int getCount() {
			return videoItems.size();
		}

		@Override
		public Object getItem(int position) {
			return videoItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.list_item, null);
				holder.thumbImage = (ImageView) convertView.findViewById(R.id.thumb_image);
				holder.videoTitle = (TextView) convertView.findViewById(R.id.video_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			//ָ����Ƶ����
			holder.videoTitle.setText(videoItems.get(position).videoTitle);
			
			//ָ������ͼ
			if(videoItems.get(position).thumbPath != null){
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoItems.get(position).thumbPath, Thumbnails.MINI_KIND);
				Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, 80, 80);
				BitmapDrawable db = new BitmapDrawable(bitmap1);
				holder.thumbImage.setImageDrawable(db);
			}
			
			 return convertView;
		}
		
		class ViewHolder {
			ImageView thumbImage;
			TextView videoTitle;
		}
		
	}
	
	
}
