package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result;
		try {
			String dbUrl = "jdbc:postgres://bggonlbtguporu:84367c82acd74324095101e2e8bd7571d00226bff38b06f3fd7a763f50979fca@ec2-23-23-244-83.compute-1.amazonaws.com:5432/dbjkemjqp9m8oa";
			Connection connection = DriverManager.getConnection(System.getenv("DATABASE_URL"));
			PreparedStatement stmt = connection.prepareStatement("SELECT response FROM ChatLookup WHERE keyword=\'?\'");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = rs.getString(1);
			}
			rs.close();
			stmt.close();
			connection.close();
			return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		//return null;
		throw new Exception("NOT FOUND");
	}

	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
				+ "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info("dbUrl: {}", dbUrl);

		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
