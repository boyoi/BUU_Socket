package com.example.wxh.buu_socket;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class car_control {
    int beeflage=0x01;
    int lightflage=0x01;
    int light_lr_flage = 0x01;
    int signo_flage = 0x01;
    int LED_flage = 0x01;
    int WirelessCharging_flage = 0x01;


    public void car_go(){
        WIFI.sendData(0x02,80,20,0);
        while(WIFI.RecDataArrey[2]!=0X03);
    }

    public void car_back(){
        WIFI.sendData(0x03,80,100,0);
        while(WIFI.RecDataArrey[2]!=0X03);
    }

    public void car_left(){
        WIFI.sendData(0x04,90,0,0);
        while(WIFI.RecDataArrey[2]!=0X02);
    }

    public  void car_right(){
        WIFI.sendData(0x05,90,0,0);
        while(WIFI.RecDataArrey[2]!=0X02);
    }

    public  void car_stop(){
        WIFI.sendData(0x01,0,0,0);
    }
    public void bee(){
        WIFI.sendData(0x30,beeflage,0,0);beeflage = (beeflage+0x1)%2;
    }
    public  void light(){
        WIFI.sendData(0X20,lightflage,lightflage,0);
        lightflage = (lightflage+0x1)%2;
        while(WIFI.RecDataArrey[2]!=0X03);
    }
    public void lightup(){
        WIFI.sendData(0X61,0,0,0);
    }
    public void findway(){
        WIFI.sendData(0x06,80,0,0);
        while(WIFI.RecDataArrey[2]!=0X01);
    }
    public void light_left_right(){
        WIFI.sendData(0X20,light_lr_flage,(light_lr_flage+0x1)%2,0);light_lr_flage=(light_lr_flage+0x1)%2;
    }
    public void alarm(){
        Timer timer=new Timer();
        WIFI.sendData(0X10,0x03,0x05,0x14);
        timer.schedule(new TimerTask(){
            public void run(){
                WIFI.sendData(0X11,0x45,0xde,0x92);
                this.cancel();}},200);
        timer.schedule(new TimerTask(){
            public void run(){
                WIFI.sendData(0X12,0,0,0);
                this.cancel();}},400);
        while(WIFI.RecDataArrey[2]!=0X03);
    }

    public void display(){
        Timer timer2=new Timer();
        WIFI.sendData(0X10,0xff,0x13,0x01);
        timer2.schedule(new TimerTask(){
            public void run(){
                WIFI.sendData(0X11,0xff,0x00,0x00);
                this.cancel();}},200);
        timer2.schedule(new TimerTask(){
            public void run(){
                WIFI.sendData(0X12,0,0,0);
                this.cancel();}},400);
        while(WIFI.RecDataArrey[2]!=0X03);
    }

    public void signo(){
        WIFI.sendSignoData(0x01,signo_flage+0x01,0,0);
        signo_flage = (signo_flage+0x01)%2;
        while(WIFI.RecDataArrey[2]!=0X03);
    }

    public void LED(){
        WIFI.sendLEDData(0x03,LED_flage+0x01,0,0);
        LED_flage = (LED_flage+0x01)%2;

    }

    public String distance(){

        int a = WIFI.RecDataArrey[5]*256+WIFI.RecDataArrey[4];
        int b = (int)a/1000;
        int c = (int)(a-a/100)/10;

        WIFI.sendLEDData(0x04,0x00,b,c);
        String st = ""+b+c;
       return st;
    }
    public void wirelesscharging(){
        WIFI.sendSignoData(0x01,WirelessCharging_flage,0,0);
        WirelessCharging_flage = (WirelessCharging_flage+0x01)%2;
    }

    public  void TrafficLight(){
        Timer timer3=new Timer();
                WIFI.sendTrafficLightData(0x01,0x00,0x00,0x00);

        timer3.schedule(new TimerTask(){
            public void run(){
                WIFI.sendTrafficLightData(0x02,0x01,0x00,0x00);
                this.cancel();}},300);
        if(WIFI.RecDataArrey[2]!=0X07){
            WIFI.sendVoiceData("红色灯");
            return;
        }

        timer3.schedule(new TimerTask(){
            public void run(){
                WIFI.sendTrafficLightData(0x02,0x02,0x00,0x00);
                this.cancel();}},600);
        if(WIFI.RecDataArrey[2]!=0X07){
            WIFI.sendVoiceData("绿色灯");
            return;
        }
        timer3.schedule(new TimerTask(){
            public void run(){
                WIFI.sendTrafficLightData(0x02,0x03,0x00,0x00);
                this.cancel();}},900);
        if(WIFI.RecDataArrey[2]!=0X07){
            WIFI.sendVoiceData("黄色灯");
            return;
        }


    }

}
