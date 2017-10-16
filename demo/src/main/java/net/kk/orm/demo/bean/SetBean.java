package net.kk.orm.demo.bean;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.ColumnConvert;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.db.Datas;
import net.kk.orm.demo.sec.SecConvert;

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
    private List<StubBean> mStubBeans;

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

    @Override
    public String toString() {
        return "SetBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + Arrays.toString(users) +
                ", mStubBean=" + mStubBean +
                ", mStubBeans=" + mStubBeans +
                ", testAdd='" + testAdd + '\'' +
                ", testAdd2=" + testAdd2 +
                '}';
    }
}
