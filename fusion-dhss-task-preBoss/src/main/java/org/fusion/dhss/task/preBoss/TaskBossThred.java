package org.fusion.dhss.task.preBoss;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class TaskBossThred implements Runnable {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(TaskBossThred.class);
	
	private String tabName;
	
	private Connection conn;
	
	public TaskBossThred(Connection conn,String tabName){
		this.tabName = tabName;
		this.conn = conn;
	}
	
	
    /**
     * 线程任务体
     */
	public void run() {
		
		logger.debug("execute   "+tabName+"   table..............");
		try{
				String period = null;
				if("minute".equals(tabName)){
					period =  "date_minute";
				}else if("hour".equals(tabName)){
					period = "date_hour";
				}else if("day".equals(tabName)){
					period ="date_day";
				}else if("month".equals(tabName)){
					period = "date_month";
				}else{
				}
				
				String sql ="SELECT bs.hlrsn AS hlrsn,bs.b_class AS businessType,bs."+period+" as datestr,"
				           +" sum(CASE bs.callback_result WHEN 'failure' THEN 1 ELSE 0 END) AS fail_count,"
				           +" count(bs.re_id) AS total FROM boss_soap bs GROUP BY datestr,businessType,hlrsn ORDER BY dateStr";
				if(null!=conn){
					ResultSet result = conn.createStatement().executeQuery(sql);
					//add data
					Statement st =conn.createStatement();
					while(result.next()){
						String hlrsn = result.getString("hlrsn")!=null?result.getString("hlrsn").toString():"";
						String businessType = result.getString("businessType")!=null?result.getString("businessType").toString():"";
						String datestr = result.getString("datestr")!=null?result.getString("datestr").toString():"";
						String fail_count = result.getString("fail_count")!=null?result.getString("fail_count").toString():"";
						String total = result.getString("total")!=null?result.getString("total").toString():"";
						String exesql = "INSERT INTO boss_soap_"+tabName+"_handle VALUES('"+hlrsn+"','"+businessType
								       +"','"+datestr+"',"+Integer.parseInt(fail_count)+","+Integer.parseInt(total)+")";
						st.addBatch(exesql);
					}
					st.execute("TRUNCATE TABLE boss_soap_"+tabName+"_handle");
					st.executeBatch();
				}else{
					logger.debug("db connection fail...");
				}
				logger.debug("handle success..........."+tabName+"_handle   " + dateFormat.format(new Date()));
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("handle boss error.........."+e.toString());
		}finally{
			if(null!=conn){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.debug("db connection close....."+e.toString());
				}
				conn = null;
			}
		}
         
	}
	

}
