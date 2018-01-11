package com.whycools.transport.utils;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionUtil {
	public static File getFileFromServer(String paramString,
			ProgressDialog paramProgressDialog) throws Exception {
		if (Environment.getExternalStorageState().equals("mounted")) {
			HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
					paramString).openConnection();
			localHttpURLConnection.setConnectTimeout(5000);
			paramProgressDialog.setMax(localHttpURLConnection
					.getContentLength());
			InputStream localInputStream = localHttpURLConnection
					.getInputStream();
			File localFile = new File(
					Environment.getExternalStorageDirectory(), "Transport.apk");
			FileOutputStream localFileOutputStream = new FileOutputStream(
					localFile);
			BufferedInputStream localBufferedInputStream = new BufferedInputStream(
					localInputStream);
			byte[] arrayOfByte = new byte[1024];
			int i = 0;
			while (true) {
				int j = localBufferedInputStream.read(arrayOfByte);
				if (j == -1) {
					localFileOutputStream.close();
					localBufferedInputStream.close();
					localInputStream.close();
					return localFile;
				}
				localFileOutputStream.write(arrayOfByte, 0, j);
				i += j;
				paramProgressDialog.setProgress(i);
			}
		}
		return null;
	}

	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "utf-8");
			int i = parser.getEventType();
			System.out.println("版本号:" + i);
			UpdataInfo info = new UpdataInfo();
			while (i != XmlPullParser.END_DOCUMENT) {
				switch (i) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						String version = parser.nextText();
						info.setVersion(version);
					} else if ("description".equals(parser.getName())) {
						String description = parser.nextText();
						info.setDescription(description);
					} else if ("path".equals(parser.getName())) {
						String path = parser.nextText();
						info.setUrl(path);
					}
					break;
				}
				i = parser.next();
			}
			return info;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}