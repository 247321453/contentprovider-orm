package net.kk.orm.linq;

import net.kk.orm.api.OrmColumn;
import net.kk.orm.api.OrmTable;
import net.kk.orm.api.SQLiteOpera;

import java.util.ArrayList;
import java.util.List;

import static net.kk.orm.utils.SQLiteUtils.mask;

public class WhereBuilder<T> {
    private OrmTable<T> mTable;
    private final StringBuilder mStringBuilder;
    private final List<Object> whereItems;
    private int mOPs = 0;
    private Orm mOrm;

    private WhereBuilder(Orm orm) {
        this.mOrm = orm;
        whereItems = new ArrayList<>();
        mStringBuilder = new StringBuilder();
    }

    WhereBuilder(Orm orm, OrmTable<T> table) {
        this(orm);
        this.mTable = table;
    }

    WhereBuilder(Orm orm, Class<T> pClass) {
        this(orm);
        this.mTable = Orm.table(pClass);
    }

    public WhereBuilder<T> only(T t) {
        List<OrmColumn> columns = mTable.getKeyColumns();
        if (columns != null) {
            for (OrmColumn column : columns) {
                op(column, "=", column.getValue(t), true);
            }
        }
        return this;
    }

    private WhereBuilder<T> op(WhereBuilder whereBuilder, boolean isAnd) {
        if (whereBuilder.mOPs > 0) {
            if (mOPs > 0) {
                if (isAnd) {
                    mStringBuilder.append(" AND ");
                } else {
                    mStringBuilder.append(" OR ");
                }
            }
            mStringBuilder.append("(");
            mStringBuilder.append(whereBuilder.getWhereString());
            mStringBuilder.append(")");
            this.whereItems.addAll(whereBuilder.whereItems);
            this.mOPs += whereBuilder.mOPs;
        }
        return this;
    }

    public WhereBuilder<T> and(WhereBuilder whereBuilder) {
        return op(whereBuilder, true);
    }

    public WhereBuilder<T> or(WhereBuilder whereBuilder) {
        return op(whereBuilder, false);
    }

    WhereBuilder<T> op(OrmColumn column, String op, Object value, boolean isAnd) {
        if (mOPs > 0) {
            if (isAnd) {
                mStringBuilder.append(" AND ");
            } else {
                mStringBuilder.append(" OR ");
            }
        }
        mStringBuilder.append(" ");
        mStringBuilder.append(column.getColumnName());
        mStringBuilder.append(wrapper(op));
        mStringBuilder.append("? ");
        mOPs++;
        this.whereItems.add(column.toDbValue(mOrm, value, SQLiteOpera.QUERY));
        return this;
    }

    private WhereBuilder<T> op(String columnName, String op, Object value, boolean isAnd) {
        OrmColumn column = mTable.getColumn(columnName);
        if (column == null) {
            return this;
        }
        return op(column, op, value, isAnd);
    }

    public WhereBuilder<T> and(String columnName, String op, Object value) {
        return op(columnName, op, value, true);
    }

    public WhereBuilder<T> or(String columnName, String op, Object value) {
        return op(columnName, op, value, false);
    }

    public WhereBuilder<T> in(String columnName, List<Object> value) {
        OrmColumn column = mTable.getColumn(columnName);
        if (column == null) {
            return this;
        }
        if (mOPs >= 0) {
            mStringBuilder.append(" AND ");
        }
        if (value == null || value.size() == 0) {
            mStringBuilder.append(" IN () ");
        } else {
            mStringBuilder.append("(" + mask(columnName) + " IN (");
            int count = value.size();
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    mStringBuilder.append("?");
                } else {
                    mStringBuilder.append(",?");
                }
            }
            mStringBuilder.append(") )");
        }
        mOPs++;
        this.whereItems.add(value);
        return this;
    }

    public WhereBuilder<T> between(String columnName, Object start, Object end) {
        OrmColumn column = mTable.getColumn(columnName);
        if (column == null) {
            return this;
        }
        if (mOPs >= 0) {
            mStringBuilder.append(" AND ");
        }
        mStringBuilder.append(" ( " + mask(columnName) + " BETWEEN ? AND ? )");
        mOPs++;
        this.whereItems.add(start);
        this.whereItems.add(end);
        return this;
    }

    public String getWhereString() {
        return mStringBuilder.toString();
    }

    public String[] getWhereItems() {
        String[] items = new String[whereItems.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = String.valueOf(whereItems.get(i));
        }
        return items;
    }

    private String wrapper(String op) {
        if ("!=".equals(op)) {
            return "<>";
        } else if ("==".equals(op)) {
            return "=";
        }
        return op;
    }

}
