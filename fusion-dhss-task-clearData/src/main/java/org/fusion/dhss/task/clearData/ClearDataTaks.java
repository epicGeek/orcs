package org.fusion.dhss.task.clearData;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ClearDataTaks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {

		try {
			ClearDataTaks.class.newInstance().distinctData();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
		}

	}

	// clear data
	private void distinctData() {

		System.out.println("delBossData begin  date at "
				+ dateFormat.format(new Date()));

		String exesql = "SELECT t1.re_id as reId,t1.callback_result as result from boss_soap t1 INNER JOIN "
				+ " (SELECT t2.re_id from boss_soap t2 WHERE t2.callback_result='failure')t3"
				+ " on t1.re_id = t3.re_id AND t1.callback_result='success'";

		Connection conn = null;

		try {
			conn = connectionDB();
			if (null != conn) {
				ResultSet resultSet = conn.createStatement().executeQuery(
						exesql);
				// delete data
				Statement st = conn.createStatement();
				boolean flag = false;
				while (resultSet.next()) {
					flag = true;
					String reId = resultSet.getString("reId").toString();
					String result = resultSet.getString("result").toString();
					String delSql = "DELETE FROM boss_soap  WHERE re_id ="
							+ reId + " and callback_result=" + result;
					st.addBatch(delSql);
				}
				if (flag) {
					st.executeBatch();
					System.out.println("删除重复数据成功.................");
				} else {
					System.out.println("无重复数据.................");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("delBossData end  date at "
				+ dateFormat.format(new Date()));
	}

	private static Connection connectionDB() {
		Connection conn = null;
		try {
			String url = getPropertieValue("datasource.url");
			String userName = getPropertieValue("datasource.user");
			String pwd = getPropertieValue("datasource.password");
			String driver = getPropertieValue("datasource.driverClassName");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, userName, pwd);
		} catch (Exception e) {
			System.out.println("连接数据库失败..." + e.getMessage());
		}
		return conn;
	}

	public static String getPropertieValue(String key) {
		InputStream inFile = null;
		String value = null;
		try {
			inFile = ClearDataTaks.class.getResource("/db.properties")
					.openStream();
			Properties props = new Properties();
			props.load(inFile);
			value = (String) props.get(key);
		} catch (Exception e) {
			System.out.println("获取资源配置失败..." + e.getMessage());
		}
		return value;
	}

}