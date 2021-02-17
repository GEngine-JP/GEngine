package info.xiaomo.gengine.persist.mysql.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import info.xiaomo.gengine.utils.FileLoaderUtil;

/**
 * 基于阿里巴巴德鲁伊的连接池实现
 *
 * @author 张力
 */
public class DruidConnectionPool implements ConnectionPool {

    private DataSource ds;

    public DruidConnectionPool(String configFile) throws Exception {
        InputStreamReader in = FileLoaderUtil.findInputStreamByFileName(configFile);
        Properties props = new Properties();
        props.load(in);
        ds = DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 获取一个数据库连接
     * <p>
     * 连接池名称
     *
     * @return Connection
     */
    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 释放一个数据库连接
     *
     * @param connection 连接
     *                   连接池名称
     * @throws SQLException SQLException
     */
    @Override
    public void release(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public String toString() {
        return ds.toString();
    }
}
