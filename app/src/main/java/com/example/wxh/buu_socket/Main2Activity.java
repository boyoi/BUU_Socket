package com.example.wxh.buu_socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main2Activity extends AppCompatActivity  implements View.OnClickListener{

    Button camera_top, camera_left, camera_bottom, camera_right,button_qrcode,qr_code;


    ImageView pic;


    EditText editText;


    boolean WIFI_connect_flag = false;

    //注册广播和初始化摄像头
    static String com_broadcase = "com.bkrc.buu";
    CameraCommandUtil cameracommand;
    String cameraAddr;

    //初始化摄像头相关设置

    // i don't known what is it.
    static ExecutorService executorServicetor = Executors.newCachedThreadPool();
    // 服务器管理器
    private DhcpInfo dhcpInfo;
    /*建立socket连接*/
    static Socket socket = null;

    public Handler getHandler() {
        return handler;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("1111","111");
//                textViewId.setText("IP:" + getlocalIP());
//                textViewPort.setText("端口:60000");
            } else if (msg.what == 2) {
                Log.e("111","1111");
//                editText.setText(" 0x55 0xaa " + WIFI.RecDataArrey[2] + " " + WIFI.RecDataArrey[3] + " " + WIFI.RecDataArrey[4]
//                        + " " + WIFI.RecDataArrey[5] + " " + WIFI.RecDataArrey[6] + " " + WIFI.RecDataArrey[7] + " " + WIFI.RecDataArrey[8]
//                        + " " + WIFI.RecDataArrey[9]);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initview();//初始化控件

        camerainit(); //注册广播与初始化摄像头

        //在清单文件里面注册服务，才能启动摄像头

    }

    void initview() {

        //button 实例化对象
        qr_code = (Button) findViewById(R.id.qr_code);
        editText = (EditText) findViewById(R.id.editText);
        pic = findViewById(R.id.camera);
        camera_bottom = findViewById(R.id.camera_bottom);
        camera_left = findViewById(R.id.camera_left);
        camera_right = findViewById(R.id.camera_right);
        camera_top = findViewById(R.id.camera_top);
        button_qrcode = findViewById(R.id.btn_qrcode);
        //控制



        //摄像头
        camera_top.setOnClickListener(this);
        camera_left.setOnClickListener(this);
        camera_right.setOnClickListener(this);
        camera_bottom.setOnClickListener(this);

        //二维码
        button_qrcode.setOnClickListener(this);
        qr_code.setOnClickListener(this);

        //
        WIFI wifi = new WIFI();
        wifi.Recthread.start();
        wifi.setRecData.start();
        Eidtthread.start();
    }

    /**
     * 启动服务，获取摄像头ip
     */
    private void searchcameraip() {
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this, SearchipService.class);
        startService(intent);
    }

    //得到IP地址
    private String getlocalIP() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //没有注册WIFI权限 会有红色波浪线提示
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();
        return Formatter.formatIpAddress(dhcpInfo.gateway);
    }

    /**
     * 注册广播和初始化摄像头
     */
    private void camerainit() {
        IntentFilter intentFilter = new IntentFilter();//意图过滤器，用来传递广播BroadCast。
        intentFilter.addAction(com_broadcase);//自定义广播
        registerReceiver(myBroadcastReceiver, intentFilter);//注册广播接收器
        cameracommand = new CameraCommandUtil();
    }

    //广播
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.e("BroadcastReceiver","BroadcastReceiver------");
            cameraAddr = intent.getStringExtra("IPCAME");
            Log.e("BroadcastReceiver", "------" + cameraAddr);
            phtgread.start();
        }
    };

    private Thread phtgread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.e("开始显示", "图片tu1");
            while (true) {
                getBitmap();
            }
        }
    });

    static Bitmap bitmap;
    /**
     * 获取摄像头图片帧
     */
    void getBitmap() {
        bitmap = cameracommand.httpForImage(cameraAddr);
        cameHandler.sendEmptyMessage(7);
    }


    /**
     * 摄像头handle
     */
    byte[] mmbyte = new byte[12];
    int light;
    String sk;
    private Handler cameHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 7) {
                pic.setImageBitmap(bitmap);
            }
            if (msg.what == 5) {
                mmbyte = (byte[]) msg.obj;
            }
        }
    };


    //得到socket
    public static Socket getSocket() {
        return socket;
    }

    //建立Socket连接
    Thread WIFIthread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                if (socket == null) {
                    Log.e("IP", getlocalIP());
                    socket = new Socket(getlocalIP(), 60000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Log.e("IP", getlocalIP());
                    if (socket == null) {
                        Log.e("Socket", "空空如也");
                    }
                }
            } catch (Exception e) {
                //未注册 权限 Socket连接异常
                Log.e("Socket", e.getMessage());
            }
        }
    });



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.camera_top:
            Toast.makeText(Main2Activity.this, "上边", Toast.LENGTH_SHORT).show();
            top();
            break;
            case R.id.camera_bottom:
                Toast.makeText(Main2Activity.this, "下边", Toast.LENGTH_SHORT).show();
                bottom();
                break;
            case R.id.camera_left:
                Toast.makeText(Main2Activity.this, "左边", Toast.LENGTH_SHORT).show();
                cameleft();
                break;
            case R.id.camera_right:
                Toast.makeText(Main2Activity.this, "右边", Toast.LENGTH_SHORT).show();
                cameright();
                break;
            case R.id.btn_qrcode:
                connect_OnClick();
                break;
            case R.id.qr_code:
                qrHandler.sendEmptyMessage(10);
                break;
            default:
                break;
        }

    }
    String result_qr;
    private Timer timer;

    Handler qrHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.e("qrcode","run");
                            Result result = null;
                            RGBLuminanceSource rSource = new RGBLuminanceSource(
                                    bitmap);
                            try {
                                BinaryBitmap binaryBitmap = new BinaryBitmap(
                                        new HybridBinarizer(rSource));
                                Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
                                hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
                                QRCodeReader reader = new QRCodeReader();
                                result = reader.decode(binaryBitmap, hint);
                                if (result.toString() != null) {
                                    result_qr = result.toString();
                                    timer.cancel();
                                    qrHandler.sendEmptyMessage(20);
                                }
                                System.out.println("正在识别");
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            } catch (ChecksumException e) {
                                e.printStackTrace();
                            } catch (FormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 200);
                    break;
                case 20:
                    Toast.makeText(Main2Activity.this, result_qr, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        };
    };

    public void connect_OnClick() {

        if (WIFI_connect_flag == false) {
            if (socket == null) {
                searchcameraip();//启动服务，获取摄像头ip
                WIFIthread.start();
                WIFI_connect_flag = true;
            }
        } else {
            if (socket != null) {
                try {
                    socket.close();
                    socket = null;
                    WIFI_connect_flag = false;
                } catch (Exception e) {

                }
            }
        }
    }

    Thread Eidtthread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
                if (WIFI.isdata == true) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }
    });



    private void cameright() {
        XcApplication.executorServicetor.execute(new Runnable() {
            @Override
            public void run() {
                cameracommand.postHttp(cameraAddr, 6, 1);  //右
            }
        });
    }

    private void cameleft() {
        XcApplication.executorServicetor.execute(new Runnable() {
            @Override
            public void run() {
                cameracommand.postHttp(cameraAddr, 4, 1);  //左
            }
        });
    }

    private void bottom() {
        XcApplication.executorServicetor.execute(new Runnable() {
            @Override
            public void run() {
                cameracommand.postHttp(cameraAddr, 2, 1);  //下
            }
        });
    }

    private void top() {
        XcApplication.executorServicetor.execute(new Runnable() {
            @Override
            public void run() {
                cameracommand.postHttp(cameraAddr, 0, 1);  //上
            }
        });
    }


}
