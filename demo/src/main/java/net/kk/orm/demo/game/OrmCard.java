package net.kk.orm.demo.game;

import net.kk.orm.demo.BuildConfig;

public interface OrmCard {
    String AUTHORITY = BuildConfig.APPLICATION_ID + ".db.cards";
    int VERSION = 1;
    String DBNAME = "cards.db";

    interface Data {
        String TABLE = "datas";
        String ID = "id";
        String OT = "ot";
        String ALIAS = "alias";
        String SETCODE = "setcode";
        String TYPE = "type";
        String LEVEL = "level";
        String ATTRIBUTE = "attribute";
        String RACE = "race";
        String ATTACK = "atk";
        String DEFENSE = "def";
        String CATEGORY = "category";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
    }

    interface Text {
        String TABLE = "texts";
        String ID = "id";
        String NAME = "name";
        String DESC = "desc";
        String STR1 = "str1";
        String STR2 = "str2";
        String STR3 = "str3";
        String STR4 = "str4";
        String STR5 = "str5";
        String STR6 = "str6";
        String STR7 = "str7";
        String STR8 = "str8";
        String STR9 = "str9";
        String STR10 = "str10";
        String STR11 = "str11";
        String STR12 = "str12";
        String STR13 = "str13";
        String STR14 = "str14";
        String STR15 = "str15";
        String STR16 = "str16";
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE + "/";
    }

    interface Cards {
        String ID = Data.TABLE + "." + Data.ID;
        String TABLE = Data.TABLE + " left  join " + Text.TABLE + " on " + Data.TABLE + "." + Data.ID + "="
                + Text.TABLE + "." + Text.ID;
        String CONTENT_URI_STRING = "content://" + AUTHORITY + "/cards/";
    }
}
