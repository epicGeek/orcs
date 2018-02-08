package com.nokia.pgw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PGWAnalyseUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PGWAnalyseUtil.class);
	private static final String FIELD_TERMINATOR = "~";
	private static final String LINE_TERMINATOR = ";;;";
	private static final String LOAD_DETAIL_DATA_SQL = ""; // TODO write SQL
															// here.
	private static final String LOAD_XML_LOG_DATA_SQL = "";

	/**
	 * @author Pei Nan
	 * @param orinalGzFile
	 * @param destinationPath
	 * @see uncompress the orginal gz file to destination path.
	 */
	public static void uncompressGzFile(File orinalGzFile, String destinationPath) {
		// TODO need test.
		File destinationPathDir = new File(destinationPath);
		if (!destinationPathDir.exists()) {
			destinationPathDir.mkdirs();
		}
		if (!orinalGzFile.getName().contains(".gz")) {
			LOGGER.info("File extention is not gz:" + orinalGzFile.getAbsolutePath());
			return;
		}
		GZIPInputStream in = null;
		File outPutUncompressedFile = null;
		FileOutputStream out = null;
		try {
			outPutUncompressedFile = new File(destinationPath + orinalGzFile.getName().replace(".gz", ""));
			in = new GZIPInputStream(new FileInputStream(orinalGzFile));
			out = new FileOutputStream(outPutUncompressedFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			LOGGER.info("Uncompressed a new file:" + outPutUncompressedFile.getAbsolutePath());
		} catch (Exception e) {
			LOGGER.info("Fail to uncomress gz file:" + orinalGzFile.getAbsolutePath());
			e.printStackTrace();
		}
	}

	public static void uncompressGzFile(String originalGzFileAbsPath, String destinationPath) {
		File originalGzFile = new File(originalGzFileAbsPath);
		uncompressGzFile(originalGzFile, destinationPath);
	}

	public static void compressFileToGz(String originalFileAbsPath) {
		File originalFile = new File(originalFileAbsPath);
		compressFileToGz(originalFile);
	}

	private static void compressFileToGz(File originalFile) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String outFileName = originalFile.getAbsolutePath() +"_"+sdf.format(d)+ ".gz";
		FileInputStream in = null;
		try {
			in = new FileInputStream(originalFile);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the inFile:" + originalFile.getAbsolutePath());
		}

		GZIPOutputStream out = null;
		try {
			out = new GZIPOutputStream(new FileOutputStream(outFileName));
		} catch (IOException e) {
			System.out.println("Could not find the outFile:" + outFileName);

		}
		byte[] buf = new byte[10240];
		int len = 0;
		try {
			while (((in.available() > 10240) && (in.read(buf)) > 0)) {
				out.write(buf);
			}
			len = in.available();
			in.read(buf, 0, len);
			out.write(buf, 0, len);
			in.close();
			System.out.println("Completing the GZIP file:" + outFileName);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public static String getFieldTerminator() {
		return FIELD_TERMINATOR;
	}

	public static String getLineTerminator() {
		return LINE_TERMINATOR;
	}

	public static String getLoadDetailDataSQL() {
		return LOAD_DETAIL_DATA_SQL;
	}

	public static String getLoadXMLDataSQL() {
		return LOAD_XML_LOG_DATA_SQL;
	}

}
