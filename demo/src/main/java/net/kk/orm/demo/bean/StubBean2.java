package net.kk.orm.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.db.Datas;

@Table(name = Datas.Stub2.TABLE, uri = Datas.Stub2.CONTENT_URI_STRING)
public class StubBean2 implements Parcelable {
    @PrimaryKey
    @Column(value = Datas.Stub2.NAME)
    private int name;
    @Column(value = Datas.Stub2.ADDRESS, defaultValue = "no address")
    private String address;

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StubBean2() {
    }

    public StubBean2(int name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "StubBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.name);
        dest.writeString(this.address);
    }

    protected StubBean2(Parcel in) {
        this.name = in.readInt();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<StubBean2> CREATOR = new Parcelable.Creator<StubBean2>() {
        @Override
        public StubBean2 createFromParcel(Parcel source) {
            return new StubBean2(source);
        }

        @Override
        public StubBean2[] newArray(int size) {
            return new StubBean2[size];
        }
    };
}
