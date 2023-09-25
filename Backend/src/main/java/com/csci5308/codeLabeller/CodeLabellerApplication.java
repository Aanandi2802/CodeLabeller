package com.csci5308.codeLabeller;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class CodeLabellerApplication {

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
//			String url = "jdbc:mysql://db-5308.cs.dal.ca:3306/CSCI5308_8_DEVINT?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
//			String username = "CSCI5308_8_DEVINT_USER";
//			String password = "umee9keiNa";
			String url = "jdbc:mysql://localhost:3306/dev?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
			String username = "root";
			String password = "1234";
			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			String sql1 = "create table if not exists users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);";
			String sql2 = "create table if not exists authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));";
			//String sql3 = "create unique index if not exists ix_auth_username on authorities (username,authority);";

			statement.execute(sql1);
			statement.execute(sql2);
			//statement.execute(sql3);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new SQLException("Couldn't connect to database.",e);
		}

		SpringApplication.run(CodeLabellerApplication.class, args);
	}

}
