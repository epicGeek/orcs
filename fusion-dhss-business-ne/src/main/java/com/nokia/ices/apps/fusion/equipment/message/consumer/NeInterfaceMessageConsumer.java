package com.nokia.ices.apps.fusion.equipment.message.consumer;

import java.util.Map;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.service.EquipmentService;
import com.nokia.ices.apps.fusion.jms.consumer.DHSSCommonMessageConsumer;

@Component
public class NeInterfaceMessageConsumer extends DHSSCommonMessageConsumer {


    private static final Logger logger = LoggerFactory.getLogger(NeInterfaceMessageConsumer.class);

    @Autowired
    private EquipmentUnitRepository equipmentUnitRepository;

    @JmsListener(destination = EquipmentService.DESQ_NAME_UNIT, containerFactory = "jmsContainerFactory")
    public void receiveMessage(Message message) {
        Map<String, String> receivedMap = convertMessageToMap(message);
        String resultCode = String.valueOf(receivedMap.get("flag"));
        String session = String.valueOf(receivedMap.get("sessionid"));
        
//        String action = String.valueOf(receivedMap.get("action"));
//        String eqType = String.valueOf(receivedMap.get("eqType"));
        
        logger.debug("消息返回 session:{},resultCode:{},eqType:{},action:{}", session, resultCode, ""/*,*/ /*action*/);
        
        if (resultCode.equalsIgnoreCase(RESULT_CODE_SUCCESS)) {//0表示成功
            logger.error("消息返回操作成功session:{},resultCode:{},eqType:{},action:{}", session, resultCode);
            EquipmentUnit unit = equipmentUnitRepository.findEquipmentUnitByUuId(session);
            if (!EquipmentService.UNIT_DELETE.equals("add")) {// 增加或修改单元,删除（禁用）无须更改
                unit.setIsForbidden(false);
                equipmentUnitRepository.save(unit);                
                logger.debug("通知集中操作修改单元成功");
            }else{
            	equipmentUnitRepository.delete(unit);
            	logger.debug("通知集中操作删除单元成功");
            }
        }
    }


}
