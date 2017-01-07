package net.kk.orm.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.kk.orm.demo.game.CardText;
import net.kk.orm.demo.game.OrmCard;
import net.kk.orm.Orm;
import net.kk.orm.demo.bean.SetBean;
import net.kk.orm.demo.bean.StubBean;
import net.kk.orm.demo.db.Datas;
import net.kk.orm.demo.game.Card;
import net.kk.orm.demo.game.CardData;
import net.kk.orm.demo.game.CardFull;
import net.kk.orm.demo.game.CardInfo;

import java.util.List;

public class MainActivity extends Activity {
    Orm mOrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrm = new Orm(this);
        if (mOrm.select(CardInfo.class).count() == 0) {
            CardInfo cardInfo = new CardInfo(123);
            cardInfo.getData().setOt(2);
            cardInfo.getData().setAlias(41546);
            cardInfo.getText().setName("aaa");
            try {
                mOrm.insert(cardInfo);
                Log.i("orm", "insert cardinfo 123 ok");
            } catch (Exception e) {
            }
            CardInfo cardInfo2 = new CardInfo(1234);
            cardInfo2.getData().setOt(2);
            cardInfo2.getData().setAlias(1234);
            cardInfo2.getText().setName("bbb");
            try {
                mOrm.insert(cardInfo);
                Log.i("orm", "insert cardinfo 1234 ok");
            } catch (Exception e) {
            }
        }
        Log.i("orm", "cards=" + mOrm.select(CardData.class).count());
        Card card = mOrm.select(Card.class).findById(27551);
        Log.i("orm", "card=" + card);
        CardFull card2 = mOrm.select(CardFull.class).findById(41546);
        Log.i("orm", "card2=" + card2);
        CardData cardData = mOrm.select(CardData.class).findById(123);
        Log.i("orm", "cardData=" + cardData);
        CardText cardText = mOrm.select(CardText.class).where(OrmCard.Text.DESC, "like", "%光波%").findFirst();
        Log.i("orm", "cardText=" + cardText);
        CardInfo cardInfo = mOrm.select(CardInfo.class).findFirst();
        Log.i("orm", "cardInfo=" + cardInfo);
        List<CardInfo> cards = mOrm.select(CardInfo.class).where(OrmCard.Data.OT, "=", 2)
                .and("(" + OrmCard.Data.LEVEL + "&0xff)", "=", 3).findAll();
        Log.i("orm", "cards=" + cards);
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
