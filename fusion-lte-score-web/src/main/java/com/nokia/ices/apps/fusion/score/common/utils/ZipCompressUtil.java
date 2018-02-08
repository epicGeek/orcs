package com.nokia.ices.apps.fusion.score.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipCompressUtil {
	public static String compress(String[] srcPath, String destPath,
			String zipFileName) {
		File zipFile = new File(destPath, zipFileName);
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);

		for (String s : srcPath) {
			File srcFile = new File(s);
			if (!srcFile.exists()) {
				throw new RuntimeException(s + "不存在！");
			}
			System.out.println(">>>>>>压缩文件【" + s + "】到【" + destPath
					+ " 】目录下<<<<<<");
			// fileSet.setDir(srcFile);
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setFile(srcFile);
			zip.addFileset(fileSet);
			zip.execute();
			//delAllFile(s);//保留ZIP，删除源文件
		}
		return destPath + zipFileName;
	}

	/**
	 * 删除文件夹下的所有文件
	 * 
	 * @param path
	 */
	public static void delAllFile(String path) {

		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException(path + "不存在！");
		}
//		try {
			FileUtils.deleteQuietly(file);
			//FileUtils.deleteDirectory(file);
//		} catch (IOException e) {
//			throw new RuntimeException("删除文件出错");
//		}

	}

	public static void delOneFile(String path) {

		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException(path + "不存在！");
		}
		file.delete();

	}

	/*
	 * 获取指定路径下文件个数；
	 * 
	 * @param filepath
	 * 
	 * @return 指定目录下的文件个数
	 */
	public int docfilecount(String filepath) {

		int count = 0;
		File file = new File(filepath);
		// 传入路径不是目录
		if (!file.isDirectory()) {
			System.out.println(" 存在的文件 ");
			count = 1;
		} else if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "" + filelist[i]);
				if (!readfile.isDirectory()) {
					count++;
				}
			}
		}
		return count;
	}

	public static List<String> docfilecountlist(String filepath) {

		List<String> docFileNameList = new ArrayList<String>();
		File file = new File(filepath);
		// 传入路径不是目录
		if (!file.isDirectory()) {
			System.out.println(" 存在的文件 ");
		} else if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {

				File readfile = new File(filepath + "" + filelist[i]);
				if (!readfile.isDirectory()) {
					docFileNameList.add(filepath + filelist[i]);
				}
			}
		}
		return docFileNameList;
	}

//	public static void main(String[] args) {
//
//		List fileList = new ArrayList();
//		ZipCompressUtil zipCompress = new ZipCompressUtil();
//		fileList = zipCompress
//				.docfilecountlist("C:/Users/JiangHao/Desktop/QQ保姆/csbm_133/csbm/");
//
//		for (Object s : fileList) {
//			System.out.println(" ----- " + (String) s);
//		}
//
//		String[] fileGroups = new String[fileList.size()];
//		fileList.toArray(fileGroups);
//		String zipFilePath = zipCompress.compress(fileGroups, "D:/bak/",
//				"test.zip");
//		System.out.println(" --***--- " + zipFilePath);
//
//	}

	public static String compress(List<String> srcPath, String destPath,
			String zipFileName) {
		File zipFile = new File(destPath, zipFileName);
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);

		for (String s : srcPath) {
			File srcFile = new File(s);
			if (!srcFile.exists()) {
				throw new RuntimeException(s + "不存在！");
			}
			System.out.println(">>>>>>压缩文件【" + s + "】到【" + destPath
					+ " 】目录下<<<<<<");
			// fileSet.setDir(srcFile);
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setFile(srcFile);
			zip.addFileset(fileSet);
			zip.execute();
			//delAllFile(s);//保留ZIP，删除源文件
		}
		return destPath + zipFileName;
	}
}
