package com.fzs.hzy.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    private String name;
    private int age;
    private List<Book> list;

    public User(String name) {
        this.name = name;
    }

    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
        list = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeTypedList(list);
    }
}
