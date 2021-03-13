package com.parkit.parkingsystem.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {  
 ResourceBundle bundle = ResourceBundle.getBundle("com/parkit/parkingsystem/properties/database");
    private String url = bundle.getString("db_url");
    private String usr = bundle.getString("db_user");
    private String pwd = bundle.getString("db_pwd");
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
  
    public Connection getConnection() throws ClassNotFoundException, SQLException {
	logger.info("Create DB connection");
	Class.forName("com.mysql.cj.jdbc.Driver");
	return DriverManager.getConnection(url, usr, pwd);
	//"jdbc:mysql://localhost:3306/prod?serverTimezone=Europe/Paris", "root", "rootroot");
    }

    public void closeConnection(Connection con) throws SQLException {
	if (con != null) {
	    try {
		con.close();
		logger.info("Closing DB connection");
		} catch (SQLException e) {
		    logger.error("Error while closing connection", e);
		    throw e;
		}
	}
    }

    public void closePreparedStatement(PreparedStatement ps) throws SQLException {
	if (ps != null) {
	    try {
		ps.close();
		logger.info("Closing Prepared Statement");
	    } catch (SQLException e) {
		logger.error("Error while closing prepared statement", e);
		throw e;				
	    }
	}
    }

    public void closeResultSet(ResultSet rs) throws SQLException {
	if (rs != null) {
	    try {
		rs.close();
		logger.info("Closing Result Set");
	    } catch (SQLException e) {
		logger.error("Error while closing result set", e);
		throw e;
	    }
	}
    }
}