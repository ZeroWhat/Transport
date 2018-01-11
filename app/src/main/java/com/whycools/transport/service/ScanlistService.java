package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.ScanRec;

import java.util.ArrayList;
import java.util.List;


/**
 * 扫码后的条码数据增删改查
 * 
 * @author QL
 * 
 */
public class ScanlistService {
	DBinventory dDBinventory;
	Context context;

	public ScanlistService(Context context) {
		if(dDBinventory==null){
			this.dDBinventory = new DBinventory(context);
		}
		this.context = context;
	}

	// 添加数据
	public void addScanlist(ScanRec scanlist) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		Log.i("addScanlist数据库表", "添加成功");
		db.execSQL(
					"insert into scanlist(" +
						"storeId," + // 1
						"shipId," + // a1
						"carId," + // 3
						"userId," + 
						"carname," + // 4
						"billtype," + // 5
						"billno," + // 6
						"goodsId," + // 7
						"goodsname," + // 8
						"bar69," + // 9
						"barcodes," + // 10
						"qty," + // 11
						"time," + // 12
						"isInCode," + // 13
						"goodsno"+
						") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { 
						scanlist.getStoreId(),//1 
						scanlist.getShipId(),//a1
						scanlist.getCarId(),//3
						scanlist.getUserId(),
						scanlist.getCarname(),//4
						scanlist.getBilltype(),//5
						scanlist.getBillno(), //6
						scanlist.getGoodsId(),//7
						scanlist.getGoodsname(), //8
						scanlist.getBar69(),//9
						scanlist.getBarcodes(),//10
						scanlist.getQty(),//11
						scanlist.getTime(),//12
						scanlist.getIsInCode(),//13
						scanlist.getGoodsno()
						});
	}
	//判断数据库中是否有数据有数据为true没有为false
	public boolean isData(){
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist ",null);
		if (cursor.getCount()==0){
			cursor.close();
			return  false;
		}else{
			cursor.close();
			return true;
		}
	}

	// 清除数据表所有内容
	public void clearScanlist() {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		try {
			db.execSQL("DELETE FROM scanlist");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
		Log.i("ShiplistService数据表", "清除了");
	}

	// 删除
	public void deleteScanlist(String barcodes) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("delete from scanlist where barcodes=?", new String[] { barcodes });
		Log.i("删除", "barcodes: "+barcodes);
	}
	// 修改
		public void updateScanlist(ScanRec scanlist) {
			SQLiteDatabase db = dDBinventory.getWritableDatabase();
			db.execSQL("update scanlist set billno=? ,   goodsId=? ,  barcodes=? ,   isInCode=? where _id=?",
					new Object[] { scanlist.getBillno(), scanlist.getGoodsId(),scanlist.getBarcodes(),scanlist.getIsInCode(),scanlist.get_id() });
			Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
		}
	// 修改
	public void updateScanlistBarcodes(String  barcodes,String id) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("update scanlist set barcodes=?  where _id=?",
				new Object[] { barcodes,id });
		Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
	}

	public String  findisInCode(String barcodes) {

		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where barcodes=?",new String[]{barcodes});
		if(cursor.moveToFirst()){
			String isInCode=cursor.getString(cursor.getColumnIndex("isInCode"));
			cursor.close();
			return  isInCode;
		}
		cursor.close();
		return "";
	}

	public boolean findbarcodes(String barcodes){

		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where barcodes=?",
				new String[] { barcodes });

		Log.i("查询数据库", "个数为: "+cursor.getCount());
		if(cursor.getCount()==0){
			Log.i("查询数据库", "没有数据");
			cursor.close();
			return false;
		}else{
			Log.i("查询数据库", "有数据");
			cursor.close();
			return true;
		}

	}
	public boolean findisincode(String isInCode){
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where isInCode=?",new String[] { isInCode });
		if (cursor.moveToFirst()){
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	// 查询
	public ScanRec findShipid(String barcodes) {

		ScanRec scanlist = new ScanRec();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where barcodes=?",
				new String[] { barcodes });
		if (cursor.moveToFirst()) {


			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // a1

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 3

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 4
			String billtype = cursor.getString(cursor
					.getColumnIndex("billtype")); // 5
			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 6
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 7
			String _goodsname = cursor.getString(cursor
					.getColumnIndex("goodsname")); // 8
			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 9
			String _barcodes = cursor.getString(cursor
					.getColumnIndex("barcodes")); // 10
			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 11
			String time = cursor.getString(cursor.getColumnIndex("time")); // 12
			String isInCode = cursor.getString(cursor
					.getColumnIndex("isInCode"));// 13


			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(_goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(_barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14

		}
		cursor.close();
		return scanlist;
	}


	// 查询
	public ScanRec findScanlist(String _id) {

		ScanRec scanlist = new ScanRec();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where _id=?",
				new String[] { _id });
		if (cursor.moveToFirst()) {


			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // a1

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 3
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 3

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 4
			String billtype = cursor.getString(cursor
					.getColumnIndex("billtype")); // 5
			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 6
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 7
			String _goodsname = cursor.getString(cursor
					.getColumnIndex("goodsname")); // 8
			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 9
			String barcodes_ = cursor.getString(cursor
					.getColumnIndex("barcodes")); // 10
			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 11
			String time = cursor.getString(cursor.getColumnIndex("time")); // 12
			String isInCode = cursor.getString(cursor
					.getColumnIndex("isInCode"));// 13


			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(_goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes_);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14

		}
		cursor.close();
		return scanlist;
	}

	// 查找全部数据
	public List<ScanRec> getAllScanlistList() {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist  order by time desc", null);
		while (cursor.moveToNext()) {
			String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5
			
			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6
			
			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7
			
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 9
			
			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10
			
			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11
			
			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13
			
			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();
			scanlist.set_id(_id);
			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}



	// 查找全部数据
	public List<ScanRec> getAllScanlistListBillno(String Billno) {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from(select * from scanlist  order by time desc)where goodsno=? and billno=?", new String[]{"单",Billno});//SELECT  *
//		FROM  (SELECT  *
//						FROM  NEWS
//				ORDER BY  TIME  DESC)
//		WHERE  ROWNUM <=20
		while (cursor.moveToNext()) {
			String _id = cursor.getString(cursor.getColumnIndex("_id")); // a1
			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5

			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6

			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 9

			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10

			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11

			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13

			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();
			scanlist.set_id(_id);
			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}

	// 查找全部数据
	public List<ScanRec> getAllScanlistListsanone() {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from(select * from scanlist  order by time desc)where goodsno=? ", new String[]{"自由"});//SELECT  *
//		FROM  (SELECT  *
//						FROM  NEWS
//				ORDER BY  TIME  DESC)
//		WHERE  ROWNUM <=20
		while (cursor.moveToNext()) {
			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5

			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6

			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 9

			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10

			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11

			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13

			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();

			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}

	// 查找全部数据
	public List<ScanRec> getAllScanlistListachat(String Billno) {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from(select * from scanlist  order by time desc)where goodsno=? and billno=?", new String[]{"采",Billno});//SELECT  *
//		FROM  (SELECT  *
//						FROM  NEWS
//				ORDER BY  TIME  DESC)
//		WHERE  ROWNUM <=20
		while (cursor.moveToNext()) {
			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5

			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6

			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 9

			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10

			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11

			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13

			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();

			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}
	// 查找全部数据
	public List<ScanRec> getAllScanlistListDispatch(String Billno) {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from(select * from scanlist  order by time desc)where goodsno=? and billno=? ", new String[]{"调",Billno});//SELECT  *
//		FROM  (SELECT  *
//						FROM  NEWS
//				ORDER BY  TIME  DESC)
//		WHERE  ROWNUM <=20
		while (cursor.moveToNext()) {
			String storeId = cursor.getString(cursor.getColumnIndex("storeId")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4

			String carname = cursor.getString(cursor.getColumnIndex("carname")); // 5

			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6

			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 9

			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10

			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11

			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13

			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();

			scanlist.setStoreId(storeId);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}

	// 查找全部数据
	public List<ScanRec> getAllScanlistbycar(String carname, String goodsname) {
		List<ScanRec> data = new ArrayList<ScanRec>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from scanlist where carname=? and goodsname=?", new String[]{carname,goodsname});
		while (cursor.moveToNext()) {
			String _id = cursor.getString(cursor.getColumnIndex("_id")); // a1

			String shipId = cursor.getString(cursor.getColumnIndex("shipId")); // 3

			String carId = cursor.getString(cursor.getColumnIndex("carId")); // 4
			String userId = cursor.getString(cursor.getColumnIndex("userId")); // 4



			String billtype = cursor.getString(cursor.getColumnIndex("billtype")); // 6

			String billno = cursor.getString(cursor.getColumnIndex("billno")); // 7

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 8


			String bar69 = cursor.getString(cursor.getColumnIndex("bar69")); // 10

			String barcodes = cursor.getString(cursor.getColumnIndex("barcodes")); // 11

			String qty = cursor.getString(cursor.getColumnIndex("qty")); // 12

			String time = cursor.getString(cursor.getColumnIndex("time")); // 13

			String isInCode = cursor.getString(cursor.getColumnIndex("isInCode"));// 14

			String goodsno=cursor.getString(cursor.getColumnIndex("goodsno"));

			ScanRec scanlist = new ScanRec();

			scanlist.set_id(_id);// a1
			scanlist.setShipId(shipId);// 3
			scanlist.setCarId(carId);// 4
			scanlist.setUserId(userId);// 4
			scanlist.setCarname(carname);// 5
			scanlist.setBilltype(billtype);// 6
			scanlist.setBillno(billno);// 7
			scanlist.setGoodsId(goodsId);// 8
			scanlist.setGoodsname(goodsname);// 9
			scanlist.setBar69(bar69);// 10
			scanlist.setBarcodes(barcodes);// 11
			scanlist.setQty(qty);// 12
			scanlist.setTime(time);// 13
			scanlist.setIsInCode(isInCode);// 14
			scanlist.setGoodsno(goodsno);

			data.add(scanlist);
		}
		cursor.close();
		return data;
	}

}
