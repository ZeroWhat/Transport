package com.whycools.transport.utils;

import java.util.Hashtable;

/**
 * Created by QL on 2016-09-01.
 */
public class Goods {
    //下载的发车记录id
    private String _id;
    public String shipId;
    public String carId;
    public String billNo;
    public String goodsId;
    public String barcodeLeftPos;
    public String goodsname;
    //内机编号
    public String barcode_in;
    //外机编号
    public String barcode_out;
    public String inleft;
    public String inQty;
    public String outleft;
    public String outQty;
    public String bar69in;
    public String bar69out;
    public String isInCode;
    //最小长度
    public String minLen;
    public String StoreId;
    private String allqty;
    private String billtype;
    private String inoutFlag;

    public String getInoutFlag() {
        return inoutFlag;
    }

    public void setInoutFlag(String inoutFlag) {
        this.inoutFlag = inoutFlag;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getAllqty() {
        return allqty;
    }

    public void setAllqty(String allqty) {
        this.allqty = allqty;
    }

    public enum BarcodeCheckResult {
      TooShort, NotMatch, Match
    }
    public Goods()
    {

    }



    public Goods(String agoodsId, String agoodsname, String abarcode_in, String abarcode_out, String abarcodeLeftPos, String aminLen) //自由扫描
    {
        this.shipId = "0";
        this.carId = "0";
        this. goodsId = agoodsId;
        this.goodsname = agoodsname;
        this.barcode_in = abarcode_in;
        this.barcode_out = abarcode_out;
        this.barcodeLeftPos = abarcodeLeftPos;
        this.minLen = aminLen;
    }

    public Goods(String ashipId, String acarId, String abillno, String agoodsId, String agoodsname, String abarcode_in, String abarcode_out, String ainleft, String ainqty, String aoutleft, String aoutqty, String abarcodeLeftPos, String abar69in, String abar69out, String aisInCode, String aMinLen) {//出库,入库
        this.shipId = ashipId;
        this. billNo = abillno;
        this. carId = acarId;
        this.goodsId = agoodsId;
        this.goodsname = agoodsname;
        this.barcode_in = abarcode_in;
        this.barcode_out = abarcode_out;
        this.inleft = ainleft;
        this.inQty = ainqty;
        this.outleft = barcode_out == "null" ?"0" : aoutleft;
        this.outQty = aoutqty;
        this.barcodeLeftPos = abarcodeLeftPos;
        this.bar69in = abar69in;
        this.bar69out = abar69out;
        this.isInCode = aisInCode;//是否内机条码
        this.minLen = aMinLen;
    }


    //比较条码是否符合规则． 818423444
    public static boolean GoodsBarcodeMatch(String acode, String barcodeStd, int pos, int minLen)
    {
        //barcodeStd可能会是　"","null"
        //| 拆分内机规则
        String[] barcodes = barcodeStd.split("|");
        int num = barcodes.length;

        if (barcodeStd.length()< 1)
            return false;

        if (acode.length() < minLen)
            return false; // 长度不够
        for (int i = 0; i < num; i++)
        {//必须超过长度
            if (acode.indexOf(barcodes[i]) == pos - 1) // 扫码结构 0123456 , 开始为数 2， 标准条码 123 ---> 产品识别成功
                return true;
        }
        return false;
    }

    //暂时没用 到这个
    public static BarcodeCheckResult GoodsBarcodeIsMatch(String acode, String barcodeStd, int pos, int minLen)
    {
        //| 拆分内机规则
        String[] barcodes = barcodeStd.split("|");
        int num = barcodes.length;

        if (acode.length() < minLen) return BarcodeCheckResult.TooShort; // 长度不够
        for (int i = 0; i < num; i++)
        {//必须超过长度
            if (acode.indexOf(barcodes[i]) == pos - 1)
                return BarcodeCheckResult.Match;
        }
        return BarcodeCheckResult.NotMatch;
    }

    private static Hashtable ahashTable = new Hashtable();
    public static Hashtable getTmpGoodsTable()
    {
        return ahashTable;
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

    public String getBarcode_in() {
        return barcode_in;
    }

    public void setBarcode_in(String barcode_in) {
        this.barcode_in = barcode_in;
    }

    public String getBarcode_out() {
        return barcode_out;
    }

    public void setBarcode_out(String barcode_out) {
        this.barcode_out = barcode_out;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBarcodeLeftPos() {
        return barcodeLeftPos;
    }

    public void setBarcodeLeftPos(String barcodeLeftPos) {
        this.barcodeLeftPos = barcodeLeftPos;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public String getInQty() {
        return inQty;
    }

    public void setInQty(String inQty) {
        this.inQty = inQty;
    }

    public String getInleft() {
        return inleft;
    }

    public void setInleft(String inleft) {
        this.inleft = inleft;
    }

    public String getIsInCode() {
        return isInCode;
    }

    public void setIsInCode(String isInCode) {
        this.isInCode = isInCode;
    }

    public String getMinLen() {
        return minLen;
    }

    public void setMinLen(String minLen) {
        this.minLen = minLen;
    }

    public String getOutleft() {
        return outleft;
    }

    public void setOutleft(String outleft) {
        this.outleft = outleft;
    }

    public String getOutQty() {
        return outQty;
    }

    public void setOutQty(String outQty) {
        this.outQty = outQty;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }
}
