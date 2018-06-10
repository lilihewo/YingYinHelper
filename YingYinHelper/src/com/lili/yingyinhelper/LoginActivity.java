package com.lili.yingyinhelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.lili.musicplayer.MusicPlayerActivity;

// 登录界面
public class LoginActivity extends Activity {
	
	private EditText accountEditText; // 账户
	private EditText passwordEditText; // 密码
	
	private MyHandler myHandler; // 消息处理器对象
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 初始化Bmob
		Bmob.initialize(this, "d753c3c409b44edff696e5652b65f76b");
		
		setContentView(R.layout.login_activity);

		// 创建消息处理器对象
		myHandler = new MyHandler();

		// 得到控件对象
		accountEditText = (EditText) findViewById(R.id.account);
		passwordEditText = (EditText) findViewById(R.id.password);
		
		BmobUser bmobUser = BmobUser.getCurrentUser();
		// 直接跳转
		if(bmobUser != null){
//			accountEditText.setText(bmobUser.getUsername());
			Intent intent = new Intent();
			intent.putExtra("username", account);
			intent.setClass(LoginActivity.this, GuideActivity.class);
			LoginActivity.this.startActivity(intent);
			// 销毁LoginActivity
			LoginActivity.this.finish();
		}
	}

	// 判断字符串是否超过指定长度
	private boolean isTooLong(String text) {
		boolean isTooLong = false;
		
		if (text.length() > 20) {
			isTooLong = true;
		} 
		
		return isTooLong;
	}
	
	private String account; // 账户
	private String password; // 密码

	// 登录
	public void login(View view) {
		
		boolean isHasNetwork = isNetworkAvailable(this);
		// 如果没有网络
		if (!isHasNetwork) {
			Toast.makeText(LoginActivity.this, "当前无网络连接", Toast.LENGTH_SHORT).show();
			return;
		}
		account = accountEditText.getText().toString();
		password = passwordEditText.getText().toString();
		
		if (account.equals("")) {
			Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(account)) {
				Toast.makeText(LoginActivity.this, "用户名不能超过20个字符", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (password.equals("")) {
			Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(password)) {
				Toast.makeText(LoginActivity.this, "密码不能超过20个字符", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		final ProgressDialog myDialog = new ProgressDialog(this);
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		myDialog.show();
		
		BmobUser bu2 = new BmobUser();
		bu2.setUsername(account);
		bu2.setPassword(password);
		bu2.login(new SaveListener<BmobUser>() {
		    @Override
		    public void done(BmobUser bmobUser, BmobException e) {
		        if(e==null){
		        	// 不在显示进度条
		        	myDialog.dismiss();

		        	Intent intent = new Intent();
					intent.putExtra("username", account);
					intent.setClass(LoginActivity.this, GuideActivity.class);
					LoginActivity.this.startActivity(intent);
					// 销毁LoginActivity
					LoginActivity.this.finish();
		        }else{
		        	myDialog.dismiss();
		        	System.out.println(e);
		        	Message msg = myHandler.obtainMessage(); 
					msg.obj = "账号不存在或密码错误";
		 			myHandler.sendMessage(msg);
		        }
		    }
		});
	}
	
	
	
	
	// 新用户注册按钮的响应函数
	public void register(View view) {
		System.out.println(" WelcomeActivity register");
		
		// 跳到注册界面
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		this.startActivity(intent);
		// 销毁LoginActivity
		this.finish();
	}
	
	// 暂不登录按钮的响应函数
	public void unLogin(View view) {
		Intent intent = new Intent();
		intent.putExtra("username", account);
		intent.setClass(LoginActivity.this, MusicPlayerActivity.class);
		this.startActivity(intent);
		// 销毁LoginActivity
		LoginActivity.this.finish();
	}

	
	// 消息处理器
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			String data = (String) msg.obj;
			Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
		}
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



