package com.example.wifi.Model;

public class WifiFlag {

    private WifiPoint ap1,ap2,ap3,ap4;

    private FingerPrint print;


    public FingerPrint getPrint() {

        return print;
    }

    public void setPrint(FingerPrint print) {

        this.print = print;
    }

    public WifiPoint getAp4() {

        return ap4;
    }

    public void setAp4(WifiPoint ap4) {

        this.ap4 = ap4;
    }

    public WifiPoint getAp3() {

        return ap3;
    }

    public void setAp3(WifiPoint ap3) {

        this.ap3 = ap3;
    }

    public WifiPoint getAp2() {

        return ap2;
    }

    public void setAp2(WifiPoint ap2) {

        this.ap2 = ap2;
    }

    public WifiPoint getAp1() {

        return ap1;
    }

    public void setAp1(WifiPoint ap1) {

        this.ap1 = ap1;
    }
    @Override
    public String toString() {

        return "WifiFlag{" +
                "ap1=" + ap1 +
                ", ap2=" + ap2 +
                ", ap3=" + ap3 +
                ", ap4=" + ap4 +
                ", print=" + print +
                '}';
    }
}
