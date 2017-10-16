package net.kk.orm.demo.bean;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.ColumnConvert;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.converts.SecConvert;
import net.kk.orm.demo.converts.StubBeen2Convert;
import net.kk.orm.demo.converts.StubBeenConvert;
import net.kk.orm.demo.db.Datas;

import java.util.Arrays;
import java.util.List;

@Table(name = Datas.Set.TABLE, uri = Datas.Set.CONTENT_URI_STRING)
public class SetBean {
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
                '}';
    }
}
