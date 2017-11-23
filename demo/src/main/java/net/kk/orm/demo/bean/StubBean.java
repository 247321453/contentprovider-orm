package net.kk.orm.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.db.Datas;

@Table(name = Datas.Stub.TABLE, uri = Datas.Stub.CONTENT_URI_STRING)
public class StubBean implements Parcelable {
    @PrimaryKey
    @Column(value = Datas.Stub.NAME)
    private String name;
    @Column(value = Datas.Stub.ADDRESS,defaultValue = "n't address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StubBean() {
    }

    public StubBean(String name, String address) {
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
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    protected StubBean(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<StubBean> CREATOR = new Parcelable.Creator<StubBean>() {
        @Override
        public StubBean createFromParcel(Parcel source) {
            return new StubBean(source);
        }

        @Override
        public StubBean[] newArray(int size) {
            return new StubBean[size];
        }
    };
}
