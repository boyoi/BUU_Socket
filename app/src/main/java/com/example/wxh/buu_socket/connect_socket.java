package com.example.wxh.buu_socket;

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
//                delay_ms(500);
                WIFI.sendVoiceData("吴祉璇大佬");
                control.car_go();

                control.findway();
                control.car_go();

                control.findway();
                control.car_go();

                control.findway();

                control.car_go();
                WIFI.sendVoiceData("左转");
                control.car_left();


                control.findway();
                control.car_go();


                control.findway();

                control.car_go();
                WIFI.sendVoiceData("我还是左转");
                control.car_left();

                control.findway();


                control.car_go();
                WIFI.sendVoiceData("我依旧左转");
                control.car_left();

                control.findway();

                control.car_go();
                WIFI.sendVoiceData("哎，我右转了");
                control.car_right();

                control.findway();

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
