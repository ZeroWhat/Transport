package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Stkcksum;

import java.util.ArrayList;
import java.util.List;


public class StkcksumService {
	DBinventory dDBinventory;
	Context context;

	public StkcksumService(Context context) {
		if(dDBinventory==null){
			this.dDBinventory = new DBinventory(context);
		}
		this.context = context;
	}

	// 添加数据
	public void addStkcksum(String auto_id,String sumdate,String storeid,String goodsno,String goodsname,String qmyereal,String qmyeCount,String isMove) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		Log.i("Stkcksum数据库表", "添加成功");
		db.execSQL(
					"insert into stkcksum(" +
						"auto_id," + // 1
						"sumdate," + // 2
						"storeid," + // 3
						"goodsno," + // 5
						"sumdate," + // 2
						"goodsname," + // 6
						"qmyereal," + // 7
						"qmyeCount," + // 8
						"isMove" + // 10
						") values(?,?,?,?,?,?,?,?)",
				new Object[] {

						auto_id,
						sumdate,
						storeid,

						goodsno,
						goodsname,
						qmyereal,
						qmyeCount,

						isMove
						});
	}

	// 清除数据表所有内容
	public void clearStkcksum() {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		try {
			db.execSQL("DELETE FROM stkcksum");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
		Log.i("Stkcksum数据表", "数据清除了");
	}
	// 删出某一条数据
	public void deleteStkcksum(String auto_id) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("delete from stkcksum where auto_id=?", new String[] { auto_id });
		Log.i("Stkcksum数据库", "成功删除");
	}
	// 修改某一条数据
		public void updateStkcksum(String  qmyeCount,String id) {
			SQLiteDatabase db = dDBinventory.getWritableDatabase();
			db.execSQL("update stkcksum set qmyeCount=? , isCounted=? where _id=?",
					new Object[] {qmyeCount ,"1",id});
			Log.i("Stkcksum数据库", "修改数据成功");

		}


	// 查询某一条数据
	public Stkcksum findStkcksum(String auto_id) {
		Stkcksum stkcksum = new Stkcksum();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stkcksum where auto_id=?",
				new String[] { auto_id });
		if (cursor.moveToFirst()) {
			String _id = cursor.getString(cursor.getColumnIndex("_id")); // 1
			String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

			String sumdate = cursor.getString(cursor.getColumnIndex("sumdate")); // 2

			String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3

			String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
			String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
			String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8
			String isCounted = cursor.getString(cursor.getColumnIndex("isCounted")); // 9
			String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10
            stkcksum.set_id(_id);
			stkcksum.setAuto_id(_auto_id);
			stkcksum.setSumdate(sumdate);
			stkcksum.setStoreid(storeid);

			stkcksum.setGoodsno(goodsno);
			stkcksum.setGoodsname(goodsname);
			stkcksum.setQmyereal(qmyereal);
			stkcksum.setQmyeCount(qmyeCount);
			stkcksum.setIsCounted(isCounted);
			stkcksum.setIsMove(isMove);

			
		}
		Log.i("Stkcksum数据库", "查找一条数据正确");
		cursor.close();
		return stkcksum;
	}

	// 查找数据库两面全部数据
	public List<Stkcksum> getAllStkcksum() {
		List<Stkcksum> data = new ArrayList<Stkcksum>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stkcksum  ", null);
		while (cursor.moveToNext()) {
			String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

			String sumdate = cursor.getString(cursor.getColumnIndex("sumdate")); // 2

			String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 4
			String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
			String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
			String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8
			String isCounted = cursor.getString(cursor.getColumnIndex("isCounted")); // 9
			String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10
			
			Stkcksum stkcksum = new Stkcksum();
			
			stkcksum.setAuto_id(_auto_id);
			stkcksum.setSumdate(sumdate);
			stkcksum.setStoreid(storeid);

			stkcksum.setGoodsno(goodsno);
			stkcksum.setGoodsname(goodsname);
			stkcksum.setQmyereal(qmyereal);
			stkcksum.setQmyeCount(qmyeCount);
			stkcksum.setIsCounted(isCounted);
			stkcksum.setIsMove(isMove);

			data.add(stkcksum);
		}
		Log.i("Stkcksum数据库", "查找所有数据正确");
		cursor.close();
		return data;
	}


	// 查找数据库两面全部数据
	public List<Stkcksum> getAllUpStkcksum() {
		List<Stkcksum> data = new ArrayList<Stkcksum>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stkcksum where qmyecount <> '' and isCounted=1  ", null);
		while (cursor.moveToNext()) {
			String _auto_id = cursor.getString(cursor.getColumnIndex("auto_id")); // 1

			String sumdate = cursor.getString(cursor.getColumnIndex("sumdate")); // 2

			String storeid = cursor.getString(cursor.getColumnIndex("storeid")); // 3
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 4
			String goodsno = cursor.getString(cursor.getColumnIndex("goodsno")); // 5
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 6
			String qmyereal = cursor.getString(cursor.getColumnIndex("qmyereal")); // 7
			String qmyeCount = cursor.getString(cursor.getColumnIndex("qmyeCount")); // 8
			String isCounted = cursor.getString(cursor.getColumnIndex("isCounted")); // 9
			String isMove = cursor.getString(cursor.getColumnIndex("isMove")); // 10

			Stkcksum stkcksum = new Stkcksum();

			stkcksum.setAuto_id(_auto_id);
			stkcksum.setSumdate(sumdate);
			stkcksum.setStoreid(storeid);

			stkcksum.setGoodsno(goodsno);
			stkcksum.setGoodsname(goodsname);
			stkcksum.setQmyereal(qmyereal);
			stkcksum.setQmyeCount(qmyeCount);
			stkcksum.setIsCounted(isCounted);
			stkcksum.setIsMove(isMove);

			data.add(stkcksum);
		}
		Log.i("Stkcksum数据库", "查找所有数据正确");
		cursor.close();
		return data;
	}

}
