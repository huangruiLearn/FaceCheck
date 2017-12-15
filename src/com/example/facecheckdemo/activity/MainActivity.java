package com.example.facecheckdemo.activity;

import org.opencv.samples.facedetect.FdActivity;

import com.example.facecheckdemo.R;
import com.example.facecheckdemo.R.id;
import com.example.facecheckdemo.R.layout;
import com.example.facecheckdemo.utils.NetworkUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	
	private RelativeLayout my_regist,my_validate;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        my_regist=(RelativeLayout)findViewById(R.id.my_regist);
		my_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (!(NetworkUtils.isConnected(MainActivity.this))) {
					Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_LONG).show();
					return;
				}
				//注册
				Intent intent=new Intent(MainActivity.this,FdActivity.class);
				intent.putExtra("id", 1101);
			    startActivity(intent);
				
			}
		});
		
		
		
		my_validate=(RelativeLayout)findViewById(R.id.my_validate);
		my_validate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (!(NetworkUtils.isConnected(MainActivity.this))) {
					Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_LONG).show();
					return;
				}
				//注册
				Intent intent=new Intent(MainActivity.this,FdActivity.class);
				intent.putExtra("id", 1102);
			    startActivity(intent);
				
			}
		});
		
		
    }

    
}
