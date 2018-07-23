package com.example.wxh.buu_socket;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_con;
    Button button_go;
    Button button_back;
    Button button_left;
    Button button_right;
    Button button_stop;
    Button button_bee;
    Button button_light;
    Button button_find_way;
    Button button_light_up;
    Button button_alarm;
    Button buttonlight_left_right;
    Button jump_to;
    Button button_display;
    Button button_signo;
    Button button_auto;
    Button button_LED;
    Button button_distance;
    TextView textViewId;
    TextView textViewPort;
    EditText editText;
    EditText editText2;
    int beeflage=0x01;
    int lightflage=0x01;
    int light_lr_flage = 0x01;
    int signo_flage = 0x01;
    //全自动化声明类
    connect_socket connect_socket;

    //声明控制类
    car_control carControl = new car_control();
    //
    boolean WIFI_connect_flag = false;
    // i don't known what is it.
    static ExecutorService executorServicetor = Executors.newCachedThreadPool();
    // 服务器管理器
    private DhcpInfo dhcpInfo;

    //注册广播和初始化摄像头
    static String com_broadcase = "com.bkrc.buu";
    CameraCommandUtil cameracommand;
    String cameraAddr;


    /*建立socket连接*/
    static Socket socket = null;
    public Handler getHandler(){
        return handler;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                textViewId.setText("IP:"+getlocalIP());
                textViewPort.setText("端口:60000");
            }else if(msg.what == 2){
                editText.setText(" 0x55 0xaa "+WIFI.RecDataArrey[2]+" "+WIFI.RecDataArrey[3]+" "+WIFI.RecDataArrey[4]
                        +" "+WIFI.RecDataArrey[5]+" "+WIFI.RecDataArrey[6]+" "+WIFI.RecDataArrey[7] +" "+WIFI.RecDataArrey[8]
                        +" "+WIFI.RecDataArrey[9]);
                int b = WIFI.RecDataArrey[9]*256+WIFI.RecDataArrey[8];
                String st = ""+b;
                editText2.setText(st);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button 实例化对象
        button_con = (Button)findViewById(R.id.buttonconnect);
        button_go = (Button)findViewById(R.id.buttongo);
        button_back = (Button)findViewById(R.id.buttonback);
        button_left = (Button)findViewById(R.id.buttonleft);
        button_right = (Button)findViewById(R.id.buttonright);
        button_stop = (Button)findViewById(R.id.buttonstop);
        button_bee = (Button)findViewById(R.id.buttonbee);
        button_light = (Button)findViewById(R.id.buttonlight);
        button_find_way = (Button)findViewById(R.id.buttonfindway);
        button_light_up = (Button)findViewById(R.id.buttonlightup);
        button_alarm = (Button) findViewById(R.id.buttonalarm);
        buttonlight_left_right =  (Button) findViewById(R.id.buttonlight_left_right);
        textViewId = (TextView) findViewById(R.id.textViewid);
        textViewPort =(TextView) findViewById(R.id.textViewport);
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        jump_to = (Button) findViewById(R.id.jump_to);
        button_signo = (Button) findViewById(R.id.buttonsigno);
        button_display = (Button) findViewById(R.id.buttondisplay);
        button_auto=(Button)findViewById(R.id.buttonauto);
        button_LED = (Button)findViewById(R.id.buttonLED);
        button_distance = (Button)findViewById(R.id.buttondistance);

        button_con.setOnClickListener(this);
        button_go.setOnClickListener(this);
        button_back.setOnClickListener(this);
        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);
        button_stop.setOnClickListener(this);
        button_bee.setOnClickListener(this);
        button_light.setOnClickListener(this);
        button_find_way.setOnClickListener(this);
        button_light_up.setOnClickListener(this);
        button_alarm.setOnClickListener(this);
        buttonlight_left_right.setOnClickListener(this);
        jump_to.setOnClickListener(this);
        button_signo.setOnClickListener(this);
        button_display.setOnClickListener(this);
        button_auto.setOnClickListener(this);
        button_LED.setOnClickListener(this);
        button_distance.setOnClickListener(this);

        //
        WIFI wifi = new WIFI();
        wifi.Recthread.start();
        wifi.setRecData.start();
        Eidtthread.start();

        //实例化类
        connect_socket = new connect_socket();
        connect_socket.full_auto.start();
    }
    //得到IP地址
    private String getlocalIP(){
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //没有注册WIFI权限 会有红色波浪线提示
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();
        return Formatter.formatIpAddress(dhcpInfo.gateway);
    }
    //得到socket
    public static Socket getSocket(){
        return socket;
    }
    //建立Socket连接
    Thread WIFIthread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            try{
                if(socket == null){
                    Log.e("IP",getlocalIP());
                    socket = new Socket(getlocalIP(),60000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Log.e("IP",getlocalIP());
                    if( socket == null){
                        Log.e("Socket","空空如也");
                    }
                }
            }catch (Exception e){
                //未注册 权限 Socket连接异常
                Log.e("Socket",e.getMessage());
            }
        }
    });
    public void connect_OnClick(){

        if(WIFI_connect_flag == false){
            if(socket == null){
                WIFIthread.start();
                WIFI_connect_flag = true;
                button_con.setText("已连接");
            }
        }else{
            if(socket != null){
                try{
                    socket.close();
                    socket = null;
                    WIFI_connect_flag = false;
                    button_con.setText("已断开");
                }catch (Exception e){

                }
            }
        }
    }
    //
    Thread Eidtthread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                try{
                    Thread.sleep(200);
                }catch (Exception e){}
                if(WIFI.isdata == true){
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }
    });




    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonconnect:connect_OnClick();break;
            case R.id.buttongo:
                carControl.car_go();
                Toast.makeText(MainActivity.this,"Go",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonback:
                carControl.car_back();
                Toast.makeText(MainActivity.this,"Back",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonleft:
                carControl.car_left();
                Toast.makeText(MainActivity.this,"Left",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonright:
                carControl.car_right();
                Toast.makeText(MainActivity.this,"Right",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonstop:
                carControl.car_stop();
                Toast.makeText(MainActivity.this,"Stop",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonbee:
                carControl.bee();
                Toast.makeText(MainActivity.this,"Bee",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonlight:
                carControl.light();
                Toast.makeText(MainActivity.this,"Light",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonfindway:
                carControl.findway();
                Toast.makeText(MainActivity.this,"寻迹",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonlightup:
                carControl.lightup();
                Toast.makeText(MainActivity.this,"灯源升档",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonlight_left_right:
                carControl.light_left_right();
                Toast.makeText(MainActivity.this,"开单灯",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonalarm:
                carControl.alarm();
                Toast.makeText(MainActivity.this,"buttondisplay",Toast.LENGTH_LONG).show();
            break;
            case R.id.buttondisplay:
                carControl.display();
                Toast.makeText(MainActivity.this,"立体显示",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonsigno:
                carControl.signo();
                Toast.makeText(MainActivity.this,"闸道",Toast.LENGTH_LONG).show();
                break;
            case R.id.jump_to:
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.buttonLED:
                carControl.LED();
                Toast.makeText(MainActivity.this,"buttonLED",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttondistance:
                String a=carControl.distance();
                Toast.makeText(MainActivity.this,"Distance:<<"+a+"   <<",Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonauto:
                connect_socket.auto_flag=10;

            default:break;
        }
    }

    }



