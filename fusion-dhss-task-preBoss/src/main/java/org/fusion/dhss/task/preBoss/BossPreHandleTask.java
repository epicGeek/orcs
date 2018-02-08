package org.fusion.dhss.task.preBoss;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;



/**
 * 预处理boss监控数据
 *
 */
public class BossPreHandleTask{
	
	private static Logger logger = Logger.getLogger(BossPreHandleTask.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String [] PERIOD = {"minute","hour","day","month"};
	
	
	
	/**
     * boss预处理任务,每15分钟执行一次 
     */
    public static void main( String[] args )
    {
    		BossPreHandleTask.preBossData();
    }
    
    public static synchronized void taskBoss() {
		try {
			for(String period:PERIOD){
				Connection conn = connectionDB();
				logger.debug("taskBoss "+period+" begin  date at " + dateFormat.format(new Date()));
				//启动线程开始执行任务
				new Thread(new TaskBossThred(conn,period)).start();
			}
    		
    	}catch(Exception e){
    		logger.debug("execute task fail........."+e.toString());
    	}
	}
    
    private static  void preBossData() {
        
    	/**
    	 * 清理同一上级网元的重复数据
    	 */
		logger.debug("delBossData begin  date at "+ dateFormat.format(new Date()));
		Connection conn = null;
		try {
			
			SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHHmmss");
			Date endDate = new Date();
			String endTime = dft.format(endDate);
			Calendar date = Calendar.getInstance();
			date.setTime(endDate);
			date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 1);
			Date startDate = dft.parse(dft.format(date.getTime()));
			String startTime = dft.format(startDate);
			conn = connectionDB();
			if (null != conn) {
				PreparedStatement pst = conn.prepareStatement("SELECT ta.re_id,ta.hlrsn FROM(SELECT re_id,hlrsn FROM boss_soap WHERE"
						+ " response_time >= ? AND response_time < ?) ta GROUP BY ta.re_id,ta.hlrsn HAVING count(1) > 1");
				pst.setLong(1, Long.parseLong(startTime));
				pst.setLong(2, Long.parseLong(endTime));
				ResultSet resultSet = pst.executeQuery();
				
				logger.debug("ResultSet  date at "+ dateFormat.format(new Date()));
				
				// delete data
				while (resultSet.next()) {
					String reId = resultSet.getString("re_id").toString();
					String hlrsn = resultSet.getString("hlrsn").toString();
					String delSql = "DELETE FROM boss_soap  WHERE re_id =? and hlrsn=? and callback_result='success'";
					PreparedStatement dpst = conn.prepareStatement(delSql);
					dpst.setString(1, reId);
					dpst.setString(2, hlrsn);
					dpst.execute();
				}
				logger.debug("delBossData end  date at "+ dateFormat.format(new Date()));
				
				BossPreHandleTask.taskBoss();
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}finally{
			if(null!=conn){
				try {
					
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.debug("connection fail..."+e.toString());
				}
				conn = null;
			}
		}
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
	    }catch (Exception e) {
	    	logger.debug(" db connection fail..."+e.toString());
	    }
	    return conn;
	  }
    
    
    public static  String getPropertieValue(String key){
    	InputStream inFile = null;
		String value = null;
		try{
			inFile = BossPreHandleTask.class.getResource("/db.properties").openStream();
			Properties props = new Properties(); 
			props.load(inFile);
			value= (String) props.get(key);
		}catch (Exception e) {
			logger.debug("get properties fail..."+e.toString());
		}
		return value;
    }
    
}



