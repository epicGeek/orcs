package com.nokia.ices.apps.fusion.subtool.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jms.consumer.DHSSCommonMessageConsumer;
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolRepository;
import com.nokia.ices.apps.fusion.subtool.service.impl.SubtoolResultServiceImpl;

@Component
public class SubtoolMessageConsumer extends DHSSCommonMessageConsumer {

	public static Map<String,String> cacheCheckName = new HashMap<>();

	@Autowired
	SubtoolRepository subtoolRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(SubtoolMessageConsumer.class);

    @JmsListener(destination = SubtoolResultServiceImpl.SRCQ_NAME_UNIT, containerFactory = "jmsContainerFactory")
    public void receiveMessage(Message message) {
    	
        Map<String, String> receivedMap = convertMessageToMap(message);
        String sessionid = String.valueOf(receivedMap.get("sessionid"));
        String src = String.valueOf(receivedMap.get("src"));
        String resultCode = String.valueOf(receivedMap.get("flag"));
        logger.debug("消息返回 resultCode:{},sessionid:{}",resultCode,sessionid);
        CheckSubtoolResult  sub = new CheckSubtoolResult();
        String flag = "0";//默认返回成功
        try{
        	String number_checkName = cacheCheckName.get(sessionid);
        	if(resultCode.equalsIgnoreCase(RESULT_CODE_SUCCESS)) {//0表示成功
        		if(src.indexOf("/")!=-1){
        			src = src.substring(src.indexOf("/")+1, src.length()); 
        		}
        		String filePath =  ProjectProperties.getCOMP_BASE_PATH()+src;
        		logger.debug("报文路径：>>>>>>>>>"+filePath);
        		String neName = number_checkName.split(",")[3];
        		String []  result = FileReader(filePath,neName);
        		String messages = null;
        		if(StringUtils.isNotEmpty(result[1])){
        			//说明命令执行成功,并检查返回内容是否有错误信息存在
        			messages = result[1];
        			if(messages.contains("COMMAND EXECUTION FAILED") ||messages.contains("error")||messages.contains("ERROR")){
        				flag = "1";
        			}else{
        				flag = "0";
        			}
        		}else{
        			//命令执行失败
        			messages = result[0];
        			flag = "1";
        		}
        		sub.setExeResults(flag);
        		sub.setErrorMessage(messages);
        	}else{
        		String msg = String.valueOf(receivedMap.get("msg"));
        		sub.setExeResults("1");
        		sub.setErrorMessage(msg);
        		logger.debug("msg---------"+msg);
        	}
        	
        	logger.debug("number_checkName =--------"+number_checkName);
        	sub.setFilePath(src);
        	sub.setCheckName(number_checkName.split(",")[1]);
        	sub.setCreateTime(new Date());
        	sub.setCreateName(number_checkName.split(",")[2]);
        	sub.setUserNumber(number_checkName.split(",")[0]);
        	subtoolRepository.save(sub);//持久化数据
        	//删除缓存
        	cacheCheckName.remove(sessionid);
        	
        	logger.debug("subtool add data success...........");
        }catch(Exception e){
        	logger.debug("error:"+e.toString());
        }
        
        
    }
    

    /**
     * 根据返回报文路径判断是否成功
     * @param filePath
     * @return
     */
    private String[] FileReader(String filePath,String neName){
		
  		String result_error = "";
  		String result_succ = "";
  		String [] result = new String[2];
  		BufferedReader buf = null;
  		
  		try{
  			File file = new File(filePath);
  			if(file.exists()){
  				buf = new BufferedReader(new FileReader(file));
  				String linstr = "";
  				while ((linstr = buf.readLine()) != null) {
					if(linstr.contains(neName)){
						result_succ+=linstr+"\r\n";
					}else{
				        if(StringUtils.isNotEmpty(result_succ)){
				        	result_succ+=linstr+"\r\n";
				        }else{
				        	result_error +=linstr+"\r\n";
				        }
					}
  				}
  			}else{
  				logger.debug("The file does not exist");
  			}
  			result[0]=result_error;
  			result[1]=result_succ;
  			//file.delete();//删除该文件
  		}catch(Exception e){
  			logger.error(e.toString());
  		}
  		return result;
  	}

}
