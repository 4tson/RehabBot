package mx.fortson.rehab.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import mx.fortson.rehab.RehabBot;

public class DegensDataSource {
	
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static {
        config.setJdbcUrl(RehabBot.getDBUrl());
        config.setUsername(RehabBot.getDbUsername());
        config.setPassword(RehabBot.getDbPassword());
        ds = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private DegensDataSource(){}
}
