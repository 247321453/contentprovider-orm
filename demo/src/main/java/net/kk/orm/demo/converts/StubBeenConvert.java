package net.kk.orm.demo.converts;


import net.kk.orm.Orm;
import net.kk.orm.converts.ListConvert;
import net.kk.orm.demo.bean.StubBean;

public class StubBeenConvert extends ListConvert<StubBean> {
    @Override
    protected StubBean findById(Orm orm, String k) {
        return orm.select(StubBean.class).findById(k);
    }

    @Override
    protected String getKey(StubBean stubBean) {
        return stubBean.getName();
    }
}
