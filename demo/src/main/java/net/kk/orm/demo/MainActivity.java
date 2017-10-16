package net.kk.orm.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.kk.orm.Orm;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;
import net.kk.orm.demo.db.Datas;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Orm mOrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrm = new Orm(this);
        try {
            testInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
创建表，追加字段 ok
数据转换，转义符号，特殊字段名 ok

id自增 ok
无id自增 ok

删一个，删多个 ok

改一个，改多个 ok

查，排序，统计数量 ok
    */
    private void testInsert() throws Exception {
        int setCount = mOrm.select(SetBean.class).count();
        Log.i("orm", "setCount=" + setCount);
        if (setCount == 0) {
            Log.i("orm", "add set:" + addSet());
        } else {
            //倒序
            SetBean setBean = mOrm.select(SetBean.class).orderBy(Datas.Set.ID, true).findFirst();
            Log.i("orm", "save setBean:" + setBean);
        }
        //根据加密字段查询
        SetBean setBean2 = mOrm.select(SetBean.class)
                .where(Datas.Set.NAME, "=", "hello")
                .findFirst();
        Log.i("orm", "get setBean:" + setBean2);
        Log.i("orm", "get stubs:" +mOrm.select(StubBean.class).findAll());
    }

    private void testUpdate() throws Exception {
        SetBean setBean = mOrm.select(SetBean.class).orderBy(Datas.Set.ID, true).findFirst();
        setBean.setName("no2");
        Log.i("orm", "update set :" + mOrm.update(setBean));
        StubBean stubBean = new StubBean();
        stubBean.setAddress("223");
        Log.i("orm", "update stub :" + mOrm.update(stubBean, mOrm.where(StubBean.class)));
    }

    private void testDelete() throws Exception {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub3");
        Log.i("orm", "delete stub :" + mOrm.delete(stubBean));
    }

    private long addSet() throws Exception {
        SetBean setBean = new SetBean();
        setBean.setId(111);
        setBean.setName("hello");
        setBean.setUsers(new int[]{1, 2, 3});
        StubBean stubBean = new StubBean();
        stubBean.setName("stub");
        stubBean.setAddress("hello");
        setBean.setStubBean(stubBean);

        List<StubBean> stubBeen = new ArrayList<>();
        stubBeen.add(new StubBean("1", "1"));
        stubBeen.add(new StubBean("stub", "hello"));
        setBean.setStubBeans(stubBeen);
        return mOrm.insert(setBean);
    }

    private long addStub1() throws Exception {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }

    private long addStub2() throws Exception {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub2");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }

    private long addStub3() throws Exception {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub3");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }
}
