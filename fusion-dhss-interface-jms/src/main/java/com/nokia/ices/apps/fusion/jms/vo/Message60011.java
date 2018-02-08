package com.nokia.ices.apps.fusion.jms.vo;

/**
 * @author Mars
 * @date 2014-02-24
 *
 */
public class Message60011 extends MessageBase {

	// 绝对路径
	private String absoluteURL;
	// 文件名列表，多个文件名使用逗号分隔
	private String srcFilenames;
	/**
	 * 文件别名列表，多个文件名使用逗号分隔， 参数可以是空值 1.如果此参数不是空表示需要对下载的文件重命名，
	 * 此时文件名的个数必须和srcFilenames参数文件名个数相同 2.如果不需要对下载的文件重命名，此参数必须填写空值
	 */
	private String aliasFilenames;

	/**
	 * 压缩文件名，参数可以为空 如果不需要压缩下载后的文件，此参数必须是空值
	 */
	private String compressedFilename;
	/**
	 * 下载文件超时时间，单位：分钟 有效范围：10----60
	 */
	private int exeTimeoutMinutes;

	public String getAbsoluteURL() {
		return absoluteURL;
	}

	public void setAbsoluteURL(String absoluteURL) {
		this.absoluteURL = absoluteURL;
	}

	public String getSrcFilenames() {
		return srcFilenames;
	}

	public void setSrcFilenames(String srcFilenames) {
		this.srcFilenames = srcFilenames;
	}

	public String getAliasFilenames() {
		return aliasFilenames;
	}

	public void setAliasFilenames(String aliasFilenames) {
		this.aliasFilenames = aliasFilenames;
	}

	public String getCompressedFilename() {
		return compressedFilename;
	}

	public void setCompressedFilename(String compressedFilename) {
		this.compressedFilename = compressedFilename;
	}

	public int getExeTimeoutMinutes() {
		return exeTimeoutMinutes;
	}

	public void setExeTimeoutMinutes(int exeTimeoutMinutes) {
		this.exeTimeoutMinutes = exeTimeoutMinutes;
	}

}
