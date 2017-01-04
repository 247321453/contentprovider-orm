package net.kk.orm.demo.bean;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.PrimaryKey;
import net.kk.orm.annotations.Table;
import net.kk.orm.demo.db.Datas;

@Table(name = Datas.Stub.TABLE, uri = Datas.Stub.CONTENT_URI_STRING)
public class StubBean {
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

    @Override
    public String toString() {
        return "StubBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
