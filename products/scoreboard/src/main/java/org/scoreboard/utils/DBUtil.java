/*
 * @(#) DBUtil.java 2013-10-12
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package org.scoreboard.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DBUtil
 * 
 * @author hzwangqh
 * @version 2013-10-12
 */
public class DBUtil {
	public static <T> List<T> resultSet2ObjList(ResultSet rs, Class<T> clazz)
			throws SQLException, InstantiationException, IllegalAccessException {
		// 结果集的元素对象
		ResultSetMetaData rsmd = rs.getMetaData();
		// 获取结果集的元素个数
		int colCount = rsmd.getColumnCount();
		// System.out.println("#");
		// for(int i = 1;i<=colCount;i++){
		// System.out.println(rsmd.getColumnName(i));
		// System.out.println(rsmd.getColumnClassName(i));
		// System.out.println("#");
		// }
		// 返回结果的列表集合
		List<T> list = new ArrayList<T>();
		// 业务对象的属性数组
		Field[] fields = clazz.getDeclaredFields();
		while (rs.next()) {// 对每一条记录进行操作
			T obj = clazz.newInstance();// 构造业务对象实体
			// 将每一个字段取出进行赋值
			for (int i = 1; i <= colCount; i++) {
				Object value = rs.getObject(i);
				// 寻找该列对应的对象属性
				for (int j = 0; j < fields.length; j++) {
					Field f = fields[j];
					// 如果匹配进行赋值
					if (f.getName().equalsIgnoreCase(rsmd.getColumnName(i))) {
						boolean flag = f.isAccessible();
						f.setAccessible(true);

						// 允许把返回的long值赋给int字段
						if (f.getType() == Integer.class
								&& value instanceof Long) {
							f.set(obj, ((Long) value).intValue());
						} else if (value instanceof Timestamp) {
							f.set(obj, value.toString());
						} else {
							f.set(obj, value);
						}

						f.setAccessible(flag);
					}
				}
			}
			list.add(obj);
		}
		return list;
	}

}
