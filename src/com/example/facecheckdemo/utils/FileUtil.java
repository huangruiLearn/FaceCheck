package com.example.facecheckdemo.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static   String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamera";

	/**��ʼ������·��
	 * @return
	 */
	private static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		return storagePath;
	}

	/**����Bitmap��sdcard
	 * @param b
	 * @return 
	 */
	public static boolean saveBitmap(Bitmap b,int id){
		String jpegName ;
		String path = initPath();
		if (id==1101) {
			Log.d("hr", "保存注册头像");
			 jpegName = path + "/" + "regist" +".jpg";
		}/*else if(){
			 jpegName = path + "/" + "validate" +".jpg";
		}*/else{
			jpegName = path + "/" + "validate" +".jpg";
		}
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}}
	
	
	
	public static Bitmap getRegistBitmap()  
	{  
	    Bitmap bitmap = null;  
	    try  
	    {  
	        File file = new File(initPath()+"/regist.jpg");  
	        if(file.exists())  
	        {  
	        	
	            bitmap = BitmapFactory.decodeFile(initPath()+"/regist.jpg");  
	            Log.e("hr", "得到nitmap");
	        }  
	    } catch (Exception e)  
	    {  
	        // TODO: handle exception  
	    }  
	      
	      
	    return bitmap;  
	} 

	public static Bitmap getValidateBitmap()  
	{  
	    Bitmap bitmap = null;  
	    try  
	    {  
	        File file = new File(initPath()+"/validate.jpg");  
	        if(file.exists())  
	        {  
	        	
	            bitmap = BitmapFactory.decodeFile(initPath()+"/validate.jpg");  
	            Log.e("hr", "得到validatenitmap");
	        }  
	    } catch (Exception e)  
	    {  
	        // TODO: handle exception  
	    }  
	      
	      
	    return bitmap;  
	} 
	
	

}
