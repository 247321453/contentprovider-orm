package net.kk.orm.demo.game;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.Table;

@Table(name = OrmCard.Cards.TABLE, uri = OrmCard.Cards.CONTENT_URI_STRING, onlyRead = true)
public class CardFull extends CardData {
    //code必须和CardData的code一样，才能覆盖sql字段
    @Column(value = OrmCard.Cards.ID, primaryKey = true)
    private long code;
    @Column(OrmCard.Text.NAME)
    private String name;
    @Column(OrmCard.Text.DESC)
    private String desc;
    @Column(OrmCard.Text.STR1)
    private String str1;
    @Column(OrmCard.Text.STR2)
    private String str2;
    @Column(OrmCard.Text.STR3)
    private String str3;
    @Column(OrmCard.Text.STR4)
    private String str4;
    @Column(OrmCard.Text.STR5)
    private String str5;
    @Column(OrmCard.Text.STR6)
    private String str6;
    @Column(OrmCard.Text.STR7)
    private String str7;
    @Column(OrmCard.Text.STR8)
    private String str8;
    @Column(OrmCard.Text.STR9)
    private String str9;
    @Column(OrmCard.Text.STR10)
    private String str10;
    @Column(OrmCard.Text.STR11)
    private String str11;
    @Column(OrmCard.Text.STR12)
    private String str12;
    @Column(OrmCard.Text.STR13)
    private String str13;
    @Column(OrmCard.Text.STR14)
    private String str14;
    @Column(OrmCard.Text.STR15)
    private String str15;
    @Column(OrmCard.Text.STR16)
    private String str16;

    public CardFull() {

    }

    public CardFull(CardFull card) {
        super(card);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public void setCode(long code) {
        this.code = code;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String getStr5() {
        return str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5;
    }

    public String getStr6() {
        return str6;
    }

    public void setStr6(String str6) {
        this.str6 = str6;
    }

    public String getStr7() {
        return str7;
    }

    public void setStr7(String str7) {
        this.str7 = str7;
    }

    public String getStr8() {
        return str8;
    }

    public void setStr8(String str8) {
        this.str8 = str8;
    }

    public String getStr9() {
        return str9;
    }

    public void setStr9(String str9) {
        this.str9 = str9;
    }

    public String getStr10() {
        return str10;
    }

    public void setStr10(String str10) {
        this.str10 = str10;
    }

    public String getStr11() {
        return str11;
    }

    public void setStr11(String str11) {
        this.str11 = str11;
    }

    public String getStr12() {
        return str12;
    }

    public void setStr12(String str12) {
        this.str12 = str12;
    }

    public String getStr13() {
        return str13;
    }

    public void setStr13(String str13) {
        this.str13 = str13;
    }

    public String getStr14() {
        return str14;
    }

    public void setStr14(String str14) {
        this.str14 = str14;
    }

    public String getStr15() {
        return str15;
    }

    public void setStr15(String str15) {
        this.str15 = str15;
    }

    public String getStr16() {
        return str16;
    }

    public void setStr16(String str16) {
        this.str16 = str16;
    }

    @Override
    public String toString() {
        return "CardFull{" +
                "code=" + code +
                ", ot=" + ot +
                ", alias=" + alias +
                ", setcode=" + setcode +
                ", type=" + type +
                ", level=" + level +
                ", attribute=" + attribute +
                ", race=" + race +
                ", attack=" + attack +
                ", defense=" + defense +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", str1='" + str1 + '\'' +
                ", str2='" + str2 + '\'' +
                ", str3='" + str3 + '\'' +
                ", str4='" + str4 + '\'' +
                ", str5='" + str5 + '\'' +
                ", str6='" + str6 + '\'' +
                ", str7='" + str7 + '\'' +
                ", str8='" + str8 + '\'' +
                ", str9='" + str9 + '\'' +
                ", str10='" + str10 + '\'' +
                ", str11='" + str11 + '\'' +
                ", str12='" + str12 + '\'' +
                ", str13='" + str13 + '\'' +
                ", str14='" + str14 + '\'' +
                ", str15='" + str15 + '\'' +
                ", str16='" + str16 + '\'' +
                '}';
    }
}
