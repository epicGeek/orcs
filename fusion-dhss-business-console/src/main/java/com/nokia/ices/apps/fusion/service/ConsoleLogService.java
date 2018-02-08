package com.nokia.ices.apps.fusion.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.console.domain.ConsoleLog;

public interface ConsoleLogService {

    Page<ConsoleLog> findConsoleLogByUnitName(Map<String, Object> searchParams,Pageable pageable);

}
