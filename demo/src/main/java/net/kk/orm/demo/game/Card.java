package net.kk.orm.demo.game;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.Table;

@Table(name = OrmCard.Cards.TABLE, uri = OrmCard.Cards.CONTENT_URI_STRING, onlyRead = true)
public class Card extends CardData {
    @Column(value = OrmCard.Cards.ID, primaryKey = true)
    private long code;
    @Column(OrmCard.Text.NAME)
    private String name;
    @Column(OrmCard.Text.DESC)
    private String desc;

    public Card() {

    }

    public Card(Card card) {
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

    @Override
    public String toString() {
        return "Card{" +
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
                '}';
    }
}
