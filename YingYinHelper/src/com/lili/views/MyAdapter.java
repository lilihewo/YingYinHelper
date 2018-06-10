package com.lili.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lili.bean.Music;
import com.lili.player.MediaUtils;
import com.lili.yingyinhelper.R;

public class MyAdapter extends BaseAdapter {
	//1.����Դ��ʲô�ط� datasource
	private Context context;

	public MyAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		if (MediaUtils.songList != null) {
			return MediaUtils.songList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (MediaUtils.songList != null) {
			return MediaUtils.songList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**---------------��ͼ�ĳ�ʼ��---------------**/
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_music, null);
			holder = new ViewHolder();
			convertView.setTag(holder);//����tag������
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_artist = (TextView) convertView.findViewById(R.id.tv_artist);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/**---------------�õ�����---------------**/
		Music music = MediaUtils.songList.get(position);
		/**---------------��������---------------**/
		holder.tv_title.setText(music.title);
		holder.tv_artist.setText(music.artist);

		if (MediaUtils.CURPOSITION == position) {
			//			holder.tv_title.setTextColor(����ǽ���һ��ʮ�����Ƶ���ɫֵ);//00FF00
			//			holder.tv_title.setTextColor(R.color._greeen);//��������
			//			holder.tv_title.setTextColor(context.getResources().getColor(R.color._greeen));//��ȷ����
			holder.tv_title.setTextColor(Color.GREEN);//��ȷ����
		} else {
			holder.tv_title.setTextColor(Color.WHITE);//��ȷ����
		}

		holder.tv_title.setTag(position);//tag�����þ���Ϊ�˷���
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_artist;
	}
	
}
