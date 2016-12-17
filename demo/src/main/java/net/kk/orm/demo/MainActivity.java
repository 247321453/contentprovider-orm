package net.kk.orm.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.kk.orm.Orm;
import net.kk.orm.WhereBuilder;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;
import net.kk.orm.demo.db.Datas;

public class MainActivity extends Activity {
    Orm mOrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrm = new Orm(this);
        new Thread(() -> {
            testInsert();
            testUpdate();
//            testDelete();
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    private void testInsert() {
        int setCount = mOrm.select(SetBean.class).count();
        Log.i("orm", "setCount=" + setCount);
        if (setCount == 0) {
            Log.i("orm", "add set:" + addSet());
        } else {
            //倒序
            SetBean setBean = mOrm.select(SetBean.class).orderBy(Datas.Set.ID, true).findFirst();
            Log.i("orm", "setBean:" + setBean);
        }
        //根据加密字段查询
        SetBean setBean2 = mOrm.select(SetBean.class)
                .where(Datas.Set.NAME, "=", "hello")
                .findFirst();
        Log.i("orm", "setBean2:" + setBean2);
        Log.i("orm", "add stub1:" + addStub1());
        Log.i("orm", "add stub2:" + addStub2());
        Log.i("orm", "add stub3:" + addStub3());
    }

    private void testUpdate() {
        SetBean setBean = mOrm.select(SetBean.class).orderBy(Datas.Set.ID, true).findFirst();
        setBean.setName("no2");
        Log.i("orm", "update set :" + mOrm.update(setBean));
        StubBean stubBean = new StubBean();
        stubBean.setAddress("223");
        Log.i("orm", "update stub :" + mOrm.update(stubBean, mOrm.where(StubBean.class)));
    }

    private void testDelete() {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub3");
        Log.i("orm", "delete stub :" + mOrm.delete(stubBean));
    }

    private long addSet() {
        SetBean setBean = new SetBean();
        setBean.setId(111);
        setBean.setName("hello");
        setBean.setUsers(new int[]{1, 2, 3});
        StubBean stubBean = new StubBean();
        stubBean.setName("stub");
        stubBean.setAddress("hello");
        setBean.setStubBean(stubBean);
        return mOrm.insert(setBean);
    }

    private long addStub1() {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }

    private long addStub2() {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub2");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }

    private long addStub3() {
        StubBean stubBean = new StubBean();
        stubBean.setName("stub3");
        stubBean.setAddress("hello");
        return mOrm.insert(stubBean);
    }
}
