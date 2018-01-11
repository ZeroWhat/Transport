package com.whycools.transport.entity;

/**
 * Created by user on 2017-08-11.
 */

/*
// 新条码标准表⑤
        db.execSQL("create table newbarcode_header ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "goodsId int not null,"
                + "goodsname nvarchar(60) not null,"
                + "bar69in nvarchar(20),"
                + "bar69out nvarchar(20),"
                + "barcode_header nvarchar(30),"
                + "isIn int,"
                + "barcodeLeftpos int, "
                + "minLen int,"
                + "revisetime datetime)");*/

public class Newbarcodeheader {
    private String _id;
    private String goodsId;
    private String goodsname;
    private String bar69in;
    private String bar69out;
    private String barcode_header;
    private String isIn;
    private String barcodeLeftpos;
    private String minLen;
    private String revisetime;


    private String in_qty;
    private String cin_qty;
    private String out_qty;
    private String cout_qty;

    public String getIn_qty() {
        return in_qty;
    }

    public void setIn_qty(String in_qty) {
        this.in_qty = in_qty;
    }

    public String getCin_qty() {
        return cin_qty;
    }

    public void setCin_qty(String cin_qty) {
        this.cin_qty = cin_qty;
    }

    public String getOut_qty() {
        return out_qty;
    }

    public void setOut_qty(String out_qty) {
        this.out_qty = out_qty;
    }

    public String getCout_qty() {
        return cout_qty;
    }

    public void setCout_qty(String cout_qty) {
        this.cout_qty = cout_qty;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getBar69in() {
        return bar69in;
    }

    public void setBar69in(String bar69in) {
        this.bar69in = bar69in;
    }

    public String getBar69out() {
        return bar69out;
    }

    public void setBar69out(String bar69out) {
        this.bar69out = bar69out;
    }

    public String getBarcode_header() {
        return barcode_header;
    }

    public void setBarcode_header(String barcode_header) {
        this.barcode_header = barcode_header;
    }

    public String getIsIn() {
        return isIn;
    }

    public void setIsIn(String isIn) {
        this.isIn = isIn;
    }

    public String getBarcodeLeftpos() {
        return barcodeLeftpos;
    }

    public void setBarcodeLeftpos(String barcodeLeftpos) {
        this.barcodeLeftpos = barcodeLeftpos;
    }

    public String getMinLen() {
        return minLen;
    }

    public void setMinLen(String minLen) {
        this.minLen = minLen;
    }

    public String getRevisetime() {
        return revisetime;
    }

    public void setRevisetime(String revisetime) {
        this.revisetime = revisetime;
    }
}
