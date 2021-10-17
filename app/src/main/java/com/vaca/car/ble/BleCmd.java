package com.vaca.car.ble;



import static com.vaca.car.utils.CRCUtils.calCRC8;

import java.util.Date;

public class BleCmd {

    public static int CMD_RESET = 0xf0;
    public static int CMD_WIFI = 0xf1;


    public static byte[] reset() {
        int len = 0;
        byte[] cmd = new byte[5 + len];
        cmd[0] = (byte) 0xA5;
        cmd[1] = (byte) CMD_RESET;
        cmd[2] = (byte) ~CMD_RESET;
        cmd[3] = (byte) 0;
        cmd[4] = calCRC8(cmd);
        return cmd;
    }


    public static byte[] setWifi() {
        int len = 0;
        byte[] cmd = new byte[5 + len];
        cmd[0] = (byte) 0xA5;
        cmd[1] = (byte) CMD_WIFI;
        cmd[2] = (byte) ~CMD_WIFI;
        cmd[3] = (byte) 0;
        cmd[4] = calCRC8(cmd);
        return cmd;
    }


}
