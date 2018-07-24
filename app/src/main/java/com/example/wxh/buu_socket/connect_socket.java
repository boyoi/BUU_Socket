package com.example.wxh.buu_socket;

import android.util.Log;
import android.widget.Toast;

import com.example.wxh.buu_socket.WIFI;



public class connect_socket {

    car_control control = new car_control();


    public int auto_flag=0;
    Thread full_auto = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true)
            {
                while (auto_flag>5)
                {
                    auto_main();
                }
            }
        }
    });



    public void auto_main()
    {
        switch (auto_flag)
        {
            case 10:
              //  WIFI.sendVoiceData("宇宙起源号启动");
                control.bee();
                delay_ms(1000);
                control.bee();

                delay_ms(2000);
                control.car_go();
                delay_ms(2000);


                control.signo();
                //语音播报

                delay_ms(2000);

                WIFI.sendData(0x042,0x00,0x00,0x00);
                delay_ms(2000);

                WIFI.sendData(0x042,0x00,0x00,0x00);
                delay_ms(2000);
                delay_ms(1000);

                WIFI.sendData(0x044,0x00,0x00,0x00);
                delay_ms(2000);

                control.distance();
                delay_ms(2000);
                control.car_left();

                delay_ms(2000);
                control.car_left();
                //交通灯未写
                delay_ms(2000);
                WIFI.sendData(0x042,0x00,0x00,0x00);

                delay_ms(2000);
                WIFI.sendData(0x040,0x00,0x00,0x00);

                delay_ms(2000);
                control.display();
                delay_ms(2000);
                control.display();

                delay_ms(2000);
                control.car_left();
//                WIFI.sendData(0x040,0x00,0x00,0x00);

                delay_ms(2000);
                WIFI.sendData(0x42,0x00,0x00,0x00);

                delay_ms(2000);
                WIFI.sendData(0x042,0x00,0x00,0x00);

                delay_ms(2000);
                control.car_left();


//                WIFI.sendData(0x044,0x00,0x00,0x00);
                delay_ms(2000);
                WIFI.sendData(0x040,0x00,0x00,0x00);

                delay_ms(4000);
                control.alarm();

                delay_ms(2000);
                control.car_left();
                delay_ms(2000);
                control.car_left();
                //红绿灯

                delay_ms(2000);


                delay_ms(2000);
                WIFI.sendData(0x044,0x00,0x00,0x00);



                delay_ms(2000);
                WIFI.sendData(0x042,0x00,0x00,0x00);

                delay_ms(2000);
                WIFI.sendVoiceData("我是一只快乐的小青蛙");

                delay_ms(2000);
                WIFI.sendData(0x042,0x00,0x00,0x00);

                delay_ms(2000);
                control.light();



//
//                Log.e("1111","111");
//
//                control.car_right();
//
//                WIFI.sendData(0x044,0x00,0x00,0x00);
//                while(WIFI.RecDataArrey[2]!=0X02);
//
//
//                Log.e("1111","111");
//                control.findway();
//                Log.e("1111","111");
//
//                WIFI.sendData(0x040,0x00,0x00,0x00);
//                while(WIFI.RecDataArrey[2]!=0X02);
//                Log.e("1111","111");
//                control.alarm();
//
//                WIFI.sendData(0x041,0x00,0x00,0x00);
//                while(WIFI.RecDataArrey[2]!=0X02);
//
//
//                WIFI.sendData(0x043,0x00,0x00,0x00);
//                while(WIFI.RecDataArrey[2]!=0X02);
//
//                control.display();




//
//
//                delay_ms(100);
//                control.signo();
//
//                control.findway();
//                control.car_go();
//
//                control.findway();
//                control.car_go();
//
//                control.findway();
//
//                control.car_go();
//                control.wirelesscharging();
//                WIFI.sendVoiceData("左转");
//                control.car_left();
//
//
//                control.findway();
//                control.car_go();
//
//
//                control.findway();
//
//                control.car_go();
//                WIFI.sendVoiceData("我还是左转");
//                control.car_left();
//
//                control.findway();
//
//
//                control.car_go();
//                WIFI.sendVoiceData("我依旧左转");
//                control.car_left();
//
//                control.findway();
//
//                control.car_go();
//                WIFI.sendVoiceData("哎，我右转了");
//                control.car_right();
//
//                control.findway();

                auto_flag=20;
                break;
            case 20:
                auto_flag=0;
                break;
        }
    }

    private void delay_ms(int ms)
    {
        try {
            Thread.sleep(ms);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
