package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public interface CustomConnection {
    default Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Properties props = new Properties();
        try(InputStream in = getClass().getResourceAsStream("/database.properties")){
            props.load(in);
        }

        String host = props.getProperty("hostname");
        String username = props.getProperty("username");
        String database = props.getProperty("database");
        String password = props.getProperty("password");
        String params = props.getProperty("params");

        String url = "jdbc:mysql://" + host + "/" + database + "?" + params;

        return DriverManager.getConnection(url, username, password);
    }

    default void closeCon(Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    default void closeSt(Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

