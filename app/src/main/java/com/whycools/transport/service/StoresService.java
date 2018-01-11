package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Stores;

import java.util.ArrayList;
import java.util.List;


public class StoresService {
	DBinventory dDBinventory;
	Context context;

	public StoresService(Context context) {
		if(dDBinventory==null){
			this.dDBinventory = new DBinventory(context);
		}
		this.context = context;
	}

	// 添加数据
	public void addStkcksum(Stores stores) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		Log.i("Stores数据库表", "添加成功");
		db.execSQL(
					"insert into stores(" +
						"storeId," + // 1
						"storename," + // 2
						"address," + // 3
						"phoneno," + // 4
						"manager" + // 5
						") values(?,?,?,?,?)",
				new Object[] {
							stores.getStoreId(),
							stores.getStorename(),
							stores.getAddress(),
							stores.getPhoneno(),
							stores.getManager()
						});
	}

	// 清除数据表所有内容
	public void clearstores() {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		try {
			db.execSQL("DELETE FROM stores");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
		Log.i("Stores数据表", "数据清除了");
	}
	// 删出某一条数据
	public void deleteStkcksum(String storeId) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("delete from stores where storeId=?", new String[] { storeId });
		Log.i("Stores数据库", "成功删除");
	}
	// 修改某一条数据
		public void updateStkcksum(Stores stores) {
			SQLiteDatabase db = dDBinventory.getWritableDatabase();
			db.execSQL("update stores set storename=? where storeId=?",
					new Object[] { stores.getStorename() });
			Log.i("Stores数据库", "修改数据成功");

		}


	// 查询某一条数据
	public Stores findStkcksum(String storeId) {
		Stores stores = new Stores();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stores where storeId=?",
				new String[] { storeId });
		if (cursor.moveToFirst()) {
			String _storeId = cursor.getString(cursor.getColumnIndex("storeId")); // 1

			String storename = cursor.getString(cursor.getColumnIndex("storename")); // 2

			String address = cursor.getString(cursor.getColumnIndex("address")); // 3
			String phoneno = cursor.getString(cursor.getColumnIndex("phoneno")); // 4
			String manager = cursor.getString(cursor.getColumnIndex("manager")); // 5
			
			stores.setStoreId(_storeId);
			stores.setStorename(storename);
			stores.setAddress(address);
			stores.setPhoneno(phoneno);
			stores.setManager(manager);
		}
		Log.i("Stores数据库", "查找一条数据正确");
		cursor.close();
		return stores;
	}

	// 查找数据库两面全部数据
	public List<Stores> getAllStkcksum() {
		List<Stores> data = new ArrayList<Stores>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from stores", null);
		while (cursor.moveToNext()) {
			String _storeId = cursor.getString(cursor.getColumnIndex("storeId")); // 1
			String storename = cursor.getString(cursor.getColumnIndex("storename")); // 2
			String address = cursor.getString(cursor.getColumnIndex("address")); // 3
			String phoneno = cursor.getString(cursor.getColumnIndex("phoneno")); // 4
			String manager = cursor.getString(cursor.getColumnIndex("manager")); // 5
			
			Stores stores = new Stores();
			
			stores.setStoreId(_storeId);
			stores.setStorename(storename);
			stores.setAddress(address);
			stores.setPhoneno(phoneno);
			stores.setManager(manager);

			data.add(stores);
		}
		Log.i("Stores数据库", "查找所有数据正确");
		cursor.close();
		return data;
	}

}
