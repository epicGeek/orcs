package com.nokia.ices.apps.fusion.maintain.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

import com.nokia.ices.apps.fusion.maintain.domain.MaintainOperation;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainResult;

public interface MaintainService {
    public static final String MAINTAIN_QNAME = "DHSS_DAILY_MAINTAIN";
    public static final String SECURITY_QNAME = "DHSS_SECURITY";
	Page<MaintainOperation> findMaintainOperationPageBySearch(String checkName,String createBy,String categoryId,Pageable pageable);
	Page<MaintainResult> findMaintainResultPageBySearch(List<MaintainOperation> list,Pageable pageable);
    void sendChangePasswordCommandViaJcomp(ModelMap paramMap, String userName);
    void sendCommandViaJcomp(ModelMap paramMap, String userName);
    MaintainResult getMaintainResultByUUID(String session);
}
