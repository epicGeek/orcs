package com.dhss.app.boss.task.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;

public class GzUtils {
	/**
	 * 把GZ文件转换成普通的文本文件类，便于解析。
	 * @param gzPath GZ包的完整路径
	 * @param soapGwName SOAPGW名称
	 * @return
	 * @throws IOException
	 */

	public static File gzToFile(File gzFile) throws IOException{
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzFile.getAbsolutePath())), "utf-8"));
		File uncompressed = new File(gzFile.getAbsolutePath().replace(".gz", ".un"));
		String line = null;
		StringBuilder gzText = new StringBuilder();
		while ((line = br.readLine()) != null) {
			gzText.append(line+"\n");
		}
		br.close();
		FileUtils.writeStringToFile(uncompressed, gzText.toString());
		return uncompressed;
	}
}
