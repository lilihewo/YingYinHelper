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


// ע�����
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
			Toast.makeText(RegisterActivity.this, "�û�������Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}	else {
			if (isTooLong(userName)) {
				Toast.makeText(RegisterActivity.this, "�û������ܳ���20���ַ�", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (password.equals("")) {
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(password)) {
				Toast.makeText(RegisterActivity.this, "���벻�ܳ���20���ַ�", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		// ����
		if (email.equals("")) {
			Toast.makeText(RegisterActivity.this, "�����ַ����Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (isTooLong(email)) {
				Toast.makeText(RegisterActivity.this, "�����ַ���ܳ���20���ַ�", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		
		BmobUser bu = new BmobUser();
		bu.setUsername(userName);
		bu.setPassword(password);
		bu.setEmail(email);
		//ע�⣺������save��������ע��
		bu.signUp(new SaveListener<MyUser>() {
		    @Override
		    public void done(MyUser s, BmobException e) {
		        if(e==null){
		            System.out.println("ע��ɹ�!"+s.toString());
		            
		            Message msg = myHandler.obtainMessage(); 
					msg.obj = "ע��ɹ�!";
		 			myHandler.sendMessage(msg);
		 			
		 			// �л�����¼����
		 			Intent intent = new Intent();
	        		intent.setClass(RegisterActivity.this, LoginActivity.class);
	        		RegisterActivity.this.startActivity(intent);
	        		// ����ע�����
	        		RegisterActivity.this.finish();
		        }else{
		            System.out.println(e);
		            
		            Message msg = myHandler.obtainMessage(); 
					msg.obj = "�˺��Ѵ���";
		 			myHandler.sendMessage(msg);
		        }
		    }
		});
	}
	
	
	// �ж��ַ����Ƿ񳬹�ָ������
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
