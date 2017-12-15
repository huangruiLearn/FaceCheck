package com.example.facecheckdemo.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.opencv.android.JavaCameraView;
import org.opencv.samples.facedetect.FdActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.facecheckdemo.R;
import com.example.facecheckdemo.bean.Add;
import com.example.facecheckdemo.bean.Verify;
import com.example.facecheckdemo.utils.Constants;
import com.example.facecheckdemo.utils.FileUtil;
import com.example.facecheckdemo.utils.HttpClientUtil;
import com.google.gson.Gson;






public class CameraActivity extends FdActivity  {

	private ProgressDialog pDialog;
  	private DialogHandler dialogHandler;
  	private String uid="10";
  	private String groupid="10";
  	private String money;
  	private MainHandler mMainHandler = null;
  	private String result;
	@Override
	protected void initLogic() {
	    mMainHandler=new MainHandler();
        ((JavaCameraView)mOpenCvCameraView).setHandler(mMainHandler,idTAG);
        findViewById(R.id.takeBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				((JavaCameraView)mOpenCvCameraView).takePicture();
				
			}
		});
       dialogHandler=new DialogHandler();
	   pDialog=new ProgressDialog(CameraActivity.this);
	   pDialog.setCancelable(false);
	   idTAG=getIntent().getIntExtra("id", 0);
	   mMainHandler = new MainHandler();
	   mMainHandler=new MainHandler();
        ((JavaCameraView)mOpenCvCameraView).setHandler(mMainHandler,idTAG);
        findViewById(R.id.takeBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((JavaCameraView)mOpenCvCameraView).takePicture();
			}
		});
	}
	
	
	
	
	 /**
     * 判断来自注册或验证
     */
    public static int idTAG = 0;
    
    private boolean isFirst=true;
    public  class MainHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case Constants.UPDATE_FACE_RECT:
			//	Face[] faces = (Face[]) msg.obj;
				//faceView.setFaces(faces);
		//		Log.e("hr", "设置红框"+faceView.isHasRect());
				Log.d("hr","出现人脸框");
			  //如果出现红框（只执行一次）
			//	if (faceView.isHasRect()&&isFirst&&idTAG==1102) {
				//来自验证两秒后拍照
				if (isFirst&&idTAG==1102) {/*
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					takePicture();
					isFirst=false;
				*/
					
				
				
				}
					//延迟两秒拍照
					
				//	alreadyTake=true;
				//	isFirst=false;
				break;
			case 1101:
				stopCamera();
				pDialog.setMessage("正在注册头像,请稍等...");
				pDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						useBaiDuRegist();
					}
				}).start();
				break;
			case 1102:
				stopCamera();
				pDialog.setMessage("正在识别头像,请稍等...");
				pDialog.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
			     	useBaiDuValidate();
						
						
					}
				}).start();
				
		
				break;
				
			case 2000:
				Log.d("hr", "图片保存成功");
				if (idTAG==1102) {
					//仅执行一次
					//if(once){
						idTAG=1102;
						mMainHandler.sendEmptyMessageDelayed(1102, 0);
				//		once=false;
				//	}
				}else if(idTAG==1101){
					idTAG=1101;
				    mMainHandler.sendEmptyMessageDelayed(1101,0);
				}
				break;
			}
			super.handleMessage(msg);
		}

	}   
    
   
    
    
    private String score;
    private String facealive;
    
    //通过两张图片验证是否是一个人脸 
    private void useBaiDuValidate(){
    	String urlPath = "https://aip.baidubce.com/rest/2.0/face/v2/verify?access_token=24.22b9d09548437dfed595b47324af5899.2592000.1501928949.282335-9853527";
    	URL url;
    	try {
    		url = new URL(urlPath);
    		Bitmap bmp = FileUtil.getValidateBitmap();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		// 将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
    		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    		try {
    			baos.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		byte[] buffer = baos.toByteArray();
    		bmp.recycle();
    		System.gc();
    		// 将图片的字节流数据加密成base64字符输出
    		String photo = Base64.encodeToString(buffer, 0, buffer.length,
    				Base64.DEFAULT);
    		HashMap<String, String> map=new HashMap<String, String>();
    		map.put("uid", uid);
    		
    		map.put("group_id", groupid);
    		map.put("image", photo);
    		map.put("ext_fields", "faceliveness");
    		dialogHandler.sendEmptyMessageDelayed(Constants.WAITRESPONSE,0);
    		String str=HttpClientUtil.doPost(urlPath, map);
    		Log.d("hr", "返回的参数"+str);
    		Gson gson=new Gson();
    		Verify person = gson.fromJson(str,Verify.class);
        	Log.d("hr", "解析的json"+person);
        	double max=0;
        	if (!("".equals(person.result))) {
        		//获取最大参数
            	for (Double b : person.result) {
                          max=b;
                         if(max>b){
                        	 max=b;
                         }
        		}
    		}
    	  else{
    		  max=0;
    	  }
        	
        	score=String.valueOf(max);
        	facealive=String.valueOf(person.ext_info.faceliveness);
        	Log.e("hr", "max是"+max);
        	if(max>50){
        		 Log.d("hr", "验证成功");
        		/* if((!TextUtils.isEmpty(String.valueOf(person.ext_info.faceliveness)))&&(person.ext_info.faceliveness>0.499)){
        	    		dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE2,0);
        	    		
        	    		dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,1500);
        	    		
        	    		return;
        	    	}*/
        		 Log.d("hr", "活体值"+person.ext_info.faceliveness);
        		 if (person.ext_info.faceliveness<0.499) {
        			 result="fail";
        			 dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE2,0);
    			}else{
    				result="success";
    				dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATESUCCESS,0);
    				
    			}
        	}else{
        		Log.d("hr", "比对失败");
        		dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATEFAIL,0);
        		result="fail";
        	}	
    	} catch (Exception e) {
    		dialogHandler.sendEmptyMessageDelayed(Constants.CATCHEXCEPTION,0);
    		result="fail";
    		e.printStackTrace();
    	}
//    	dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,3000);
}
    
    
    
    //通过百度接口先注册人脸
    private void useBaiDuRegist(){
    	String urlPath = "https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add?access_token=24.22b9d09548437dfed595b47324af5899.2592000.1501928949.282335-9853527";
    	URL url;
    	try {
    		url = new URL(urlPath);
    		Bitmap bmp = FileUtil.getRegistBitmap();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		// 将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
    		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    		try {
    			baos.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		byte[] buffer = baos.toByteArray();
    		bmp.recycle();
    		System.gc();
    		// 将图片的字节流数据加密成base64字符输出
    		String photo = Base64.encodeToString(buffer, 0, buffer.length,
    				Base64.DEFAULT);
    		HashMap<String, String> map=new HashMap<String, String>();
    		map.put("uid", uid);
    		map.put("user_info", "aaa6");
    		map.put("group_id", groupid);
    		map.put("image", photo);
    		dialogHandler.sendEmptyMessageDelayed(Constants.WAITRESPONSE,0);
    		String str=HttpClientUtil.doPost(urlPath, map);
    		Log.e("hr", "传的参数"+str);
    		Gson gson=new Gson();
    		Add person = gson.fromJson(str,Add.class);
        	Log.d("hr", "解析的json"+person);
        	//获取最大参数
        	if (TextUtils.isEmpty(person.error_msg)){
        		 Log.d("hr", "注册成功");
        		 dialogHandler.sendEmptyMessageDelayed(Constants.REGISTSUCCESS,0);
        	//	returnMessage=person.error_msg;
        	//	result="fail";
    		}else{
    			Log.d("hr", "注册失败");
        		dialogHandler.sendEmptyMessageDelayed(Constants.REGISTFAIL,0);
        		// result="success";
    		}
    	} catch (Exception e) {
    		dialogHandler.sendEmptyMessageDelayed(Constants.CATCHEXCEPTION,0);
    		e.printStackTrace();
    	}
    	dialogHandler.sendEmptyMessageDelayed(Constants.REGISTCOMPLETE,3000);
}
    
    
    private  void stopCamera(){
    	((JavaCameraView)mOpenCvCameraView).stopCamera();    	
    }
    
    private  class DialogHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case Constants.WAITRESPONSE:
				pDialog.setMessage("参数传递成功,等待服务器响应...");
				break;
			case Constants.REGISTSUCCESS:
				pDialog.setMessage("注册成功");
				break;
			case Constants.REGISTFAIL:
				pDialog.setMessage("注册失败:"+"请重新注册");
				break;
			case Constants.REGISTCOMPLETE:
				pDialog.dismiss();
				finish();
				break;
            case Constants.VALIDATESUCCESS:
            	pDialog.setMessage("人脸验证成功");
            	stopCamera();
            	dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,3000);
				break;
            case Constants.VALIDATEFAIL:
            	pDialog.setMessage("人脸相似度过小,请重新检测");
            	dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,3000);
               	break;		
            case Constants.VALIDATCOMPLETE:
        		stopCamera();
            	pDialog.setMessage("人脸验证完成");
            	pDialog.dismiss();
            	Intent intent=new Intent(CameraActivity.this,DealSuccessActivity.class);
            	intent.putExtra("result", result);
            	if (!TextUtils.isEmpty(money)) {
            		intent.putExtra("money", money);
				}
            	intent.putExtra("facealive", facealive);
            	intent.putExtra("score", score);
                startActivity(intent); 
                System.gc();
                finish();
               	break;
            case Constants.VALIDATCOMPLETE2: 	
            	pDialog.setMessage("活体验证失败,请重新检测");
            	dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,3000);
            break;
            case Constants.CATCHEXCEPTION: 	
            	pDialog.setMessage("未检测到人脸,请重新检测");
            	dialogHandler.sendEmptyMessageDelayed(Constants.VALIDATCOMPLETE,3000);
            break;
			}
			super.handleMessage(msg);
		}
    
    
    
    
    
    
    }





    
}
