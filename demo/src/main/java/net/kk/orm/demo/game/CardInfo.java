package net.kk.orm.demo.game;

import net.kk.orm.annotations.Column;
import net.kk.orm.annotations.Table;

@Table(name = OrmCard.Info.TABLE, uri = OrmCard.Info.CONTENT_URI_STRING)
public class CardInfo {
    @Column(value = OrmCard.Info.ID, primaryKey = true)
    private long id;
    @Column(OrmCard.Info.DATA)
    private CardData data;
    @Column(OrmCard.Info.TEXT)
    private CardText text;

    public CardInfo() {
        this.text = new CardText();
        this.data = new CardData();
    }

    public CardInfo(long id) {
        this();
        this.id = id;
        this.text.setCode(id);
        this.data.setCode(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CardData getData() {
        return data;
    }

    public void setData(CardData data) {
        this.data = data;
    }

    public CardText getText() {
        return text;
    }

    public void setText(CardText text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "id=" + id +
                ", data=" + data +
                ", text=" + text +
                '}';
    }
}
