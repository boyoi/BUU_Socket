package com.example.wxh.buu_socket;

import com.example.wxh.buu_socket.WIFI;

public class connect_socket {

    public void BEEP(boolean i)
    {
        if(i) WIFI.sendData(0x30, 1, 0, 0);
        else WIFI.sendData(0x30, 0, 0, 0);
    }
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
    public void go (int sp ,int mp)
    {
        WIFI.sendData(0x02, sp, mp, 0);
        // delay_ms(1500);
        while (WIFI.RecDataArrey[2]!=0x03);
    }

    public void left(int sp)
    {
        WIFI.sendData(0x04, sp, 00, 0);
        // delay_ms(1500);
        while (WIFI.RecDataArrey[2]!=0x02);
    }

    public void auto_main()
    {
        switch (auto_flag)
        {
            case 10:
                go(60,22);
                left(90);
                BEEP(true);
                delay_ms(500);
                BEEP(false);
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
