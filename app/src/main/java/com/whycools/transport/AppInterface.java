package com.whycools.transport;

/**
 * Created by Zero on 2016-12-05.
 */

public interface AppInterface {
    String SERVERDDRESS="http://home.whycools.com:89/";
    String LOADATA = "正在加载中...";
    String LOCATION_URL="http://api.map.baidu.com/location/ip?ak=fWrY58aTZHQPhgOhqtCItxUiOsNfGf9P&coor=bd09ll";
    String BAIDU1="http://api.map.baidu.com/geocoder/v2/";
    String BAIDU2="http://api.map.baidu.com/routematrix/v2/driving?output=json&origins=";

    String NET_NOUSE = "没有网络能干啥，去设置中开启网络吧";


    /*接口说明
    * 1.登录用到的接口
    * whycools/getAccessRight.jsp?user=&pwd=
    * 返回userid
    *
    * a1.版本更新接口
    * whycools/getFileVersion.jsp?filename=whycools.apk
    * 返回版本号
    *
    * whycools/getFileVersionDetailJson.jsp?filename=whycools.apk
    * 返回更新的内容
    *
    *update/whycools.apk
    * 下载apk
    *
    * 3.盘点表
    * rent/rProxyDsetExec.jsp?deviceid=&s=（压缩的数据）---exec dbo.UpdStoreCountDate''
    * 盘点表数据上传
    *
    * rent/rProxy.jsp?deviceid=&s=（压缩数据）----getInventory StoreId
    *
    * 4.标准表数据下载
    *rent/getBarcodeHeader.jsp?deviceid="+"&revisetime="+lasttime
    *
    * 5.发货清单数据下载
    * "rent/rProxy.jsp?deviceid=" + "&s="（数据压缩）---getShiplist4gun userid
    *
    * 6.仓库数据下载
    * "rent/rProxyDset.jsp?deviceid="+"&s="+ Zip.compress("select auto_id,storename,address,phoneno, manager from stores (nolock) where is_use=1 order by storename")
    *
    * 7.日志上传
    * update/simpleUpload.jsp?path=applog
    *
    *
    * 8.疑难解答
    *"rent/rProxy.jsp?deviceid=&s="(压缩字符串)---"shipDoneConfirm "+userid+", "+billno+", '"+date+"', a1, '"+{"地址错", "用户不在", "用户拒收","图片上传","其他"}+"'
    *
    * 9.扫描条码上传
    *  String str = listSan.get(i).getShipId() + "~"//枪id：
                        + listSan.get(i).getUserId() +"~"//用户id：817
                        + listSan.get(i).getStoreId()+"~"//仓库id	214
                        +listSan.get(i).getCarId() + "~"//车辆id： 21
                        + listSan.get(i).getBilltype() + "~"//订单类别： 出，入
                        + listSan.get(i).getBillno() + "~"//订单号8位 98742121
                        + listSan.get(i).getGoodsId() + "~"//产品id  64321
                        + listSan.get(i).getQty() + "~"//数量（出库负数，入库正数）-1,1
                        + listSan.get(i).getBarcodes() + "~"//条码：
                        + listSan.get(i).getBar69() + "~"//69码
                        + listSan.get(i).getIsInCode() + "~"//是否内机：0,1
                        + listSan.get(i).getTime();//2016-08-26 11:27:54
    * rent/rProxyDsetExec.jsp?deviceid=&s=（压缩字符串）——"exec UploadBarcodeQty '"+str+"!'"
    *
    * 10.盘点上传
    *  String str = list.get(i).getStoreid() + "~"
                            + list.get(i).getSumDate() +"~"
                            + list.get(i).getAuto_id()+"~"
                            +list.get(i).getQmyeCount() + "~"
                            + list.get(i).getQmyereal();
    *
    * rent/rProxyDsetExec.jsp?deviceid=&s=(压缩字符串)——"exec dbo.UpdStoreCountDate'"+str+"!'"
    * */


}
