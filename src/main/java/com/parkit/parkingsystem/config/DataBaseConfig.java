package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		Properties props = new Properties();
		FileInputStream fis = null;

		try {

			fis = new FileInputStream("jdbc.properties");
			props.load(fis);
		} catch (IOException e) {
			logger.error("Error when read jdbc.properties", e);
		} finally {

			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex) {
				logger.error("Error closing file properties", ex);
			}
		}
    	
        logger.info("Create DB connection");
		Class.forName(props.getProperty("driver"));
		return DriverManager.getConnection(props.getProperty("urlprod"), props.getProperty("username"),
				props.getProperty("password"));//
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
