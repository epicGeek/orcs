/*package com.nokia.ices.core.utils.db;

*//**
 * 纯jdbc连接方法
 * @author xiaojun
 *
 *//*
<<<<<<< .mine
public class MysqlDB {

//	public String url = "jdbc:postgresql://10.212.182.93:5432/dhlr";
//
//	public String user = "admin";
//
//	public String passwd = "dhlr_oam";
//
//	public String driver = "org.postgresql.Driver";
	
	public String url = "";

	public String user = "";

	public String passwd = "";

	public String driver = "";

	private Properties InitParam = new Properties();

	private static MysqlDB instance;

	public static MysqlDB newInstance() {
		if (null == instance)
			instance = new MysqlDB();
		return instance;
	}

	private MysqlDB() {
		try {
//			String filepath=ProjectProperties.getLOG_BASE_PATH()+ File.separator +ModuleDownLoadNameDefinition.DOWNLOAD_NELOG+File.separator;
//			System.out.println("文件路径=="+ProjectProperties.getLOG_BASE_PATH());
			InitParam.load(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							"./application-data-store-2nd.properties"));
		} catch (IOException ioe) {
			System.out.println("Read File Error");
		}
		url = InitParam.getProperty("spring.jdbc.2nd.datasource.url");
		user = InitParam.getProperty("spring.jdbc.2nd.datasource.username");
		passwd = InitParam.getProperty("spring.jdbc.2nd.datasource.password");
		driver = InitParam.getProperty("spring.jdbc.2nd.datasource.driver-class-name");
		System.out.println(url);

	}

	*//**
	 * 获取连接
	 * @return
	 *//*
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
		}
		try {
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			System.out.println("create new conn failure: " + url);
			e.printStackTrace();
		}
		return conn;

	}
	*//**
	 * 释放连接
	 * @param remoteStmt
	 * @param rs
	 * @param remoteRs
	 * @param conn
	 *//*
	public static void releaseConn(ResultSet remoteRs,Statement remoteStmt,Connection conn) {
		if (null != remoteRs){
			try {
				if (!remoteRs.isClosed())
					remoteRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		if (null != remoteStmt){
			try {
				remoteStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	*//**
	 * 查询某对象,组装成List<Map<String,Object>>对象
	 *//*
	public static List<Map<String,Object>> queryForList(String sql){
		List<Map<String,Object>> returnList=new ArrayList<Map<String,Object>>();
		Map<String,Object> returmMap=null;
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			ResultSetMetaData rsmd = remoteRs.getMetaData();
			int count=rsmd.getColumnCount();
			String[] name=new String[count];
			
			while (remoteRs.next()) {
				returmMap=new HashMap<String,Object>();
				for(int i=0;i<count;i++){
					name[i]=rsmd.getColumnName(i+1);
					returmMap.put(name[i], remoteRs.getString(name[i]));
				}
				returnList.add(returmMap);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		
		return returnList;
	}
	*//**
	 * 查询某对象,组装成map对象
	 *//*
	public static Map<String,Object> queryForMap(String sql){
		Map<String,Object> returmMap=new HashMap<String,Object>();
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			ResultSetMetaData rsmd = remoteRs.getMetaData();
			int count=rsmd.getColumnCount();
			String[] name=new String[count];
			
			while (remoteRs.next()) {
				for(int i=0;i<count;i++){
					name[i]=rsmd.getColumnName(i+1);
					returmMap.put(name[i], remoteRs.getString(name[i]));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		
		return returmMap;
	}
	*//**
	 * 查询总数,返回LONG型,仅返回第一列数值型内容
	 *//*
	public static int queryForInt(String sql){
		int returnNumber=0;
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			if (remoteRs.next()) {
				returnNumber=remoteRs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		return returnNumber;
	}
	*//**
	 * 查询总数,返回LONG型,仅返回第一列数值型内容
	 *//*
	public static Long queryForLong(String sql){
		Long returnNumber=0l;
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			if (remoteRs.next()) {
				returnNumber=remoteRs.getLong(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		return returnNumber;
	}
	*//**
	 * 查询某一列数据,返回字符串
	 *//*
	public static String queryForString(String sql){
		String returnStr="";
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			if (remoteRs.next()) {
				returnStr=remoteRs.getString(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		return returnStr;
	}
	
	*//**
	 * 添加数据
	 *//*
	public static boolean insertData(String sql){
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		boolean flag = false;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
            flag = remoteStmt.execute(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		return flag;
	}
	*//**
	 * 修改数据
	 *//*
	public static boolean updateData(String sql){
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		int renum=0;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			renum= remoteStmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		if(renum==0)
			return false;
		else
			return true;
	}
	*//**
	 * 删除数据
	 *//*
	public static boolean deleteData(String sql){
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		boolean flag = false;
		try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			flag = remoteStmt.execute(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
		return flag;
	}
	
	public static <T> List<T> queryForEntityList(Class<T> obj,String sql) {
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
		List<T> list = new ArrayList<T>();
        try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			RsMapEntityHelper.rsMapToEntityList(obj, remoteRs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
        return list;
    }
	
	public static <T> T queryForEntity(Class<T> obj,String sql) {
		MysqlDB remotedb = MysqlDB.newInstance();
		Connection remoteConn = null;
		Statement remoteStmt = null;
		ResultSet remoteRs = null;
        T t = null;
        try {
			remoteConn = remotedb.getConnection();
			remoteStmt = remoteConn.createStatement();
			remoteRs = remoteStmt.executeQuery(sql);
			RsMapEntityHelper.rsMapEntity(obj, remoteRs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			releaseConn(remoteRs,remoteStmt,remoteConn);
		}
        return t;
    }
}
=======
public class MysqlDB {}
>>>>>>> .r13675
*/