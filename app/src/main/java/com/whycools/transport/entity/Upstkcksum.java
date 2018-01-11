package com.whycools.transport.entity;

/**
 * Created by user on 2017-08-25.
 */

public class Upstkcksum {
    private String _id;
    private String auto_id;
    private String SumDate;
    private String storeid;
    private String time;
    private String qmyereal;
    private String qmyeCount;
    private String goodsname;

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public String getSumDate() {
        return SumDate;
    }

    public void setSumDate(String sumDate) {
        SumDate = sumDate;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQmyereal() {
        return qmyereal;
    }

    public void setQmyereal(String qmyereal) {
        this.qmyereal = qmyereal;
    }

    public String getQmyeCount() {
        return qmyeCount;
    }

    public void setQmyeCount(String qmyeCount) {
        this.qmyeCount = qmyeCount;
    }
}
