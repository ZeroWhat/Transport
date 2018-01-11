package com.whycools.transport.utils;

import com.java4ever.apime.io.GZIP;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

/**
 * 文字压缩与上传
 * 
 * @author QL
 * 
 */
public class Zip {

	// ---------------------gzip start ------------------ 压缩
	public static byte[] GZIPCompress(byte[] data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			GZIPOutputStream stm = new GZIPOutputStream(bos);
			BufferedOutputStream out = new BufferedOutputStream(stm);
			int i = 0;
			int len = data.length;
			for (; i < len; i++) {
				out.write(data[i]);
			}
			out.flush();
			out.close();
			return bos.toByteArray();
		} catch (Exception ex) {
			return null;
		}
	}

	// ---------------------gzip start ------------------ 解压缩
	// 解压缩成byte[]

	public static byte[] GZIPUncompress(byte[] data) {
		try {
			return GZIP.inflate(data);
		} catch (Exception ex) {
			return null;
		}
	}

	// / 压缩  ///1F 1=16 F = 15   16+15 = 31 =  1F
	public static String compress(String str) {
		byte[] bb;
		String ret = "";
		try {
			bb = str.getBytes("utf-8");
			byte[] newbb = GZIPCompress(bb);
//			Log.i("压缩的byte数组为", "结果: "+ Arrays.toString(bb));
			int len1 = (newbb.length);
			for (int i = 0; i < len1; i++) {
				String hex = Integer.toHexString(newbb[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				ret += hex.toUpperCase();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	// 解压缩文字
	public static String uncompress(String ret)
			throws UnsupportedEncodingException {
		byte[] _in = new byte[ret.length() / 2];
//		Log.i("解压缩的byte数组1", "结果: "+Arrays.toString(_in));
		int len = (ret.length() / 2);

		for (int i = 0; i < len; i++) {
			_in[i] = (byte) Integer.parseInt(ret.substring(i * 2, (i + 1) * 2),
					16);
		}
		byte[] _byte = GZIPUncompress(_in);
//		Log.i("解压缩的byte数组1", "结果: "+Arrays.toString(_byte));
		return new String(_byte, "utf-8");
	}

}
