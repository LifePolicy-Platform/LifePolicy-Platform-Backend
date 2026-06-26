package maventest.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * JDBC 工具：以 Connection → Statement → ResultSet 查詢，回傳 List&lt;Map&lt;String, String&gt;&gt;。
 */
public final class DBUtils {

    private static final String JDBC_DRIVER;
    private static final String JDBC_URL;
    private static final String JDBC_USERNAME;
    private static final String JDBC_PASSWORD;

    static {
        Properties props = loadJdbcProperties();
        JDBC_DRIVER = props.getProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        JDBC_URL = props.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/");
        JDBC_USERNAME = props.getProperty("jdbc.username", "root");
        JDBC_PASSWORD = props.getProperty("jdbc.password", "");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBUtils() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public static List<Map<String, String>> queryList(String queryString) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> rtnList = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryString);

            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, String> dataMap = new LinkedHashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                	dataMap.put(rsmd.getColumnLabel(i), rs.getString(i));
                }
                rtnList.add(dataMap);
            }
        } catch (Exception e) {
            throw new SQLException("查詢資料發生異常, e:" + e.getMessage(), e);
        } finally {
            close(rs, stmt, conn);
        }
        return rtnList;
    }

    public static int executeUpdate(String sql) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(null, stmt, conn);
        }
    }

    private static Properties loadJdbcProperties() {
        Properties props = new Properties();
        try (InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }

    private static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
