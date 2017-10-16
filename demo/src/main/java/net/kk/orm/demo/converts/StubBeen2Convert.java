package net.kk.orm.demo.converts;


import net.kk.orm.Orm;
import net.kk.orm.converts.ListConvert;
import net.kk.orm.demo.bean.StubBean2;

public class StubBeen2Convert extends ListConvert<StubBean2> {
    @Override
    protected StubBean2 findById(Orm orm, String k) {
        return orm.select(StubBean2.class).findById(k);
    }

    @Override
    protected String getKey(StubBean2 stubBean) {
        return ""+stubBean.getName();
    }
}
