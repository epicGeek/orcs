package com.nokia.ices.apps.fusion.patrol.common;

/**
 * @author yudq
 * @date 2015年6月28日
 *
 */
//public class LuaJavaUtil {
//
//	private static final int size = 10;
//
//	static {
//		for (int i = 0; i < size; i++) {
//			LuaStateFactory.newLuaState();
//		}
//	}
//
//	private static LuaState getLuaState() {
//		int random = Double.valueOf(Math.random() * 1000).intValue();
//		int index = random % size;
//		return LuaStateFactory.getExistingState(index);
//	}
//	/**
//	 * 根据参数名称和匹配源获得匹配的结果
//	 * @param scriptName 脚本名称
//	 * @param src
//	 * @return
//	 */
//	public static String executeLua(String scriptName, String params) {
//		LuaState L = getLuaState();
//
//		synchronized (L) {
//			// 加载lua标准库,否则一些lua基本函数无法使用
//			L.openLibs();
//			// findScript
//			String scriptContent = "";// dTScriptService.getScriptContent(scriptName);
//			// doScript
//			L.LdoString(scriptContent);
//			// 找到函数 match
//			L.getField(LuaState.LUA_GLOBALSINDEX, "main");
//
//			// 参数1压栈
//		    L.pushString(params);
//			 
//			// 调用，共1个参数1个返回值
//			// L.call(1, 1);
//			int errorCode = 0;
//			errorCode = L.pcall(1, 1, 0);
//			if (0 != errorCode) {
//				RuntimeException exception = new BusinessServiceException("执行Lua脚本出错!");
//				throw exception;
//			}
//
//			// 保存返回值到result中
//			L.setField(LuaState.LUA_GLOBALSINDEX, "result");
//			// 读入result
//			LuaObject lobj = L.getLuaObject("result");
//
//			return lobj.getString();
//		}
//	}
//
//	/**
//	 * 根据参数名称和匹配源获得匹配的结果
//	 * @param scriptName 脚本名称
//	 * @param src
//	 * @return
//	 */
//	public static String executeLua(String scriptName, List<String> params) {
//		LuaState L = getLuaState();
//
//		synchronized (L) {
//			// 加载lua标准库,否则一些lua基本函数无法使用
//			L.openLibs();
//			// findScript
//			String scriptContent = "";// dTScriptService.getScriptContent(scriptName);
//			// doScript
//			L.LdoString(scriptContent);
//			// 找到函数 match
//			L.getField(LuaState.LUA_GLOBALSINDEX, "main");
//
//			// 参数1压栈
//			for (String p : params) {
//				L.pushString(p);
//			}
//			// 调用，共1个参数1个返回值
//			// L.call(1, 1);
//			int errorCode = 0;
//			errorCode = L.pcall(params.size(), 1, 0);
//			if (0 != errorCode) {
//				RuntimeException exception = new BusinessServiceException("执行Lua脚本出错!");
//				throw exception;
//			}
//
//			// 保存返回值到result中
//			L.setField(LuaState.LUA_GLOBALSINDEX, "result");
//			// 读入result
//			LuaObject lobj = L.getLuaObject("result");
//
//			return lobj.getString();
//		}
//	}
//
//	/**
//	 * 
//	 * @param content
//	 * @param src
//	 * @return
//	 */
//	public static String executeLuaFromContent(String content, List<String> params) throws Exception{
//		try {
//			LuaState L = getLuaState();
//			
//			synchronized (L) {
//				// 加载lua标准库,否则一些lua基本函数无法使用
//				//add by leisheng at 2015-7-15 修复BUG
//				if (null == L){
//					throw new Exception("Instance lua environment failed, LuaState is null");
//				}
//				// end add
//				L.openLibs();
//				// doScript
//				//add by leisheng at 2015-7-15 修复BUG
//				if (StringUtils.isBlank(content)){
//					throw new Exception("The lua script is null");
//				}
//				// end add
//				L.LdoString(content);
//				// 找到函数 match
//				L.getField(LuaState.LUA_GLOBALSINDEX, "main");
//
//				// 参数压栈
//				for (String p : params) {
//					L.pushString(p);
//				}
//				// 调用，共1个参数1个返回值
//				// L.call(1, 1);
//				int errorCode = 0;
//				errorCode = L.pcall(params.size(), 1, 0);
//				if (0 != errorCode) {
//					RuntimeException exception = new BusinessServiceException("执行Lua脚本出错! Error code: "+errorCode);
//					throw exception;
//				}
//
//				// 保存返回值到result中
//				L.setField(LuaState.LUA_GLOBALSINDEX, "result");
//				// 读入result
//				LuaObject lobj = L.getLuaObject("result");
//
//				return lobj.getString();
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	/**
//	 * 
//	 * @param content 第一个是LUA脚本
//	 * @param param 第二个参数是报文
//	 * @return
//	 */
//	public static String executeLuaFromContent(String content,  String   p) throws Exception{
//		try {
//			LuaState L = getLuaState();
//			
//			synchronized (L) {
//				// 加载lua标准库,否则一些lua基本函数无法使用
//				//add by leisheng at 2015-7-15 修复BUG
//				if (null == L){
//					throw new Exception("Instance lua environment failed, LuaState is null");
//				}
//				// end add
//				L.openLibs();
//				// doScript
//				//add by leisheng at 2015-7-15 修复BUG
//				if (StringUtils.isBlank(content)){
//					throw new Exception("The lua script is null");
//				}
//				// end add
//				L.LdoString(content);
//				// 找到函数 match
//				L.getField(LuaState.LUA_GLOBALSINDEX, "main");
//
//				// 参数压栈				 
//				L.pushString(p);
//			 
//				// 调用，共1个参数1个返回值
//				// L.call(1, 1);
//				int errorCode = 0;
//				errorCode = L.pcall(1, 1, 0);
//				if (0 != errorCode) {
//					RuntimeException exception = new BusinessServiceException("执行Lua脚本出错! Error code: "+errorCode);
//					throw exception;
//				}
//
//				// 保存返回值到result中
//				L.setField(LuaState.LUA_GLOBALSINDEX, "result");
//				// 读入result
//				LuaObject lobj = L.getLuaObject("result");
//
//				return lobj.getString();
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	 
//}
