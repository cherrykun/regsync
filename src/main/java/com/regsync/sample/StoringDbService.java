package com.regsync.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoringDbService {

    public void storingDbService(ArrayList<Object> list) throws Exception {
    	UserDao dao = new UserDao();
    	List<UserDto> users = dao.findAllUser();
    	for(UserDto user:users) {
    	System.out.println("id:" + user.getId());
    	System.out.println("name:" + user.getName());
    	}
    	
    	Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        //接続文字列
        String url = "jdbc:postgresql:postgres";
        String user = "tomo";
        String password = "";

        try{
            Class.forName("org.postgresql.Driver");

            //PostgreSQLへ接続
            conn = DriverManager.getConnection(url, user, password);

            //自動コミットOFF
            conn.setAutoCommit(false);

            //SELECT文の実行
            stmt = conn.createStatement();
            String sql = "SELECT 1";
            rset = stmt.executeQuery(sql);

            //SELECT結果の受け取り
            while(rset.next()){
                String col = rset.getString(1);
                System.out.println(col);
            }

            //INSERT文の実行
            sql = "INSERT INTO public.user VALUES (2, 'BBB')";
            stmt.executeUpdate(sql);
            conn.commit();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
        	disconnectDb(rset, stmt, conn);
        }
    }
    
    private static void disconnectDb(ResultSet rset, Statement stmt, Connection conn) {
    	//接続の切断
        try {
            if(rset != null)rset.close();
            if(stmt != null)stmt.close();
            if(conn != null)conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}