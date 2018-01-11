package com.whycools.transport.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 创建所有数据库表
 * Created by Zero on 2016-12-05.
 */
public class DBinventory extends SQLiteOpenHelper {

    public DBinventory(Context context) {
        super(context, "inventory.db", null, 4);
        Log.i("创建数据字段", "成功: ");
    }

    // 数据库第一次创建时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table temporary (_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode nvarchar(100))");
        Log.i("创建表", "onCreate: temporary成功");

        // 服务器上接受的 发货提货 清单①
        db.execSQL("create table shiplist (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "storeid int," +//仓库id
                "carId int," +//汽车id
                "inoutFlag int," + // 内外机标志 -1为出 1 为进
                "carname nvarchar(50)," + // 车辆名称
                "shipdate nvarchar(10)," + // 发货日期
                "seqno nvarchar(10), " + // 车次号
                "billtype nvarchar(4)," + // 单据类别 出,入, 调
                "billno nvarchar(10)," + // 单据编号
                 "company_address nvarchar(100)," + // 对方单位地址
                "goodsId int ," + // 产品id
                "goodsname nvarchar(100) ," + // 品名
                "in_qty int ," + // 入库数
                "cin_qty int ," + // 已入库数
                "out_qty int ," + // 出库数
                "cout_qty int ," + // 已出库数
                "innerno varchar(20) ," + // 内机识别码 0121222|AXSDA01|ADS0011					// --
                "outerno varchar(20) ," + // 外机识别码 3000121
                "address varchar(20) ,"+//地址
                "Phonenumber  varchar(20) ,"+//电话号码
                "isfinish varchar(20) ,"+//是否完成
                "distance varchar(20) ,"+//距离值
                "isStart varchar(20) ,"+//是否发车
                "stateId varchar(20) ,"+//标识
                "province varchar(20) ,"+//省
                "city varchar(20) ,"+//市
                "district varchar(20) ,"+//区
                "cm int )");

        // 扫描后的条码数据②
        db.execSQL("create table scanlist ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + // 1
                "storeId int," + // 仓库id2
                "shipId int," + // 3
                "carId int," + // 4车id
                "userId int," + // 4用户id
                "carname nvarchar(20)," + // 5汽车名称
                "billtype nvarchar(4), " + // 6单据类别
                "billno nvarchar(10)," + // 7单据编号 SJ0000
                "goodsId int not null," + // 8货物id
                "goodsname nvarchar(70)," + // 9货物名称
                "bar69 nvarchar(50)," + // 10 69码
                "barcodes nvarchar(50)," + // 11条码
                "qty int," + // 12 数量
                "time nvarchar(20)," + // 13时间
                "isInCode int," +
                "goodsno nvarchar(20))");// 14条码类别

        // 扫描后的条码数据备份 --③
        db.execSQL("create table scanlistbak ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "billno nvarchar(10),"
                + "barcode nvarchar(50),"
                + "qty int,"
                + "postdate datetime)");
        // 69条码库存数据 - 志高 四
        db.execSQL("create table barcodeStore ("
                + "_id integer primary key autoincrement not null,"
                + "goodsid int,"
                + "bar69 nvarchar(50),"
                + "barcode nvarchar(50),"
                + "qty int,"
                + "isIn int)");

        // 条码标准表⑤
        db.execSQL("create table barcode_header ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "goodsId int not null,"
                + "goodsname nvarchar(60) not null,"
                + "bar69in nvarchar(20),"
                + "bar69out nvarchar(20),"
                + "barcode_header_in nvarchar(30),"
                + "barcode_header_out nvarchar(30),"
                + "barcodeLeftpos int, "
                + "minLen int,"
                + "revisetime datetime)");

        // 仓库表 stores⑥
        db.execSQL("create table stores ("
                + "storeId int,"
                + "storename nvarchar(30),"
                + "address nvarchar(100),"
                + "phoneno nvarchar(30),"
                + "manager nvarchar(20))");

        // stkcksum 盘点表⑦
        db.execSQL("create table stkcksum ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "auto_id int,  " +
                "sumdate nvarchar(10)," +
                "storeid int," +
                "goodsId int," +
                "goodsno nvarchar(10), " +
                "goodsname nvarchar(60), " +
                "qmyereal int, " +
                "qmyeCount int," +
                "isCounted bit,  " +
                "isMove bit)"); //isMove == 是否有进出

        // 总部库存条码barcodes⑧
        db.execSQL("create table barcodes ("
                + "auto_id int,"
                + "aid int,"
                + "barcode nvarchar(30))");

        // 单个标准码条码标准⑨
        db.execSQL("create table code_header_multi (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "goodsId int not null," +
                "is_in int," +
                "bar69 nvarchar(20)," +
                "barcode nvarchar(100)," +
                "barcodeLeftpos int)");
//		db.execSQL("create table temporary (auto_id INTEGER PRIMARY KEY AUTOINCREMENT,barcode nvarchar(100))");
//		Log.i("创建表", "onCreate: temporary成功");

        db.execSQL("create index barcode_idx_scanlist on scanlist (billtype, billno, barcodes)");
        db.execSQL("create index barcodeheader_index on barcode_header (bar69in,bar69out,barcode_header_in,barcode_header_out,goodsId)");
        db.execSQL("create index barcodeheader_index_goodsid on barcode_header (goodsId)");
//		db.execSQL("create index code_header_idx_shiplist on code_header_multi (goodsId,barcode_header_in,barcode_header_out)");
        //临时数据库temporary

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
                + "revisetime datetime)");

        // stkcksum 新盘点表⑦
        db.execSQL("create table newstkcksum ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "auto_id int,  " +
                "SumDate nvarchar(10)," +//日期
                "storeid int," +        //仓库id
                "classname nvarchar(10)," +//品名
                "goodsno nvarchar(10), " +//产品编号
                "goodsname nvarchar(60), " +//产品名称
                "qmyereal int, " +//仓库数
                "qmyeCount int," +//仓库出
                "isMove bit)"); //isMove == 是否有进出

        // 上传盘点表
        db.execSQL("create table upstkcksum ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "auto_id int,  " +
                "SumDate nvarchar(10)," +//日期
                "storeid int," +        //仓库id
                "time nvarchar(20)," + // 13时间
                "goodsname nvarchar(60), " +//产品名称
                "qmyereal int, " +//
                "qmyeCount int)"); //


    }


    // 数据库文件版本号发生变化时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        Log.i("---------------------", "onUpgrade: "+arg1);
        Log.i("---------------------", "onUpgrade: "+arg2);
        if(arg2>arg1){


        if (arg2==2){

        // stkcksum 新盘点表⑦
        db.execSQL("create table newstkcksum ("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "auto_id int,  " +
                "SumDate nvarchar(10)," +//日期
                "storeid int," +        //仓库id
                "classname nvarchar(10)," +//品名
                "goodsno nvarchar(10), " +//产品编号
                "goodsname nvarchar(60), " +//产品名称
                "qmyereal int, " +//仓库数
                "qmyeCount int," +//仓库出
                "isMove bit)"); //isMove == 是否有进出
        }
        if(arg2==3){
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
                    + "revisetime datetime)");

        }
            if(arg2==4){
                // 上传盘点表
                db.execSQL("create table upstkcksum ("+
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "auto_id int,  " +
                        "SumDate nvarchar(10)," +//日期
                        "storeid int," +        //仓库id
                        "time nvarchar(20)," + // 时间
                        "goodsname nvarchar(60), " +//产品名称
                        "qmyereal int, " +//
                        "qmyeCount int)"); //
            }

        }



    }

}