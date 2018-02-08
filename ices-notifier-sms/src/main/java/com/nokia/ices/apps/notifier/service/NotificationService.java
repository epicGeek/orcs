package com.nokia.ices.apps.notifier.service;

import com.nokia.ices.apps.notifier.domain.NotificationMessageRecord;

public interface NotificationService {
	void testConn();
	void sendMSG(NotificationMessageRecord message);
}
