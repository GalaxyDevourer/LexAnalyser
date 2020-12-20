package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableDAO implements CustomConnection{
    public boolean isTableExist(String tablename) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM " + tablename);

            ResultSet rs = stmt.executeQuery();
        }
        catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();

            return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return true;
    }

    public boolean isColumnExist(String tablename, String column) {
        Connection conn = null;
        PreparedStatement stmt = null;

        boolean isExist;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT COLUMN_NAME\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE table_name = '" + tablename + "' AND column_name LIKE '" + column + "'");

            ResultSet rs = stmt.executeQuery();
            isExist = rs.next();
        }
        catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();

            return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return isExist;
    }
}
