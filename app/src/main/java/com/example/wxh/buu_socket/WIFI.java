package com.example.wxh.buu_socket;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WIFI {
    Socket wifiSocket ;
    public WIFI(){
        wifiSocket = MainActivity.getSocket();
    }
    /*数据缓存区*/
    BlockingQueue<Integer> basket = new ArrayBlockingQueue<Integer>(200);
    public static DataInputStream bInputStream = null;
    public static DataOutputStream bOutputStream =null;
    /*选取有效数据*/
    public static int RecDataArrey[] = new int [10] ;
    //
    public static boolean isdata = false;
    /*开启接受线程*/
    Thread Recthread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            while(true){
                try{
                    if(MainActivity.getSocket() != null) {
                        final byte[] mybyte = new byte[10];
                        bInputStream = new DataInputStream(MainActivity.getSocket().getInputStream());
                        bInputStream.read(mybyte);
                        for (int i = 0; i < 10; i++) {
                            basket.put((mybyte[i] & 0x00ff));
                            Log.d("一级数据", i+"："+(mybyte[i]&0x00ff));
                        }
                        Log.i("一级接收", "正常");
                    }
                }catch (Exception e){
                    Log.e("接收数据",e.getMessage());
                }
            }
        }
    });
    Thread setRecData = new Thread(new Runnable(){
        //根据协议筛选数据 选取有用的标志位
        @Override
        public void run() {
            while(true){
                try{
                    int Datatemp1 = basket.take();
                    if(Datatemp1 == 0x0055){
                        int Datatemp2 = basket.take();
                        if(Datatemp2 == 0x00aa){
                            for(int i = 2;i < 10;i++){
                                RecDataArrey[i] = basket.take();
                            }
                        }
                         isdata =!isdata;
                    }
                }catch (Exception e){
                    Log.e("筛选数据","异常");
                }

            }
        }
    });

       //发送数据
    public static void sendData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0xaa);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }

    //发送数据
    public static void sendSignoData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0x03);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }

    //发送数据
    public static void sendLEDData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0x04);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }

    //发送无线充电数据
    public static void sendWirelessChargingData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0x0a);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }


    //发送交通灯数据
    public static void sendTrafficLightData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0x0e);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }




    public static void sendTFTData(int S,int M1,int M2,int M3){
        final byte [] setbyte = new byte[8];
        int temp = (S + M1 + M2 + M3) % 256;
        setbyte[0] = intToByte(0x55);
        setbyte[1] = intToByte(0x0b);
        setbyte[2] = intToByte(S);
        setbyte[3] = intToByte(M1);
        setbyte[4] = intToByte(M2);
        setbyte[5] = intToByte(M3);
        setbyte[6] = intToByte(temp);
        setbyte[7] = intToByte(0xbb);
        if(MainActivity.getSocket() != null){
            MainActivity.executorServicetor.execute(new Runnable() {  //开启线程，传输数据
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(setbyte, 0, setbyte.length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.e("send","发送失败");
        }
    }



//转换字符发送

    private static byte[] bytesend(byte[] sbyte) {
        byte[] textbyte = new byte[sbyte.length + 5];
        textbyte[0] = (byte) 0xFD;
        textbyte[1] = (byte) (((sbyte.length + 2) >> 8) & 0xff);
        textbyte[2] = (byte) ((sbyte.length + 2) & 0xff);
        textbyte[3] = 0x01;// 合成语音命令
        textbyte[4] = (byte) 0x01;// 编码格式
        for (int i = 0; i < sbyte.length; i++) {
            textbyte[i + 5] = sbyte[i];
        }
        return textbyte;
    }


    public static void sendVoiceData(String str){

        try{
           final byte[]sbyte = bytesend(str.getBytes("GBK"));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        if (MainActivity.getSocket() != null && !MainActivity.getSocket().isClosed()) {
                            if(bOutputStream == null)
                                bOutputStream = new DataOutputStream(MainActivity.getSocket().getOutputStream());
                            bOutputStream.write(sbyte, 0, sbyte .length);
                            Log.e("send","发送成功");
                            bOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

     //      WIFI.send_voice(sbyte);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    //int转byte
    public static byte intToByte(int value){
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xff);
        src[2] = (byte) ((value >> 16) & 0xff);
        src[1] = (byte) ((value >> 8) & 0xff);
        src[0] = (byte) (value & 0xff);
        return src[0];
    }

}
