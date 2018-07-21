package com.example.wxh.buu_socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.bkrcl.control_car_video.camerautil.SearchCameraUtil;

public class SearchipService extends Service {
	public SearchipService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	private String IPcamera =null;
	SearchCameraUtil searchcamerautil;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("SearchipService","SearchipService");
		thread.start();

        /*
        Intent intentFi = new Intent(MainActivity.com_broadcase);
        sendBroadcast(intentFi);
        */
	}
	private Thread thread =new Thread(new Runnable() {
		@Override
		public void run() {
			while(IPcamera ==null||IPcamera.equals(""))
			{
				searchcamerautil =new SearchCameraUtil();
				IPcamera =searchcamerautil.send();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			Log.e("IPcamera=========",IPcamera);
			cameraHandler.sendEmptyMessage(8);

		}
	});

	private Handler cameraHandler =new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what ==8)
			{
				Intent cameraIntent =new Intent();
				cameraIntent.setAction(Main2Activity.com_broadcase);
				cameraIntent.putExtra("IPCAME",IPcamera+":81");
				sendBroadcast(cameraIntent);
			}

		}
	};
}
