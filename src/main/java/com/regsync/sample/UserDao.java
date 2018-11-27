package com.regsync.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDao {
	private static final String USER_TABLE = "public.user";
	private static final String ID = "id";
	private static final String NAME = "name";
	// 接続文字列
	private static final String url = "jdbc:postgresql:postgres";
	private static final String user = "tomo";
	private static final String password = "";

	public List<UserDto> findAllUser() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		List<UserDto> userDto = new ArrayList<>();
		try {
			Class.forName("org.postgresql.Driver");
			// PostgreSQLへ接続
			conn = DriverManager.getConnection(url, user, password);
			// 自動コミットOFF
			conn.setAutoCommit(false);
			// SELECT文の実行
			stmt = conn.createStatement();
			String sql = "SELECT * FROM " + USER_TABLE;
			rset = stmt.executeQuery(sql);
			// SELECT結果の受け取り
			while (rset.next()) {
				UserDto dto = new UserDto();
				dto.setId(rset.getInt(ID));
				dto.setName(rset.getString(NAME));
				userDto.add(dto);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("userDto:" + userDto);
		return userDto;
	}
	
	public void insertUser() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO " + USER_TABLE + "(" + ID + "," + NAME + ") VALUES (?, ?)" ;
		try {
			Class.forName("org.postgresql.Driver");
			// PostgreSQLへ接続
			conn = DriverManager.getConnection(url, user, password);
			// 自動コミットOFF
			conn.setAutoCommit(false);
			//実行するSQLのパラメータを準備
			ps = conn.prepareStatement(sql);
//					ps.setInt(1, x);
//					ps.setString(2, x);
			// INSERT文の実行
			int i = ps.executeUpdate();
			System.out.println("結果：" + i);
			//コミット
			conn.commit();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			conn.rollback();  //ロールバックする
			e.printStackTrace();
		}
	}
}
