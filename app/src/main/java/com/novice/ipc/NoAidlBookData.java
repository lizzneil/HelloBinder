package com.novice.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 不用AIDL进使用的parcel
 */
public class NoAidlBookData implements Parcelable {

    private int price;
    private String name;

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.price);
        dest.writeString(this.name);
    }

    public NoAidlBookData() {
    }

    protected NoAidlBookData(Parcel in) {
        this.price = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<NoAidlBookData> CREATOR = new Creator<NoAidlBookData>() {
        @Override
        public NoAidlBookData createFromParcel(Parcel source) {
            return new NoAidlBookData(source);
        }

        @Override
        public NoAidlBookData[] newArray(int size) {
            return new NoAidlBookData[size];
        }
    };

    @Override
    public String toString() {
        return "Book{" +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}
