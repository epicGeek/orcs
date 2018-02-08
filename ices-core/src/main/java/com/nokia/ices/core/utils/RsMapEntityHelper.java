/**
 * @包名 ：fes.andy.framework.db.helper<br>
 * @文件名 ：RsMapEntityHelper.java<br>
 * @类描述 ：<br>
 * @作者 ：Andy.wang<br>
 * @创建时间 ：2013-9-4上午10:02:19<br>
 * @更改人 ：<br>
 * @更改时间 ：<br>
 */
package com.nokia.ices.core.utils;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
/**
 * @包名 ：fes.andy.framework.db.helper<br>
 * @文件名 ：RsMapEntityHelper.java<br>
 * @类描述 ：ResultSet映射到实体Entity的帮助类<br>
 * @作者 ：Andy.wang<br>
 * @创建时间 ：2013-9-4上午10:02:19<br>
 * @更改人 ：<br>
 * @更改时间 ：<br>
 */
public class RsMapEntityHelper {
    /**
     * 
     * @方法名 ：rsMapEntity<br>
     * @方法描述 ：根据结果集（一条数据）映射 到 实体类<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-4上午10:09:16 <br>
     * @param <T>
     *            ：类型
     * @param clazz
     *            ：实体的Class
     * @param rs
     *            ：查询的结果集
     * @return 返回类型 ：T
     */
    public static <T> T rsMapEntity(Class<T> clazz, ResultSet rs) {
        ResultSetMetaData rsmd = null;
        String temp = "";
        Method s = null;
        T t = null;
        try {
            rsmd = rs.getMetaData();
            if (rs.next()) {
                t = clazz.newInstance();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    temp = rsmd.getColumnName(i);
                    s = clazz.getDeclaredMethod(StringHelper
                            .asserSetMethodName(StringHelper
                                    .toJavaAttributeName(temp)), String.class);
                    s.invoke(t, rs.getString(temp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }
 
    /**
     * 
     * @方法名 ：resultSetMapToEntityList<br>
     * @方法描述 ：根据结果集（多条数据）映射 到 实体类集合<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-4上午10:11:37 <br>
     * @param <T>
     *            ：泛型
     * @param clazz
     *            ：实体类的Class
     * @param rs
     *            ：查询的结果集
     * @return 返回类型 ：List<T>
     */
    public static <T> List<T> rsMapToEntityList(Class<T> clazz,
            ResultSet rs) {
        ResultSetMetaData rsmd = null;
        List<T> list = new ArrayList<T>();
        String temp = "";
        Method s = null;
        T t = null;
        try {
            rsmd = rs.getMetaData();
            while (rs.next()) {
                t = clazz.newInstance();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                	
                    temp = rsmd.getColumnName(i);
                    s = clazz.getDeclaredMethod(StringHelper
                            .asserSetMethodName(StringHelper
                                    .toJavaAttributeName(temp)), String.class);
                    s.invoke(t, rs.getString(temp));
                }
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }
}