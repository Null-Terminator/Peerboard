package com.nullterminator.peerboard;

import java.sql.*;
import oracle.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class DBCompare {
    public static int authenticateLogin(String email, String pwd) throws SQLException {
        String url 		= "jdbc:oracle:thin:@rrdevmydb.cwa8zfgibaxd.us-west-2.rds.amazonaws.com:1521:peerbord";
        String username 	= "rrdevpb";
        String password 	= "devpeerboard";
        int u_id = -1;
        String uEmail = "";
        String uFName = "";
        String uLName = "";
        String uSalt = "";
        String uPwd = "";
        int uVeriPIN = -3;
        //This is a test comment


        Connection conn 	= null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn 			= DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);

            String query1	        = "SELECT * FROM users WHERE email = ?";
            PreparedStatement stmt 	= conn.prepareStatement(query1);
            stmt.setString(1, email);

            ResultSet rs 	= stmt.executeQuery();

            while(rs.next())
            {
                u_id 		= rs.getInt("U_ID");
                uEmail 		= rs.getString("EMAIL");
                uFName      = rs.getString("FNAME");
                uLName 		= rs.getString("LNAME");
                uSalt       = rs.getString("SALT");
                uPwd        = rs.getString("PWD");
                uVeriPIN    = rs.getInt("VERIPIN");
            }
        }
		
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return -4;
		}

        catch (SQLException se)
        {
            System.out.println("SQL Exception:");
            while(se!= null)
            {
                System.out.println("State : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error : " + se.getErrorCode());

                se = se.getNextException();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            if (conn != null && !conn.isClosed())
                conn.close();

        }

        if(uEmail == ""){
            //TODO: Send them back saying email address is not registered
            return -1;
        }

        try {
			String dbpwd = Sha1Hash.SHA1(uSalt + uPwd);
			if(!dbpwd.equals(pwd)){
				//#TODO: Send back saying wrong password
				return -2;
			}
		} catch (/*NoSuchAlgorithm*/Exception e) {
			e.printStackTrace();
		} /*catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		

        if(uVeriPIN != 0){
            //#TODO: Send them to verification page
            return uVeriPIN;
        }

        //#TODO: Successful login, handle it at activity_login
		return 0;
    }
}
