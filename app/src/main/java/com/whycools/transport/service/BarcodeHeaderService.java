package com.whycools.transport.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whycools.transport.DBHelper.DBinventory;
import com.whycools.transport.entity.BarcodeHeader;

import java.util.ArrayList;
import java.util.List;


/**
 * 条码标准表增删改查
 * 
 * @author QL
 * 
 */
public class BarcodeHeaderService {
	DBinventory dDBinventory;
	Context context;

	public BarcodeHeaderService(Context context) {
		if(dDBinventory==null){
			this.dDBinventory = new DBinventory(context);
		}

		this.context = context;
	}
	public String lastTime(){
			String s = "";
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select max(revisetime) as revisetime from barcode_header",null);
			while (cursor.moveToNext())
			{
				if (cursor.getString(cursor.getColumnIndex("revisetime"))!=null)
				{
					s=cursor.getString(cursor.getColumnIndex("revisetime"));
				}
			}
		db.close();
		cursor.close();
			return s;


	}
//	                                                              string goodsname, string barcode_header_in, string barcode_header_out, string barcodeLeftpos, string revisetime, string bar69in, string bar69out, string minLen)

	public void addBarcodeHeader(boolean isUpdate,String goodsId, String goodsname,String barcode_header_in,String barcode_header_out,String barcodeLeftpos,String revisetime,String bar69in,String bar69out,String minLen) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		if (isUpdate){
			db.execSQL("update barcode_header set goodsname=? ,bar69in=?,bar69out=?,barcode_header_in=?,barcode_header_out=?,barcodeLeftpos=?,minLen=?,revisetime=? where goodsId=?",new Object[]{goodsname,bar69in,bar69out,barcode_header_in,barcode_header_out,barcodeLeftpos,minLen,revisetime});
		}else{

			db.execSQL(
					"insert into barcode_header(" + "goodsId," + // 1
							"goodsname," + // 2
							"barcode_header_in," + // 3
							"barcode_header_out," + // 4
							"barcodeLeftpos," + // 5
							"revisetime," + // 6
							"bar69in," + // 7
							"bar69out," + // 8
							"minLen" + // 9
							") values(?,?,?,?,?,?,?,?,?)",
					new Object[]{goodsId,goodsname,barcode_header_in,barcode_header_out,barcodeLeftpos,revisetime,bar69in,bar69out,minLen});


		}


	}


	// 添加数据
	public void addBarcodeHeader1(String goodsId, String goodsname,String bar69in,String bar69out,String barcode_header_in,String barcode_header_out,String barcodeLeftpos,String minLen,String revisetime) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();

		Log.i("BarcodeHeader数据库表", "添加成功");
		db.execSQL(
				"insert into barcode_header(" + "goodsId," + // 1
						"goodsname," + // 2
						"bar69in," + // 3
						"bar69out," + // 4
						"barcode_header_in," + // 5
						"barcode_header_out," + // 6
						"barcodeLeftpos," + // 7
						"minLen," + // 8
						"revisetime" + // 9
						") values(?,?,?,?,?,?,?,?,?)",
				new Object[]{goodsId,goodsname,bar69in,bar69out,barcode_header_in,barcode_header_out,barcodeLeftpos,minLen,revisetime});
	}

	// 清除数据表所有内容
	public void clearBarcodeHeader() {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		db.execSQL("DELETE FROM barcode_header");

	}

	// 修改
	public void updateBarcodeHeader_(BarcodeHeader barcodeHeader) {
		SQLiteDatabase db = dDBinventory.getWritableDatabase();
		 db.execSQL("update barcode_header set goodsId=? where barcode_header_in=?",new
		 Object[]{barcodeHeader.getGoodsId()});

	}

	// 查询内机识别码某一条数据
	public BarcodeHeader findBarcodeHeader_in(String barcode_header_in) {
		BarcodeHeader barcodeHeader = new BarcodeHeader();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from barcode_header where barcode_header_in=?",
				new String[] { barcode_header_in });
		if (cursor.moveToFirst()) {
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 2
			String goodsname = cursor.getString(cursor
					.getColumnIndex("goodsname")); // 3
			String bar69in = cursor.getString(cursor.getColumnIndex("bar69in")); // 4
			String bar69out = cursor.getString(cursor
					.getColumnIndex("bar69out")); // 5
			String _barcode_header_in = cursor.getString(cursor
					.getColumnIndex("barcode_header_in")); // 6
			String barcode_header_out = cursor.getString(cursor
					.getColumnIndex("barcode_header_out")); // 7
			String barcodeLeftpos = cursor.getString(cursor
					.getColumnIndex("barcodeLeftpos")); // 8
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			String revisetime = cursor.getString(cursor
					.getColumnIndex("revisetime")); // 10

			barcodeHeader.setGoodsId(goodsId);
			barcodeHeader.setGoodsname(goodsname);
			barcodeHeader.setBar69in(bar69in);
			barcodeHeader.setBar69out(bar69out);
			barcodeHeader.setBarcode_header_in(_barcode_header_in);
			barcodeHeader.setBarcode_header_out(barcode_header_out);
			barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
			barcodeHeader.setMinLen(minLen);
			barcodeHeader.setRevisetime(revisetime);

		}
		cursor.close();
		return barcodeHeader;
	}
	// 查询外机识别码某一条数据
	public BarcodeHeader findBarcodeHeader_out(String barcode_header_out) {
		BarcodeHeader barcodeHeader = new BarcodeHeader();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from barcode_header where barcode_header_out=?",
				new String[] { barcode_header_out });
		if (cursor.moveToFirst()) {

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 2
			String goodsname = cursor.getString(cursor
					.getColumnIndex("goodsname")); // 3
			String bar69in = cursor.getString(cursor.getColumnIndex("bar69in")); // 4
			String bar69out = cursor.getString(cursor
					.getColumnIndex("bar69out")); // 5
			String _barcode_header_in = cursor.getString(cursor
					.getColumnIndex("barcode_header_in")); // 6
			String _barcode_header_out = cursor.getString(cursor
					.getColumnIndex("barcode_header_out")); // 7
			String barcodeLeftpos = cursor.getString(cursor
					.getColumnIndex("barcodeLeftpos")); // 8
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			String revisetime = cursor.getString(cursor
					.getColumnIndex("revisetime")); // 10


			barcodeHeader.setGoodsId(goodsId);
			barcodeHeader.setGoodsname(goodsname);
			barcodeHeader.setBar69in(bar69in);
			barcodeHeader.setBar69out(bar69out);
			barcodeHeader.setBarcode_header_in(_barcode_header_in);
			barcodeHeader.setBarcode_header_out(_barcode_header_out);
			barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
			barcodeHeader.setMinLen(minLen);
			barcodeHeader.setRevisetime(revisetime);

		}
		cursor.close();
		return barcodeHeader;
	}


	// 查找形同goodid的全部数据
	public BarcodeHeader getAllgoodsId(String goodsId) {
		BarcodeHeader barcodeHeader = new BarcodeHeader();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header where goodsId=?", new String[]{goodsId});

		while (cursor.moveToNext()) {
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname")); // 3
			String bar69in = cursor.getString(cursor.getColumnIndex("bar69in")); // 4
			String bar69out = cursor.getString(cursor
					.getColumnIndex("bar69out")); // 5
			String barcode_header_in = cursor.getString(cursor
					.getColumnIndex("barcode_header_in")); // 6
			String barcode_header_out = cursor.getString(cursor
					.getColumnIndex("barcode_header_out")); // 7
			String barcodeLeftpos = cursor.getString(cursor
					.getColumnIndex("barcodeLeftpos")); // 8
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			String revisetime = cursor.getString(cursor.getColumnIndex("revisetime")); // 10

			barcodeHeader.setGoodsname(goodsname);
			barcodeHeader.setBar69in(bar69in);
			barcodeHeader.setBar69out(bar69out);
			barcodeHeader.setBarcode_header_in(barcode_header_in);
			barcodeHeader.setBarcode_header_out(barcode_header_out);
			barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
			barcodeHeader.setMinLen(minLen);
			barcodeHeader.setRevisetime(revisetime);
		}
		cursor.close();
		return barcodeHeader;
	}


	// 查找全部数据
	public List<BarcodeHeader> getAllBarcodeHeaderList() {
		List<BarcodeHeader> data = new ArrayList<BarcodeHeader>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header", null);
		while (cursor.moveToNext()) {

			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 2
			String goodsname = cursor.getString(cursor
					.getColumnIndex("goodsname")); // 3
			String bar69in = cursor.getString(cursor.getColumnIndex("bar69in")); // 4
			String bar69out = cursor.getString(cursor
					.getColumnIndex("bar69out")); // 5
			String barcode_header_in = cursor.getString(cursor
					.getColumnIndex("barcode_header_in")); // 6
			String barcode_header_out = cursor.getString(cursor
					.getColumnIndex("barcode_header_out")); // 7
			String barcodeLeftpos = cursor.getString(cursor
					.getColumnIndex("barcodeLeftpos")); // 8
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			String revisetime = cursor.getString(cursor
					.getColumnIndex("revisetime")); // 10

			if(!barcodeLeftpos.equals("null")&&  !minLen.equals("null")){
				BarcodeHeader barcodeHeader = new BarcodeHeader();
				barcodeHeader.setGoodsId(goodsId);
				barcodeHeader.setGoodsname(goodsname);
				barcodeHeader.setBar69in(bar69in);
				barcodeHeader.setBar69out(bar69out);
				barcodeHeader.setBarcode_header_in(barcode_header_in);
				barcodeHeader.setBarcode_header_out(barcode_header_out);
				barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
				barcodeHeader.setMinLen(minLen);
				barcodeHeader.setRevisetime(revisetime);

				data.add(barcodeHeader);
			}

		}
		cursor.close();
		return data;
	}
	public BarcodeHeader  getGooodsid(String barcode,int isin){
		BarcodeHeader barcodeHeader = new BarcodeHeader();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header", null);
//		Log.i("标准表", "个数为: "+cursor.getCount());
		int k=0;
		while (cursor.moveToNext()) {
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId"));
			barcodeHeader.setGoodsId(goodsId);
			String goodsname = cursor.getString(cursor.getColumnIndex("goodsname"));
			barcodeHeader.setGoodsname(goodsname);
			int barcodeLeftpos=1;
			int minLen=30;

			Log.i("标准表", "个数"+k++);
//			Log.i("标准表", "goodsId: "+goodsId);
			if(!cursor.getString(cursor.getColumnIndex("minLen")).equals("null")){
				 minLen = Integer.valueOf(cursor.getString(cursor.getColumnIndex("minLen")))-1;
				Log.i("标准表", "minLen: "+minLen);
			}
			if(!cursor.getString(cursor.getColumnIndex("barcodeLeftpos")).equals("null")){
				 barcodeLeftpos= Integer.valueOf(cursor.getString(cursor.getColumnIndex("barcodeLeftpos")));
				Log.i("标准表", "barcodeLeftpos: "+barcodeLeftpos);
			}


			String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in"));
			Log.i("标准表", "barcode_header_in: "+barcode_header_in);
			String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out"));
			Log.i("标准表", "barcode_header_out: "+barcode_header_out);
//			if(barcode_header_in.length()>3&&barcode.contains(barcode_header_in)){
//				Log.i(">>>>>>>>>>>>>>>>>>>>>", "<<<<<<<<<<<<<<<<<<: 正确的");
//				return goodsId;
//			}
			if(barcode.length()>=minLen){
//				Log.i("标准表", "对比结果: barcode.length()>=minLen&&isin==1			"+barcode);
//				Log.i("标准表", "barcodeLeftpos-1: "+(barcodeLeftpos-1));
//				Log.i("标准表", "barcodeLeftpos-1+barcode_header_in.length(): "+(barcodeLeftpos-1+barcode_header_in.length()));
//				Log.i("标准表", "barcode_header_in: "+barcode_header_in);


				if(barcode_header_in.length()>0&&barcode_header_in.contains("|")){
					Log.i("内机", "内机双标码: "+barcode_header_in);
					String[] strArray = barcode_header_in.split("\\|");
					for (int i = 0; i <strArray.length ; i++) {
						if(strArray[i].length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcodeLeftpos-1+strArray[i].length()).equals(strArray[i].toString())){
							barcodeHeader.setIsin("1");
							cursor.close();
							return barcodeHeader;
						}
					}

				}else if(barcode_header_in.length()>0&&barcode_header_in.length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcodeLeftpos-1+barcode_header_in.length()).equals(barcode_header_in)){
					Log.i("内机", "内机单个---------------: "+barcode_header_in);
					barcodeHeader.setIsin("1");
					cursor.close();
					return barcodeHeader;
				}else if(barcode_header_out.length()>0&&barcode_header_out.contains("|")){
					Log.i("外机", "外机双标: --------------"+barcode_header_out);
					String[] strArray = barcode_header_out.split("\\|");
					for (int i = 0; i <strArray.length ; i++) {
						if(strArray[i].length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcodeLeftpos-1+strArray[i].length()).equals(strArray[i].toString())){
							barcodeHeader.setIsin("0");
							cursor.close();
							return barcodeHeader;
						}
					}

				}else if(barcode_header_out.length()>0&&barcode_header_out.length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcodeLeftpos-1+barcode_header_out.length()).equals(barcode_header_out)){
					Log.i("外机", "外机单个: --------------"+barcode_header_out);
					barcodeHeader.setIsin("0");
					cursor.close();
					return barcodeHeader;

				}
			}
		}
		cursor.close();
		barcodeHeader.setGoodsId("0");
		barcodeHeader.setGoodsname("没有查询到");
		return barcodeHeader;
	}


	// 查找全部数据
	public String  getGoodsidBybarcode(String barcode,int isin) {

		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header", null);
		while (cursor.moveToNext()) {
			String goodsId = cursor.getString(cursor.getColumnIndex("goodsId")); // 3
			int minLen = Integer.valueOf(cursor.getString(cursor.getColumnIndex("minLen"))); // 6\
			int barcodeLeftpos;
			if(cursor.getString(cursor.getColumnIndex("barcodeLeftpos")).equals(null)){
				 barcodeLeftpos = Integer.valueOf(cursor.getString(cursor.getColumnIndex("barcodeLeftpos"))); // 6\
			}else{
				barcodeLeftpos=1;
			}


			if(isin==1){

				String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in")); // 6\


				Log.i("标准表服务", "barcode_header_in: "+barcode_header_in);
				if(barcode_header_in.contains("|")){
					Log.i("标准表服务", "双标码: "+barcode_header_in);
					String[] strArray = barcode_header_in.split("\\|");
					for (int i = 0; i <strArray.length ; i++) {
						if(minLen<=barcode.length()&&strArray[i].length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,strArray[i].length()).equals(strArray[i].toString())){
							cursor.close();
							return goodsId;
						}
					}

				}else if(minLen<=barcode.length()&&barcode_header_in.length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcode_header_in.length()).equals(barcode_header_in)){
					cursor.close();
					return goodsId;
				}
			}else{

				String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out")); // 7

				if(barcode_header_out.contains("|")){

					String[] strArray = barcode_header_out.split("\\|");
					for (int i = 0; i <strArray.length ; i++) {
						if(minLen<=barcode.length()&&strArray[i].length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,strArray[i].length()).equals(strArray[i].toString())){
							cursor.close();
							return goodsId;
						}
					}

				}else if(minLen<=barcode.length()&&barcode_header_out.length()<barcode.length()&&barcode.substring(barcodeLeftpos-1,barcode_header_out.length()).equals(barcode_header_out)){
					cursor.close();
					return goodsId;

				}

			}

		}
		cursor.close();
		return "0";
	}
	// 查找全部数据
	public List<BarcodeHeader> findInOutByGoodId(String goodsId) {
		Log.i("", "findInOutByGoodId: ");
		List<BarcodeHeader> data = new ArrayList<BarcodeHeader>();
		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header where goodsId=?", new String []{goodsId});
		while (cursor.moveToNext()) {

			String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in")); // 6
			Log.i("标准表中-------------", "barcode_header_in: "+barcode_header_in);
			String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out")); // 7
			Log.i("标准表中------------", "barcode_header_out: "+barcode_header_out);
			String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos")); // 8
			Log.i("标准表中", "barcodeLeftpos: "+barcodeLeftpos);
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			Log.i("标准表中", "minLen: "+minLen);
			BarcodeHeader barcodeHeader = new BarcodeHeader();
			barcodeHeader.setBarcode_header_in(barcode_header_in);
			barcodeHeader.setBarcode_header_out(barcode_header_out);
			barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
			barcodeHeader.setMinLen(minLen);
			data.add(barcodeHeader);
		}
		cursor.close();
		return data;
	}
	// 查找全部数据
	public BarcodeHeader findByGoodId(String goodsId) {
		Log.i("", "findInOutByGoodId: ");

		SQLiteDatabase db = dDBinventory.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from barcode_header where goodsId=?", new String []{goodsId});
		BarcodeHeader barcodeHeader = new BarcodeHeader();
		while (cursor.moveToNext()) {

			String barcode_header_in = cursor.getString(cursor.getColumnIndex("barcode_header_in")); // 6
			Log.i("标准表中", "barcode_header_in: "+barcode_header_in);
			String barcode_header_out = cursor.getString(cursor.getColumnIndex("barcode_header_out")); // 7
			Log.i("标准表中", "barcode_header_out: "+barcode_header_out);
			String barcodeLeftpos = cursor.getString(cursor.getColumnIndex("barcodeLeftpos")); // 8
			Log.i("标准表中", "barcodeLeftpos: "+barcodeLeftpos);
			String minLen = cursor.getString(cursor.getColumnIndex("minLen")); // 9
			Log.i("标准表中", "minLen: "+minLen);

			barcodeHeader.setBarcode_header_in(barcode_header_in);
			barcodeHeader.setBarcode_header_out(barcode_header_out);
			barcodeHeader.setBarcodeLeftpos(barcodeLeftpos);
			barcodeHeader.setMinLen(minLen);

		}
		cursor.close();
		return barcodeHeader;
	}






}
