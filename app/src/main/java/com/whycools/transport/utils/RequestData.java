package com.whycools.transport.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Zero on 2016-12-05.
 */

public class RequestData {

    //发货清单
    public  static String getResult( String sqlurl){
        try {
            URL url = new URL(sqlurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();//基于HTTP协议的连接对象
            conn.setConnectTimeout(5000);//请求超时时间 5s
            conn.setRequestMethod("GET");//请求方式

            String result="";
            Log.i("网络请求响应码", "getResult: "+conn.getResponseCode());
            if(conn.getResponseCode() == 200){//响应码==200 请求成功
                InputStream inputStream = conn.getInputStream();//得到输入流
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = inputStream.read(buffer)) != -1){
                    arrayOutputStream.write( buffer, 0, len);
                }
                inputStream.close();
                arrayOutputStream.close();
                byte[] newby = Zip.GZIPUncompress(arrayOutputStream.toByteArray());
                if (newby!=null){
                    result = new String(newby, "utf-8");
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("网络请求数据错误",e.getMessage());
            return "";
        }
    }

    /**
     * get请求
     */
    public static String HttpGet(String URL) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(URL);
            HttpResponse httpResp = httpClient.execute(httpGet);
            if(httpResp.getStatusLine().getStatusCode()==200){
                return EntityUtils.toString(httpResp.getEntity(),"UTF-8");
            } else {
                return "get请求失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }finally {
            httpClient.getConnectionManager().shutdown();//释放链接
        }
    }



    //详细地址转换经纬度
    public static String  getLoglat(String url1,String city,String address){
        try {
            String strurl= url1
                    +"?address=" + URLEncoder.encode(city+address, "UTF-8")
                    +"&output=json&ak=1y0x2zQB8ucSFqfL9np4OojGHzYuvcGP&callback=showLocation";
            URL url = new URL(strurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            InputStreamReader is = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(is);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            is.close();
            conn.disconnect();
            String sResult =strBuffer.toString();
            return sResult;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("经纬度网络请求的错误>>>>>>>>>>", e.getMessage());
            return "";
        }
    }
    //距离值
    public  static String getDistanceData( String url2,String mylat,String mylng,String lat,String lng){
        try {

            String strurl = url2 +
                    URLEncoder.encode(mylat+","+mylng,
                            "UTF-8") + "&destinations=" +
                    URLEncoder.encode(lat + "," + lng, "UTF-8") +
                    "&output=json&ak=1y0x2zQB8ucSFqfL9np4OojGHzYuvcGP";
            Log.i("距离url", "strurl: "+strurl);
            URL url = new URL(strurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            InputStreamReader is = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(is);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            is.close();
            conn.disconnect();
            String sResult =strBuffer.toString();
            return sResult;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("距离值网络请求的错误", e.getMessage());
            return "";
        }
    }




}
