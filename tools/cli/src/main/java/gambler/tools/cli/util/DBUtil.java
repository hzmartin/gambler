/*
 * @(#) DBUtil.java 2013-10-12
 * 
 */
package gambler.tools.cli.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBUtil {

    private static SqlSessionFactory sf;

    public static SqlSessionFactory getSqlSessionFactory() throws IOException {
        if (sf != null) {
            return sf;
        }
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sf = new SqlSessionFactoryBuilder().build(inputStream);
        return sf;
    }

}
