package net.kk.orm.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBean implements Parcelable {
    private String name;
    private int i;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.i);
    }

    public ParcelableBean() {
    }

    protected ParcelableBean(Parcel in) {
        this.name = in.readString();
        this.i = in.readInt();
    }

    public static final Parcelable.Creator<ParcelableBean> CREATOR = new Parcelable.Creator<ParcelableBean>() {
        @Override
        public ParcelableBean createFromParcel(Parcel source) {
            return new ParcelableBean(source);
        }

        @Override
        public ParcelableBean[] newArray(int size) {
            return new ParcelableBean[size];
        }
    };
}
