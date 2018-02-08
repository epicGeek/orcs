package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.score.domain.BtsProxy;

public interface DashBoardRepository {
	
	//饼图
	public String searchMaxCycleBreakReasion();
	/**
	 * 饼图
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchCurBreakReasion(Map<String, Object> map);

	/**
	 * DASHBOARD 地市模块
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchCurAreaScore(Map<String, Object> map);
	
	public List<Map<String, Object>> searchCurWorstArea(Map<String, Object> param);

	//最后一个周期
	public String searchMaxCycleWorstArea();

	public List<Map<String, Object>> searchAreaScore(Map<String, String> map);

	//==========
	public String searchMaxCycleBtsScore(String flag);

	public List<BtsProxy> searchBtsGrade(Map<String,Object> map);
   /**
   * 诺基亚最后周期的数据
   * @return
   */
	public String searchMaxCycleAreaScoreByNokia();
	/**
	 * 第三方最后周期的数据
	 * @return
	 */
	public String searchMaxCycleAreaScoreByThird();

	public String searchMaxCycleAreaScore(Map<String, String> map);
	/**
	 * 地市-诺基亚自定义时间
	 * @return
	 */
	public String searchCycleAreaNokia();
	/**
	 * 地市-第三方自定义时间
	 * @return
	 */
	public String searchCycleAreaThrid();
	
	
	/**
	 * 故障原因 饼图地市、区县故障占比排名
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchAreaAndCityScore(Map<String, Object> map);
	
	public Double searchTotal(Map<String, Object> map);
	
	/**
	 * 地市--->地市基站个数占比折线图
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> areaGradeSearch(Map<String, Object> map);
	
	/**
	 * 地市 雷达图
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> areaAvgScore(Map<String, Object> map);
	
}
