package com.nullterminator.peerboard;

import java.sql.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class DBCompare {
	private static String url 		= "jdbc:oracle:thin:@rrdevmydb.cwa8zfgibaxd.us-west-2.rds.amazonaws.com:1521:peerbord";
	private static String username 	= "rrdevpb";
	private static String password 	= "devpeerboard";
    
	public static int authenticateLogin(String email, String pwd) throws SQLException {
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
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		

        if(uVeriPIN != 0){
            //#TODO: Send them to verification page
            return uVeriPIN;
        }

        //#TODO: Successful login, handle it at activity_login
		return 0;
    }
	
	public static int userRegister(String fname, String lname, String email, String salt, String pword) throws SQLException {
        Connection conn 	= null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn 			= DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
			
			String query1			= "SELECT MAX(u_id) max FROM users";
			Statement stmt 			= conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs			= stmt.executeQuery(query1);
			
			int u_id = 0;
			int count = 0;
			int min = 1000;
			int max = 9999;

			Random r = new Random();
			int veriPIN = r.nextInt(max - min + 1) + min;
			
			
			while (rs.next())
			{
				u_id		= rs.getInt("max") + 1;
			}
			
			String query2			= "SELECT COUNT(*) c FROM users WHERE EMAIL = ?";
			PreparedStatement stmt2 	= conn.prepareStatement(query2);
            stmt2.setString(1, email);
			
			ResultSet rs2			= stmt2.executeQuery();
			
			while (rs2.next())
			{
				count		= rs2.getInt("c");
			}
			
			if (count != 0){
				return -1;
				//TODO: Go back to SignUpActivity and set -1 to the corresponding error
			}
			

            String insrt	        = "INSERT INTO users (U_ID, EMAIL, FNAME, LNAME, SALT, PWD, VERIPIN) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt3 	= conn.prepareStatement(insrt);
            stmt3.setInt(1, u_id);
			stmt3.setString(2, email);
			stmt3.setString(3, fname);
			stmt3.setString(4, lname);
			stmt3.setString(5, salt);
			stmt3.setString(6, pword);
			stmt3.setInt(7, veriPIN);

            stmt3.executeUpdate();
			conn.commit();
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
			return -4;
        }
        catch (Exception e)
        {
            e.printStackTrace();
			return -4;
        }

        finally
        {
            if (conn != null && !conn.isClosed())
                conn.close();

        }
		
		//TODO: Send an email to user with the veriPIN using Richa's code

        //TODO: Successful signup, handle it at SignUpActivity
		return 0;
    }
}
