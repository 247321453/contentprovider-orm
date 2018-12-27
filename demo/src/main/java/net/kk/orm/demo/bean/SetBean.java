package net.kk.orm.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.ColumnConvert;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.converts.SecConvert;
import net.kk.orm.demo.converts.StubBeen2Convert;
import net.kk.orm.demo.converts.StubBeenConvert;
import net.kk.orm.demo.db.Datas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Table(name = Datas.Set.TABLE, uri = Datas.Set.CONTENT_URI_STRING)
public class SetBean implements Parcelable {
    @PrimaryKey(autoIncrement = true)
    @Column(Datas.Set.ID)
    private long id;
    @ColumnConvert(SecConvert.class)
    @Column(value = Datas.Set.NAME)
    private String name;
    @Column(Datas.Set.USERS)
    private int[] users;
    @Column(Datas.Set.STUB)
    private StubBean mStubBean;
    @Column(Datas.Set.STUBS)
    @ColumnConvert(StubBeenConvert.class)
    private List<StubBean> mStubBeans;
    @Column(Datas.Set.STUBS2)
    @ColumnConvert(StubBeen2Convert.class)
    private List<StubBean2> mStubBeans2;
    //第一次MyContentProvider改为1
    @Column(value = "testAdd", defaultValue = "hello")
    private String testAdd;
    @Column(value = "testAdd2", defaultValue = "1")
    private int testAdd2;

    @Column("stats")
    public boolean status;
    @Column("testFloat")
    public float testFloat;
    @Column("testDouble")
    public double testDouble;

    public long getId() {
        return id;
    }

    public SetBean() {
    }

    public SetBean(long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getUsers() {
        return users;
    }

    public void setUsers(int[] users) {
        this.users = users;
    }

    public StubBean getStubBean() {
        return mStubBean;
    }

    public void setStubBean(StubBean stubBean) {
        mStubBean = stubBean;
    }

    public List<StubBean> getStubBeans() {
        return mStubBeans;
    }

    public void setStubBeans(List<StubBean> stubBeans) {
        mStubBeans = stubBeans;
    }

    public List<StubBean2> getStubBeans2() {
        return mStubBeans2;
    }

    public void setStubBeans2(List<StubBean2> stubBeans2) {
        mStubBeans2 = stubBeans2;
    }

    @Override
    public String toString() {
        return "SetBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + Arrays.toString(users) +
                ", mStubBean=" + mStubBean +
                ", mStubBeans=" + mStubBeans +
                ", mStubBeans2=" + mStubBeans2 +
                ", testAdd='" + testAdd + '\'' +
                ", testAdd2=" + testAdd2 +
                ", status=" + status +
                ", testFloat=" + testFloat +
                ", testDouble=" + testDouble +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeIntArray(this.users);
        dest.writeParcelable(this.mStubBean, flags);
        dest.writeTypedList(this.mStubBeans);
        dest.writeTypedList(this.mStubBeans2);
        dest.writeString(this.testAdd);
        dest.writeInt(this.testAdd2);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
    }

    protected SetBean(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.users = in.createIntArray();
        this.mStubBean = in.readParcelable(StubBean.class.getClassLoader());
        this.mStubBeans = in.createTypedArrayList(StubBean.CREATOR);
        this.mStubBeans2 = in.createTypedArrayList(StubBean2.CREATOR);
        this.testAdd = in.readString();
        this.testAdd2 = in.readInt();
        this.status = in.readByte() != 0;
    }

    public static final Creator<SetBean> CREATOR = new Creator<SetBean>() {
        @Override
        public SetBean createFromParcel(Parcel source) {
            return new SetBean(source);
        }

        @Override
        public SetBean[] newArray(int size) {
            return new SetBean[size];
        }
    };
}
