package com.novice.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class AidlBookData implements Parcelable {

    public static final Creator<AidlBookData> CREATOR = new Creator<AidlBookData>() {
        @Override
        public AidlBookData createFromParcel(Parcel in) {
            return new AidlBookData(in);
        }

        @Override
        public AidlBookData[] newArray(int size) {
            return new AidlBookData[size];
        }
    };

    private int price;
    private String name;

    public AidlBookData(int aPrice,String aName){
        price = aPrice;
        name = aName;
    }
    protected AidlBookData(Parcel in) {
        this.price = in.readInt();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.price);
        dest.writeString(this.name);
    }

    /***
     * 这个文档里没有提，但必需有。
     *
     * AIDL 编译报  can be an out parameter, so you must declare it as in, out or inout
     * Aidl文件 中 自定义类型 需要声明数据方向 in out inout
     * in：只能客户端流向服务端
     * out：只能服务端流向客户端
     * @param inParcel
     */
    public void readFromParcel(Parcel inParcel) {
        this.price = inParcel.readInt();
        this.name = inParcel.readString();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name+ ":\t[" +price +"]\t"+getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
