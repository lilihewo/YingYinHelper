package com.lili.videoplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lili.data.Data;
import com.lili.yingyinhelper.R;

// 在线视频列表
public class VideoListActivity extends ListActivity {
	
	private ArrayList<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_list_activity);
	
		initData(); // 初始化数据
		
		SimpleAdapter listAdapter = new SimpleAdapter
				(
				this,
				list,
				R.layout.myvideo,
				new String[] {"name" },
				new int[] 	 {R.id.name}
				);
		setListAdapter(listAdapter);
	}
	
	// 初始化数据
	private void initData() {
		list = new ArrayList<HashMap<String, String>>();
		// 星月神话
		String path = "http://112.5.254.247/hc.yinyuetai.com/uploads/videos/common/37EA014085A1618920F265F17C8F1EED.flv?sc=9b4c78bdff2e6e17&br=777&vid=85926&aid=23&area=ML&vst=0&ptp=mv&rd=yinyuetai.com";
		HashMap<String, String> xingYue = new HashMap<String, String>();
		xingYue.put("path", path);
		xingYue.put("name", "星月神话");
		list.add(xingYue);

		//
		String baiHuPath = "http://112.5.254.246/hc.yinyuetai.com/uploads/videos/common/BD78014106D935CA4BFD2FD06984D1FB.flv?sc=43861bf87ec0459f&br=778&vid=762329&aid=12867&area=ML&vst=0&ptp=mv&rd=yinyuetai.com";
		HashMap<String, String> baiHu = new HashMap<String, String>();
		baiHu.put("path", baiHuPath);
		baiHu.put("name", "白狐");
		list.add(baiHu);

		
//		// 香港卫视
//		HashMap<String, String> hongkong = new HashMap<String, String>();
//		hongkong.put("path", "rtmp://live.hkstv.hk.lxdns.com/live/hks");
//		hongkong.put("name", "香港卫视");
//		list.add(hongkong);
//		
//		// 外国频道1
//		HashMap<String, String> waiguo1 = new HashMap<String, String>();
//		waiguo1.put("path", "http://aljazeera-eng-hd-live.hls.adaptive.level3.net/aljazeera/english2/index.m3u8");
//		waiguo1.put("name", "外国频道1");
//		list.add(waiguo1);		
		
		// 又不行了
//		// CCTV1
//		HashMap<String, String> CCTV1 = new HashMap<String, String>();
//		CCTV1.put("path", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
//		CCTV1.put("name", "CCTV1");
//		list.add(CCTV1);
//		
//		// CCTV3
//		HashMap<String, String> CCTV3 = new HashMap<String, String>();
//		CCTV3.put("path", "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8");
//		CCTV3.put("name", "CCTV3");
//		list.add(CCTV3);
//		
//		// CCTV5
//		HashMap<String, String> CCTV5 = new HashMap<String, String>();
//		CCTV5.put("path", "http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8");
//		CCTV5.put("name", "CCTV5");
//		list.add(CCTV5);
//		
//		// CCTV5+
//		HashMap<String, String> CCTV5p = new HashMap<String, String>();
//		CCTV5p.put("path", "http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8");
//		CCTV5p.put("name", "CCTV5+");
//		list.add(CCTV5p);
//		
//		// CCTV6
//		HashMap<String, String> CCTV6 = new HashMap<String, String>();
//		CCTV6.put("path", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8");
//		CCTV6.put("name", "CCTV6");
//		list.add(CCTV6);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		boolean isHasNetwork = isNetworkAvailable(this);
		// 如果没有网络
		if (!isHasNetwork) {
			Toast.makeText(VideoListActivity.this, "当前无网络连接", Toast.LENGTH_SHORT).show();
			return;
		}
		
		HashMap mHashMap = (HashMap) getListAdapter().getItem(position);

		String path = (String) mHashMap.get("path");
		Data.path = path;
		Intent intent = new Intent(this, MyVideoActivity.class);
		this.startActivity(intent);
	}
	
	
	// 检测网络状态
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的  
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					System.out.println("当前网络是连接的");
					return true;
				}
			}
		}

		return false;
	}
}














