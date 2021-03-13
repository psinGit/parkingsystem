package com.parkit.parkingsystem.integration;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.parkit.parkingsystem.config.DataBaseConfig;

public class DBConnectionTest {
    //connection should be established to database "prod"
    DataBaseConfig dbConfig = new DataBaseConfig();
    Connection con, errCon;
    ResultSet rs;
    PreparedStatement ps;
    
    @BeforeEach
    public void getConnection(){
	try {
	    con = dbConfig.getConnection();
	} catch (ClassNotFoundException | SQLException e) {
	    System.out.println(e.getMessage());
	}
    }
    
    @Test
    public void executeSQLQueryAndCloseConnection() {
	try {
	    //check the name of the database
	    assertThat(con.getCatalog()).isEqualTo("prod");
	    ps = con.prepareStatement("select count(*) from parking");
	    rs = ps.executeQuery();
	    dbConfig.closeResultSet(rs);
	    dbConfig.closePreparedStatement(ps);
	    dbConfig.closeConnection(con);
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
    }
    
    @Test
    public void captureDBClosingError() throws SQLException {
	try {
	    //catch SQLException from closing database
	    errorClosingConnection_then_throwingException();
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    assertThat(e.getMessage()).contains("Unknown database");
	}finally {
	    con.setCatalog("prod");
	    dbConfig.closeConnection(con);
	}
    }
    
    //simulate error while closing database
    public void errorClosingConnection_then_throwingException() throws SQLException {
	//change  the database to dummy name and try to close it
	con.setCatalog("dummy");
	dbConfig.closeConnection(con);
    }
}