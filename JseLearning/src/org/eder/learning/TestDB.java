package org.eder.learning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDB {

	public static void main(String[] args) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DriverManager.getConnection("jdbc:derby:c:\\temp\\db");
			System.out.println("Connected to database");
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			Statement sqlCommands = con.createStatement();
			ResultSet queryResults = sqlCommands.executeQuery("select * from employees");
			showResultSet(queryResults);
			/*
			  con.setAutoCommit(false); 
			  ps =  con.prepareStatement("Insert into employees (name, age) values (?,?)" ); 
			  ps.setString(1, "Mary"); 
			  ps.setInt(2, 45); 
			  ps.executeUpdate();
			  con.commit(); ps = con.prepareStatement("select * from employees"); 
			  queryResults = ps.executeQuery(); showResultSet(queryResults);
			 */
		} catch (SQLException e) {
			if (con != null)
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			e.printStackTrace();
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private static void showResultSet(ResultSet queryResults)
			throws SQLException {
		while (queryResults.next()) {
			StringBuilder row = new StringBuilder();
			row.append(queryResults.getInt(1));
			row.append(" | ");
			row.append(queryResults.getString("name"));
			row.append(" | ");
			row.append(queryResults.getInt("age"));
			System.out.println(row.toString());
		}
	}

}
