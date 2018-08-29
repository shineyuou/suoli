package com.annis.tensioncable.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Equipment implements Parcelable {

    String name;

    /**
     * 测量状态 0: 已停止 1.测量中 2.已暂停
     */
    int status = 0;

    public Equipment(String name, int status) {
        this.name = name;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.status);
    }

    protected Equipment(Parcel in) {
        this.name = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<Equipment> CREATOR = new Creator<Equipment>() {
        @Override
        public Equipment createFromParcel(Parcel source) {
            return new Equipment(source);
        }

        @Override
        public Equipment[] newArray(int size) {
            return new Equipment[size];
        }
    };
}
