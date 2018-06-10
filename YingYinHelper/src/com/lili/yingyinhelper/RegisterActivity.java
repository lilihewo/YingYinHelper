package com.lili.yingyinhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.lili.tables.MyUser;


// 注册界面
public class RegisterActivity extends Activity {
	
	private EditText accountEditText; // 
	private EditText passwordEditText; // 
	private EditText emailEditText;
	
	private MyHandler myHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		myHandler = new MyHandler();
		
		accountEditText = (EditText) findViewById(R.id.register_account);
		passwordEditText = (EditText) findViewById(R.id.register_password);
		emailEditText = (EditText) findViewById(R.id.email);
	}
	
	String userName = "";
	String password = "";
	String email = "";
	public void register(View view) {
		userName = accountEditText.getText().toString(); 
		password = passwordEditText.getText().toString();
		email = emailEditText.getText().toString();
		if (userName.equals("")) {
			Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}	else {
			if (isTooLong(userName)) {
				Toast.makeText(RegisterActivity.this, "用户名不能超过20个字符", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (password.equals("")) {
			Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(password)) {
				Toast.makeText(RegisterActivity.this, "密码不能超过20个字符", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		// 邮箱
		if (email.equals("")) {
			Toast.makeText(RegisterActivity.this, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(email)) {
				Toast.makeText(RegisterActivity.this, "邮箱地址不能超过20个字符", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		
		BmobUser bu = new BmobUser();
		bu.setUsername(userName);
		bu.setPassword(password);
		bu.setEmail(email);
		//注意：不能用save方法进行注册
		bu.signUp(new SaveListener<MyUser>() {
		    @Override
		    public void done(MyUser s, BmobException e) {
		        if(e==null){
		            System.out.println("注册成功!"+s.toString());
		            
		            Message msg = myHandler.obtainMessage(); 
					msg.obj = "注册成功!";
		 			myHandler.sendMessage(msg);
		 			
		 			// 切换到登录界面
		 			Intent intent = new Intent();
	        		intent.setClass(RegisterActivity.this, LoginActivity.class);
	        		RegisterActivity.this.startActivity(intent);
	        		// 销毁注册界面
	        		RegisterActivity.this.finish();
		        }else{
		            System.out.println(e);
		            
		            Message msg = myHandler.obtainMessage(); 
					msg.obj = "账号已存在";
		 			myHandler.sendMessage(msg);
		        }
		    }
		});
	}
	
	
	// 判断字符串是否超过指定长度
	private boolean isTooLong(String text) {
		boolean isTooLong = false;
		
		if (text.length() > 20) {
			isTooLong = true;
		} 
		
		return isTooLong;
	}
	
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			String data = (String) msg.obj;
			Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
