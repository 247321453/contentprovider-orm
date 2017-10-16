package net.kk.orm.demo.bean;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.db.Datas;

@Table(name = Datas.Stub2.TABLE, uri = Datas.Stub2.CONTENT_URI_STRING)
public class StubBean2 {
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
}
