package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.task.LoadStaticData;

public class Test {
	public static void main(String[] args) {
		String RULE_PATTERN_CUC = 
				"+ */\n"+
				"+ backup/CUC_BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
				"+ backup/CUC_Telnet_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
				"+ CUC_BOSS_ERR_CASE.log\n"+
				"+ CUC_BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
				"+ CUC_Telnet_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
				"- *";
		System.out.println(RULE_PATTERN_CUC);
	}
}
