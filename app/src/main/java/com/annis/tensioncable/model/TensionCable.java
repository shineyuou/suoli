package com.annis.tensioncable.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TensionCable implements Parcelable {
    /**
     * 编号(名字)
     */
    String name;
    /**
     * 长度
     */
    double length;
    /**
     * 横截面积
     */
    double CSA;
    /**
     * 密度
     */
    double density;
    boolean isClicked;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public TensionCable(String name, double length, double CSA, double density) {
        this.name = name;
        this.length = length;
        this.CSA = CSA;
        this.density = density;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getCSA() {
        return CSA;
    }

    public void setCSA(double CSA) {
        this.CSA = CSA;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.length);
        dest.writeDouble(this.CSA);
        dest.writeDouble(this.density);
        dest.writeByte(this.isClicked ? (byte) 1 : (byte) 0);
    }

    protected TensionCable(Parcel in) {
        this.name = in.readString();
        this.length = in.readDouble();
        this.CSA = in.readDouble();
        this.density = in.readDouble();
        this.isClicked = in.readByte() != 0;
    }

    public static final Creator<TensionCable> CREATOR = new Creator<TensionCable>() {
        @Override
        public TensionCable createFromParcel(Parcel source) {
            return new TensionCable(source);
        }

        @Override
        public TensionCable[] newArray(int size) {
            return new TensionCable[size];
        }
    };
}
