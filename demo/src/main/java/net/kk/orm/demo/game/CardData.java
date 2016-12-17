package net.kk.orm.demo.game;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.Table;

@Table(name = OrmCard.Data.TABLE, uri = OrmCard.Data.CONTENT_URI_STRING)
public class CardData {
    @Column(value = OrmCard.Data.ID, primaryKey = true)
    private long code;
    @Column(OrmCard.Data.OT)
    protected int ot;
    @Column(OrmCard.Data.ALIAS)
    protected long alias;
    @Column(OrmCard.Data.SETCODE)
    protected long setcode;
    @Column(OrmCard.Data.TYPE)
    protected long type;
    @Column(OrmCard.Data.LEVEL)
    protected int level;
    @Column(OrmCard.Data.ATTRIBUTE)
    protected int attribute;
    @Column(OrmCard.Data.RACE)
    protected long race;
    @Column(OrmCard.Data.ATTACK)
    protected int attack;
    @Column(OrmCard.Data.DEFENSE)
    protected int defense;
    @Column(OrmCard.Data.CATEGORY)
    protected long category;

    protected CardData() {
    }

    public CardData(long code) {
        this();
        this.code = code;
    }

    public CardData(CardData card) {
        this(card.getCode());
        this.ot = card.ot;
        this.alias = card.alias;
        this.setcode = card.setcode;
        this.type = card.type;
        this.level = card.level;
        this.attribute = card.attribute;
        this.race = card.race;
        this.attack = card.attack;
        this.defense = card.defense;
        this.category = card.category;

    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getOt() {
        return ot;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }

    public long getAlias() {
        return alias;
    }

    public void setAlias(long alias) {
        this.alias = alias;
    }

    public long getSetcode() {
        return setcode;
    }

    public void setSetcode(long setcode) {
        this.setcode = setcode;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public long getRace() {
        return race;
    }

    public void setRace(long race) {
        this.race = race;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CardData{" +
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
                '}';
    }
}
