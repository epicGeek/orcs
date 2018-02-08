/**
 * @包名 ：fes.andy.framework.db.helper<br>
 * @文件名 ：StringHelper.java<br>
 * @类描述 ：<br>
 * @作者 ：Andy.wang<br>
 * @创建时间 ：2013-9-3下午12:12:14<br>
 * @更改人 ：<br>
 * @更改时间 ：<br>
 */
package com.nokia.ices.core.utils;
 
/**
 * @包名 ：fes.andy.framework.db.helper<br>
 * @文件名 ：StringHelper.java<br>
 * @类描述 ：Java属性名与Oracle字段名相互转化的帮助类<br>
 * @作者 ：Andy.wang<br>
 * @创建时间 ：2013-9-3下午12:12:14<br>
 * @更改人 ：<br>
 * @更改时间 ：<br>
 */
public class StringHelper {
    /**
     * 
     * @方法名 ：getTableNamePrefixLength<br>
     * @方法描述 ：获取数据库表名前缀的长度，不包含下划线<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-4上午10:34:47 <br>
     * @return 返回类型 ：int
     */
//    private static int getTableNamePrefixLength() {
//        return ITable.tableNamePrefix.replace("_", "").length();
//    }
 
    /**
     * 
     * @方法名 ：toJavaClassName<br>
     * @方法描述 ：将数据库表名转化为Java的实体类名<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午03:06:25 <br>
     * @param tableName
     *            ：数据库表名
     * @return 返回类型 ：String
     */
//    public static String toJavaClassName(String tableName) {
//        String name = toJavaAttributeName(tableName);
//        return name.substring(getTableNamePrefixLength());// 去掉表名前缀"mini_t_"
//    }
 
    /**
     * 
     * @方法名 ：toTableName<br>
     * @方法描述 ：将Java的实体类名转化为数据库表名<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午03:08:46 <br>
     * @param javaClassName
     *            ：java实体类名
     * @return 返回类型 ：String
     */
//    public static String toTableName(String javaClassName) {
//        return ITable.tableNamePrefix + javaClassName;
//    }
 
    /**
     * 
     * @方法名 ：toJavaAttributeName<br>
     * @方法描述 ：将数据库中列名转化为Java类的属性名（按照Java驼峰标示规范命名）<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午12:16:40 <br>
     * @param dbColumnName
     *            ：数据库表列名称
     * @return 返回类型 ：String
     */
    public static String toJavaAttributeName(String dbColumnName) {
        char ch[] = dbColumnName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (i == 0) {
                ch[i] = Character.toLowerCase(ch[i]);
            }
            if ((i + 1) < ch.length) {
                if (ch[i] == '_') {
                    ch[i + 1] = Character.toUpperCase(ch[i + 1]);
                } else {
                    ch[i + 1] = Character.toLowerCase(ch[i + 1]);
                }
            }
        }
        return new String(ch).replace("_", "");
    }
 
    /**
     * 
     * @方法名 ：toDbColumnName<br>
     * @方法描述 ：将Java类的属性名转化为数据库中列名（按照DB规范命名）<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午12:27:30 <br>
     * @param javaAttributeName
     *            ：Java类的属性名
     * @return 返回类型 ：String
     */
    public static String toDbColumnName(String javaAttributeName) {
        StringBuffer sb = new StringBuffer(javaAttributeName);
        char[] c = sb.toString().toCharArray();
        byte b = 0;
        int k = 0;
        for (int i = 0; i < c.length; i++) {
            b = (byte) c[i];
            if (b >= 65 && b <= 90) {
                sb.insert((i + k), "_");
                k++;
            }
 
        }
        return sb.toString().toLowerCase();
    }
 
    /**
     * 
     * @方法名 ：asserGetMethodName<br>
     * @方法描述 ：组装Java实体类属性的get方法名<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午01:38:49 <br>
     * @param attributeName
     *            ：Java类的属性名
     * @return 返回类型 ：String
     */
    public static String asserGetMethodName(String attributeName) {
        StringBuffer sb = new StringBuffer(16);
        char[] ch = attributeName.toCharArray();
        ch[0] = Character.toUpperCase(ch[0]);
        sb.append("get");
        sb.append(new String(ch));
        return sb.toString();
    }
 
    /**
     * 
     * @方法名 ：asserSetMethodName<br>
     * @方法描述 ：组装Java实体类属性的set方法名<br>
     * @创建者 ：Andy.wang<br>
     * @创建时间 ：2013-9-3下午01:41:54 <br>
     * @param attributeName
     *            : Java类的属性名
     * @return 返回类型 ：String
     */
    public static String asserSetMethodName(String attributeName) {
        StringBuffer sb = new StringBuffer(16);
        char[] ch = attributeName.toCharArray();
        ch[0] = Character.toUpperCase(ch[0]);
        sb.append("set");
        sb.append(new String(ch));
        return sb.toString();
    }
 
}