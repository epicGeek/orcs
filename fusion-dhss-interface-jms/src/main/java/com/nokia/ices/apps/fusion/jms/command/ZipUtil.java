package com.nokia.ices.apps.fusion.jms.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtil {
	private static final Logger logger = Logger.getLogger(ZipUtil.class);

	// 压缩字符串
	public static String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		String result = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != gzip) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			result = out.toString("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 解压缩字符串
	public static String uncompress(String str) throws Exception {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					str.getBytes("ISO-8859-1"));
			GZIPInputStream gunzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int n;
			while ((n = gunzip.read(buffer)) != 0) {
				if (n >= 0) {
					out.write(buffer, 0, n);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			// test ok
			throw new Exception(e.getMessage());
		}
		// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
		return out.toString();
	}

	// 压缩单个或多个文件
	public static boolean zipFiles(List<String> srcfile, String dest) {
		boolean ok = false;
		byte[] buf = new byte[4096];
		try {
			File zipfile = new File(dest);
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			for (int i = 0; i < srcfile.size(); i++) {
				FileInputStream in = new FileInputStream(srcfile.get(i));
				out.putNextEntry(new ZipEntry(srcfile.get(i)));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			ok = true;
		} catch (Exception e) {
			logger.error("zipFiles error:" + e.toString());
			e.printStackTrace();
		}
		return ok;
	}

	public static void main(String[] args) throws IOException {
		List<String> srcfile = new ArrayList<String>();
		srcfile.add("D:\\deplyTest\\JWE_simple\\JWE1\\log\\log.log");
		zipFiles(srcfile, "d:/dddd.zip");
	}

}
