package org.fusion.dhss.task.neLog;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;


/**
 * 发送协议类型为SHH2的单元进行日志获取
 */

public class SendNeLogTask{
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(SendNeLogTask.class);
	
    public static void main( String[] args ){
    	
    		SendNeLogTask.sendNeLogTasks();
    }
    
    /**
     * 定时向执行机发送获取单元日志任务，默认每天凌晨2点开始
     */
    public static void  sendNeLogTasks() {
    	logger.debug("sendNeLog unit check task begin at " + dateFormat.format(new Date()));
        String scriptName = "DHLR_NE_LOG";
        String unitSql ="SELECT b.unit_name,a.ne_name,a.ne_type,b.unit_type,b.root_password FROM equipment_unit b "
        		      + "JOIN equipment_ne a ON a.id = b.ne_id AND b.server_protocol='SSH'"; 
        Connection con = null;
        try {
        	con = connectionDB();
        	ResultSet result =  con.createStatement().executeQuery(unitSql);
        	int count = 0;
        	//add data
			Statement st =con.createStatement();
				while(result.next()){
					count++;
					String unitName = result.getString("unit_name");
					String unit_type_name = result.getString("unit_type");
					String rootPwd = result.getString("root_password");
					String ne_name = result.getString("ne_name");
					String ne_type_name = result.getString("ne_type");
					
					String pwd = Hex.encodeHexString(getPropertieValue("dhss.target.pwd").getBytes());
					
					StringBuffer commandParams = new StringBuffer();
					
					commandParams.append(unitName).append("," + pwd).append("," + rootPwd)
					.append("," + getPropertieValue("dhss.target.user") + "@" +getPropertieValue("dhss.target.path"));
					
					new Thread(new SendNeLogThread(scriptName,unitName, commandParams.toString())).start();
					//java.sql.Date date = new java.sql.Date(new Date().getTime());
					Date date = new Date();
					String exesql = "INSERT INTO equipment_ne_operation_log(give_time,ne_name,ne_type,unit_name,unit_type) VALUES('"+dateFormat.format(date)+"','"+ne_name
							+"','"+ne_type_name+"','"+unitName+"','"+unit_type_name+"')";
					st.addBatch(exesql);
					if(count==1){
						break;
					}
				}
				//批量添加
				st.executeBatch();
			
		} catch (SQLException e) {
			logger.debug("serach Data fail......"+e.toString());
		}
        
        logger.debug("sendNeLog unit check task end at " + dateFormat.format(new Date()));
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
	    	logger.debug("connection db fail..."+e.toString());
	    }
	    return conn;
	  }
    
    
    public static  String getPropertieValue(String key){
    	InputStream inFile = null;
		String value = null;
		try{
			inFile = SendNeLogTask.class.getResource("/db.properties").openStream();
			Properties props = new Properties(); 
			props.load(inFile);
			value= (String) props.get(key);
		}catch (Exception e) {
			logger.debug("get properties fail......"+e.toString());
		}
		return value;
    }
}
