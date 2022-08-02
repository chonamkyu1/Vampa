package com.vam.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class JDBCTest {

	static {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testConnection() {
		try 
			(Connection conn = DriverManager.getConnection(
					"jdbc:mariadb://127.0.0.1:3306/ex",
					"root",
					"1234"
					)) {
				System.out.println("conn : " + conn);
			}
		 catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
}
