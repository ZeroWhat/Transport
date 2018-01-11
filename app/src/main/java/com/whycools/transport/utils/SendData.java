//package com.whycools.transport.utils;
//
//import android.content.Context;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.whycools.transport.AppInterface;
//import com.whycools.transport.crash.util.DateUtils;
//import com.whycools.transport.entity.ScanRec;
//import com.whycools.transport.entity.Scanlistbak;
//import com.whycools.transport.service.ScanlistService;
//import com.whycools.transport.service.ScanlistbakService;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 数据传送
// *
// * @author QL
// *
// */
//public class SendData implements AppInterface{
//	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// 数据库数据
//	List<ScanRec> listSan;
//	private ScanlistbakService scanlistbakService;
//	private ScanlistService scanlistService;
//	private String result;
//	private Context context;
//	private String barcodeStr;
//	public SendData(Context context,String barcodeStr){
//		this.context=context;
//		this.barcodeStr=barcodeStr;
//        ScanlistSync();
//	}
//	public  void ScanlistSync(){
//		scanlistService = new ScanlistService(context);
//		scanlistbakService=new ScanlistbakService(context);
//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				String url=SharedPreferencesUtil.getData(context,"ServerAddress",SERVERDDRESS).toString();
//				String url_synchronization=url+"rent/rProxyDsetExec.jsp?deviceid=&s="+getdata();
//
//
//				Log.i("扫描数据上传", "请求网络的url: "+url_synchronization);
//				result= RequestData.HttpGet(url_synchronization);
//				Message msg=new Message();
//				msg.what=1;
//				handler.sendMessage(msg);
//			}
//		}.start();
//	}
//	Handler handler=new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//
//			if (msg.what==1){
////				Toast.makeText(context, "result"+result, Toast.LENGTH_SHORT).show();
//				Log.i("上传扫描数据", "result: "+result);
////				if (result.equals("-1")){
//////						Toast.makeText(TheMenuOfActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//////					TransparentDialogUtil.showSuccessMessage(context,"上传成功");
////					List<Scanlistbak> scanlistbakdata=scanlistbakService.getAllScanlistbakList();
//////					Log.i(TAG, "添加到临时表中的总个数: "+scanlistbakdata.size());
//////					for (int j = 0; j < scanlistbakdata.size(); j++) {
//////						scanlistService.deleteScanlist(scanlistbakdata.get(j).getBarcode());
////////						Log.i(TAG, "getIsInCode存储的值: <<"+j+">>"+scanlistbakdata.get(j).getBarcode());
////////						Log.i(TAG, "删除的个数: "+j);
////////						Log.i(TAG, "删除后的结果: "+scanlistService.findisInCode(scanlistbakdata.get(j).getBarcode()));
//////					}
////				}else{
//					//上传失败的原因一般就是上传字段错误(仔细检查字段)
////						Toast.makeText(TheMenuOfActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
////					TransparentDialogUtil.showErrorMessage(context,"上传失败");
////				}
//			}
//		}
//	};
//
//	private String getdata(){
//		ScanRec scanlist=scanlistService.findScanlist(barcodeStr);
////		listSan.get(i).getShipId() + "~"//枪id：
////				+ listSan.get(i).getUserId() +"~"//用户id：817
////				+ listSan.get(i).getStoreId()+"~"//仓库id	214
////				+listSan.get(i).getCarId() + "~"//车辆id： 21
////				+ listSan.get(i).getBilltype() + "~"//订单类别： 出，入
////				+ listSan.get(i).getBillno() + "~"//订单号8位 98742121
////				+ listSan.get(i).getGoodsId() + "~"//产品id  64321
////				+ listSan.get(i).getQty() + "~"//数量（出库负数，入库正数）-1,1
////				+ listSan.get(i).getBarcodes() + "~"//条码：
////				+ listSan.get(i).getBar69() + "~"//69码
////				+ listSan.get(i).getIsInCode() + "~"//是否内机：0,1
////				+ listSan.get(i).getTime();//2016-08-26 11:27:54
//		String str= "exec UploadBarcodeQty '"+scanlist.getShipId() + "~"//枪id：
//				+ scanlist.getUserId() +"~"//用户id：817
//				+ scanlist.getStoreId()+"~"//仓库id	214
//				+scanlist.getCarId() + "~"//车辆id： 21
//				+ scanlist.getBilltype() + "~"//订单类别： 出，入
//				+ scanlist.getBillno() + "~"//订单号8位 98742121
//				+ scanlist.getGoodsId() + "~"//产品id  64321
//				+ scanlist.getQty() + "~"//数量（出库负数，入库正数）-1,1
//				+ scanlist.getBarcodes() + "~"//条码：
//				+ scanlist.getBar69() + "~"//69码
//				+ scanlist.getIsInCode() + "~"//是否内机：0,1
//				+ scanlist.getTime()+"!'";//2016-08-26 11:27:54
//		Log.i("上传压缩的字符串为", "str: "+str);
//
//		Error.contentToTxt(context,"扫描完立马上传的数据"+str);
//		return Zip.compress(str);
//
//	}
//
//	/**
//	 * 从数据库读取listview数据
//	 *
//	 * @return
//	 */
//	public String  getDataScanlist() {
//		list.clear();
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// 数据库数据
//		listSan = scanlistService.getAllScanlistList();
//
//		StringBuffer buff = new StringBuffer();
//
//		buff.append("exec UploadBarcodeQty '");
//
//		scanlistbakService.clearScanlistbak();
//		if (null != listSan && listSan.size() > 0) {
//			for (int i = 0; i < listSan.size(); i++) {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("ScanId", listSan.get(i).getScanId());
//				map.put("StoreId", listSan.get(i).getStoreId());
//				map.put("ShipId", listSan.get(i).getShipId());
//				map.put("CarId", listSan.get(i).getCarId());
//				map.put("Carname", listSan.get(i).getCarname());
//				map.put("Billtype", listSan.get(i).getBilltype());
//				map.put("Billno", listSan.get(i).getBillno());
//				map.put("GoodsId", listSan.get(i).getGoodsId());
//				map.put("Goodsname", listSan.get(i).getGoodsname());
//				map.put("Bar69", listSan.get(i).getBar69());
//				map.put("Barcodes", listSan.get(i).getBarcodes());
//				map.put("Qty", listSan.get(i).getQty());
//				map.put("Time", listSan.get(i).getTime());
//				map.put("IsInCode", listSan.get(i).getIsInCode());
//				map.put("Goodsno",listSan.get(i).getGoodsno());
//
//				list.add(map);
//				Scanlistbak scanlistbak=new Scanlistbak();
//				scanlistbak.setBillno(listSan.get(i).getBillno());
//				scanlistbak.setBarcode(listSan.get(i).getIsInCode());
//				scanlistbak.setQty(listSan.get(i).getQty());
//				scanlistbak.setPostdate(listSan.get(i).getTime());
//				scanlistbakService.addScanlistbak(scanlistbak);
////				String userid=SharedPreferencesUtil.getData(TheMenuOfActivity.this,"user","000");
//
//				Log.i("扫描数据", "枪id: "+listSan.get(i).getShipId());
//				/*
//				 * 枪id~用户id，仓库id，车id，单据类别billtype,单据号billno，产品id，数量，条码，69码，是否内机标志
//				 * (0/1)，时间
//				 */
//				String str = listSan.get(i).getShipId() + "~"//枪id：
//						+ listSan.get(i).getUserId() +"~"//用户id：817
//						+ listSan.get(i).getStoreId()+"~"//仓库id	214
//						+listSan.get(i).getCarId() + "~"//车辆id： 21
//						+ listSan.get(i).getBilltype() + "~"//订单类别： 出，入
//						+ listSan.get(i).getBillno() + "~"//订单号8位 98742121
//						+ listSan.get(i).getGoodsId() + "~"//产品id  64321
//						+ listSan.get(i).getQty() + "~"//数量（出库负数，入库正数）-1,1
//						+ listSan.get(i).getBarcodes() + "~"//条码：
//						+ listSan.get(i).getBar69() + "~"//69码
//						+ listSan.get(i).getIsInCode() + "~"//是否内机：0,1
//						+ listSan.get(i).getTime();//2016-08-26 11:27:54
//				buff.append(str);
//				buff.append("!");
//				//当前数据长度大于4000，把Scanlist数据库大于4000的数据取出来放倒临时到秒数据库里
////				Log.i(TAG, "上传字符串长度: "+buff.length()+"实际个数"+i);
//
//				if ( buff.length()>4000){
//					//清除数据库Scanlistbak所有的数据
//					List<Scanlistbak> scanlistbakdata=scanlistbakService.getAllScanlistbakList();
////					Log.i(TAG, "添加到临时表中的总个数: "+scanlistbakdata.size());
////					for (int j = 0; j < scanlistbakdata.size(); j++) {
//////						Log.i(TAG, "getBarcode: "+j+">>>"+scanlistbak.getBarcode());
////
////						 scanlistService.deleteScanlist(scanlistbakdata.get(j).getBarcode());
////						listSan.get(i).getIsInCode();
////						Log.i(TAG, "getIsInCode存储的值: <<"+j+">>"+scanlistbakdata.get(j).getBarcode());
////						Log.i(TAG, "删除的个数: "+j);
////						Log.i(TAG, "删除后的结果: "+scanlistService.findisInCode(scanlistbakdata.get(j).getBarcode()));
////					}
////					Log.i(TAG, "压缩的长度大于4000: ");
//
//					buff.append("'");
//					Log.i("要压缩的文字", buff.toString());
//					listSan.clear();
//					return Zip.compress(buff.toString());
//				}
//			}
//		}
//		buff.append("'");
////		 scanlistService.clearScanlist();
//
//		try {
//			Log.i("压缩字符串", Zip.compress(buff.toString()).toString());
//			Log.i("解压字符串", Zip.uncompress(Zip.compress(buff.toString()).toString()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
////			String date= DateUtil.getdate();//获取时间
////			Error.contentToTxt(Environment.getExternalStorageDirectory() + "/MyDownload/error.txt",date+"菜单大全同步数据字符串压缩解压缩"+e.getMessage()+"\r\n");//异常写入文档
//		}
//		listSan.clear();
//		return Zip.compress(buff.toString());
//	}
//
//
//
//
//}
