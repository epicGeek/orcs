package com.nokia.ices.apps.fusion.ems.event;

import java.text.ParseException;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.ems.domain.EmsCheckJob;

@RepositoryEventHandler(EmsCheckJob.class)
public class EmsCheckJobEventHandler {

	

	@HandleBeforeCreate
	public void handleBeforeCreate(EmsCheckJob EmsCheckJob) {
		try {
			generateMissed(EmsCheckJob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@HandleBeforeSave
	public void handleBeforeSave(EmsCheckJob EmsCheckJob) {
		try {
			generateMissed(EmsCheckJob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private static void generateMissed(EmsCheckJob EmsCheckJob)
			throws ParseException {
		System.out.println("+++++++++++++++++++++++++++++++++++++"+EmsCheckJob.getExecDate());
		EmsCheckJob.setNextDate(EmsCheckJob.getExecDate());
	}
	
	
	
}
