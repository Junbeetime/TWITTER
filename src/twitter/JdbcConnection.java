package twitter;

import java.sql.*;
import java.io.FileReader;
import java.util.Properties;
import java.time.LocalDateTime;

public class JdbcConnection {
	final String ERROR = "Wrong approach!";
    private String server;
    private String database;
    private String username;
    private String password;

    public JdbcConnection() throws Exception {

        try {
            FileReader dbConfig= new FileReader("src/config/db.properties");
            Properties properties = new Properties();
            properties.load(dbConfig);
            server = properties.getProperty("server");
            database = properties.getProperty("database");
            username = properties.getProperty("user_name");
            password = properties.getProperty("password");

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
	
    // DB object
    public Connection getConnection() {
        Connection connect = null;
        // Connect
        try {
            connect = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", username, password);
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(ERROR);
        }

        return connect;
    }

    // userID
    public boolean existUserID(String userID){
        String sql = " SELECT * FROM userinfo WHERE userId = '" + userID + "'";

        PreparedStatement pstm = null;
        Connection connect = getConnection();
        ResultSet rs = null;

        try {
            pstm = connect.prepareStatement(sql);
            rs = pstm.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // DB close
        try {
            if(connect != null) {
                connect.close();
            }
            if(pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    // Email
    public boolean existEmail(String email){
        String sql = " SELECT * FROM userinfo WHERE userEmail = '" + email + "'";

        PreparedStatement pstm = null;
        Connection connect = getConnection();
        ResultSet rs = null;

        try {
            pstm = connect.prepareStatement(sql);
            rs = pstm.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // DB close
        try {
            if(connect != null) {
                connect.close();
            }
            if(pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    // phone number
    public boolean existPhoneNumber(String phoneNumber){
        String sql = " SELECT * FROM userinfo WHERE phoneNumber = '" + phoneNumber + "'";

        PreparedStatement pstm = null;
        Connection connect = getConnection();
        ResultSet rs = null;

        try {
            pstm = connect.prepareStatement(sql);
            rs = pstm.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // DB close
        try {
            if(connect != null) {
                connect.close();
            }
            if(pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    // userConfirm Email
    public boolean userConfirm(String userID, String email){
        String sql = " SELECT * FROM userinfo WHERE userEmail = '" + email + "'";

        PreparedStatement pstm = null;
        Connection connect = getConnection();
        ResultSet rs = null;

        try {
            pstm = connect.prepareStatement(sql);
            rs = pstm.executeQuery();
            if(rs.next()){
                if(rs.getString("userID").equalsIgnoreCase(userID)){
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // DB close
        try {
            if(connect != null) {
                connect.close();
            }
            if(pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    // 휴대폰 번호 회원가입
    public boolean singUpPhoneNumber(String userID, String phoneNumber, String userPassword, String userName) {
    	String sql = " INSERT INTO userinfo(userId, phoneNumber, userPassword, userName) "
                + " VALUES(?, ?, ?, ?) ";
    	
    	 Connection connect = getConnection();
         PreparedStatement ptsm = null;

         int count = 0;
         
         try {
             ptsm = connect.prepareStatement(sql);
             ptsm.setString(1, userID);
             ptsm.setString(2, phoneNumber);
             ptsm.setString(3, userPassword);
             ptsm.setString(4, userName);

             count = ptsm.executeUpdate();

         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println(ERROR);
         } finally {
             // DB close
             try {
                 if(connect != null) {
                     connect.close();
                 }
                 if(ptsm != null) {
                     ptsm.close();
                 }
             } catch (SQLException e) {
                 System.out.println(ERROR);
             }
         }
         return count > 0 ? true : false;
    }
    
    
    // 이메일 회원가입
    public boolean signUp(String userID, String email, String userPassword, String userName) {
        String sql = " INSERT INTO userinfo(userId, userEmail, userPassword, userName) "
                + " VALUES(?, ?, ?, ?) ";

        Connection connect = getConnection();
        PreparedStatement ptsm = null;

        int count = 0;

        try {
            ptsm = connect.prepareStatement(sql);
            ptsm.setString(1, userID);
            ptsm.setString(2, email);
            ptsm.setString(3, userPassword);
            ptsm.setString(4, userName);

            count = ptsm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ERROR);
        } finally {
            // DB close
            try {
                if(connect != null) {
                    connect.close();
                }
                if(ptsm != null) {
                    ptsm.close();
                }
            } catch (SQLException e) {
                System.out.println(ERROR);
            }
        }
        return count > 0 ? true : false;
    }
    
    public boolean changeUserPassword(String userID, String newUserPassword) {
        String sql = "UPDATE userInfo SET userPassword = '" + newUserPassword + "' WHERE userId = '" + userID + "'";

        Connection connect = getConnection();
        PreparedStatement ptsm = null;

        int count = 0;
        
        try {
            ptsm = connect.prepareStatement(sql);
            count = ptsm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // DB close
            try {
                if(connect != null) {
                    connect.close();
                }
                if(ptsm != null) {
                    ptsm.close();
                }
            } catch (SQLException e) {
                System.out.println(ERROR);
            }
        }
        return count > 0 ? true : false;
    }
    
    public boolean changeUserPassword(int userIdx, String newUserPassword) {
        String sql = "UPDATE userinfo SET userPassword = '" + newUserPassword + "' WHERE userInfoIdx = " + userIdx;

        Connection connect = getConnection();
        PreparedStatement ptsm = null;

        int count = 0;
        try {
            ptsm = connect.prepareStatement(sql);
            count = ptsm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // DB close
            try {
                if(connect != null) {
                    connect.close();
                }
                if(ptsm != null) {
                    ptsm.close();
                }
            } catch (SQLException e) {
                System.out.println(ERROR);
            }
        }
        return count > 0 ? true : false;
    }
    
    public int login(String userID, String password){
        String sql = " SELECT * FROM userinfo WHERE userId = '" + userID + "'";

        PreparedStatement ptsm = null;
        PreparedStatement ptsm2 = null;
        Connection connect = getConnection();
        
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        int userIdx = 0;

        try {
            ptsm = connect.prepareStatement(sql);
            ptsm2 = connect.prepareStatement(sql);
            
            rs = ptsm.executeQuery();
            rs2  = ptsm2.executeQuery(sql);
            
            if(rs.next())
                if(rs.getString("userPassword").equalsIgnoreCase(password)){
                    return rs.getInt("userInfoIdx");
                }
            
//            userIdx = rs2.getInt(1);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // DB close
        try {
            if(connect != null) {
                connect.close();
            }
            if(ptsm != null) {
                ptsm.close();
            }
        } catch (SQLException e) {
        }
        
        return userIdx;
    }
}
