package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.Scanlistbak;

import java.util.ArrayList;
import java.util.List;


/**
 * 扫码上传临时文件数据库增删改查
 * @author QL
 *
 */
public class ScanlistbakService {
	DBinventory dDBinventory;
	Context context;

	public ScanlistbakService(Context context) {
		if(dDBinventory==null){
			this.dDBinventory = new DBinventory(context);
		}
		this.context = context;
	}

	// 添加数据
	public void addScanlistbak(Scanlistbak scanlistbak) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		Log.i("addScanlistbak数据库表", "添加成功");
		db.execSQL("insert into scanlistbak( "+
						"billno," + // 1
						"barcode," + // 2
						"qty," + // 3
						"postdate" + // 4
						") values(?,?,?,?)",
				new Object[] { scanlistbak.getBillno(),scanlistbak.getBarcode(),scanlistbak.getQty(),scanlistbak.getPostdate()});
	}

	// 清除数据表所有内容
	public void clearScanlistbak() {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("DELETE FROM scanlistbak");
		// Toast.makeText(context, "删除了", Toast.LENGTH_SHORT).show();
		Log.i("ScanlistbakService数据表", "清除了");
	}
	
	
	// 查询
		public Scanlistbak findScanlistbak(String scan_id) {
			Scanlistbak scanlistbak = new Scanlistbak();
			SQLiteDatabase db = dDBinventory.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from scanlistbak where scan_id=?",
					new String[] { scan_id });
			if (cursor.moveToFirst()) {
				String billno = cursor.getString(cursor.getColumnIndex("billno")); // 2
				String barcode = cursor.getString(cursor.getColumnIndex("barcode")); // 3
				String qty = cursor.getString(cursor.getColumnIndex("qty")); // 4
				String postdate = cursor.getString(cursor.getColumnIndex("postdate")); // 5
				
				
				scanlistbak.setBillno(billno);//2
				scanlistbak.setBarcode(barcode);//3
				scanlistbak.setQty(qty);//4
				scanlistbak.setPostdate(postdate);//5

			}
			cursor.close();
			return scanlistbak;
		}

		// 查找全部数据
		public List<Scanlistbak> getAllScanlistbakList() {
			List<Scanlistbak> data = new ArrayList<Scanlistbak>();
			SQLiteDatabase db = dDBinventory.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from scanlistbak", null);
			while (cursor.moveToNext()) {

				String billno = cursor.getString(cursor.getColumnIndex("billno")); // 2
				String barcode = cursor.getString(cursor.getColumnIndex("barcode")); // 3
				String qty = cursor.getString(cursor.getColumnIndex("qty")); // 4
				String postdate = cursor.getString(cursor.getColumnIndex("postdate")); // 5
				Scanlistbak scanlistbak = new Scanlistbak();

				scanlistbak.setBillno(billno);//2
				scanlistbak.setBarcode(barcode);//3
				scanlistbak.setQty(qty);//4
				scanlistbak.setPostdate(postdate);//5

				data.add(scanlistbak);
			}
			cursor.close();
			return data;
		}

		


}
