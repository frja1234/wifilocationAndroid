package com.example.wifi.Model;


public class FingerPrint {
    private float x;
    private float y;


    public FingerPrint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FingerPrint() {
        x = 0;
        y = 0;
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
