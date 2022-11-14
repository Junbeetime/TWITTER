package twitter;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnection {
	final String ERROR = "Something wrong.";
	private String server;
	private String database;
	private String userName;
	private String password;

	public JdbcConnection() throws Exception {

		try {
			FileReader dbConfig = new FileReader("src/config/db.properties");
			Properties properties = new Properties();
			properties.load(dbConfig);
			server = properties.getProperty("server");
			database = properties.getProperty("database");
			userName = properties.getProperty("user_name");
			password = properties.getProperty("password");

			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	public Connection getConnection() {
		Connection connection = null;
		// Connect
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + server + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true", userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(ERROR);
		}

		return connection;
	}

	public boolean isExistingID(String ID) {
		String sql = " SELECT * FROM userinfo WHERE userId = '" + ID + "'";

		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;

		try {
			preparedStatement = connection.prepareStatement(sql);
			selectResult = preparedStatement.executeQuery();
			if (selectResult.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// DB close
		try {
			if (connection != null) {
				connection.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
		}
		return false;
	}

	public int login(String ID, String password) {
		String sql = " SELECT * FROM userinfo WHERE userId = '" + ID + "'";

		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;

		try {
			preparedStatement = connection.prepareStatement(sql);
			selectResult = preparedStatement.executeQuery();
			if (selectResult.next())
				if (selectResult.getString("userPassword").equalsIgnoreCase(password)) {
					return selectResult.getInt("userInfoIdx");
				}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// DB close
		try {
			if (connection != null) {
				connection.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
		}
		return -1;
	}

	public String[] profile(int ID) {
		String sql = "select user.userName,user.userLocatoin,user.userBio,user.userIdx,user.createAt,count(CASE when follow.userIdx = user.userIdx THEN 1 END) as following,count( CASE when follow.followedUser = user.userIdx THEN 1 END) as follower\r\n"
				+ "from follow,user\r\n" + "where user.userIdx = '" + ID + "';";
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;
		String[] res = new String[8];
		try {
			preparedStatement = connection.prepareStatement(sql);
			selectResult = preparedStatement.executeQuery();
			if (selectResult.next()) {
				for (int i = 1; i <= 7; i++) {
					res[i] = selectResult.getString(i);
				}
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void editProfile(int ID, String photoAdrress, String headerPhotoAdrress, String userBio, String userLocatoin,
			String userName) {
		String sqlPhoto = "insert into photo(photoAdress) value('" + photoAdrress + "')";
		String AdrressIdx1 = "SELECT LAST_INSERT_ID()";
		String sqlheadPhoto = "insert into photo(photoAdress) value ('" + headerPhotoAdrress + "')";
		String AdrressIdx2 = "SELECT LAST_INSERT_ID()";
		String ads1 = null;
		String ads2 = null;
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;
		try {
			preparedStatement = connection.prepareStatement(sqlPhoto);
			preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(AdrressIdx1);
			selectResult = preparedStatement.executeQuery();
			selectResult.next();
			ads1 = selectResult.getString(1);
			preparedStatement = connection.prepareStatement(sqlheadPhoto);
			preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(AdrressIdx2);
			selectResult = preparedStatement.executeQuery();
			selectResult.next();
			ads2 = selectResult.getString(1);
			String sql = "update user \r\n" + "set userBio = \"" + userBio + "\",photoIdx = \"" + ads1
					+ "\",headerPhotoIdx = \"" + ads2 + "\",userLocatoin = \"" + userLocatoin + "\",userName = \""
					+ userName + "\"\r\n" + "where userIdx = '" + ID + "'";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void pofileTweets(int userId) {

	}

	public void pofileHowToFollow(int userId) {

	}

	public void pofileTweetsreplies() {

	}

	public void likes() {
		String sql = "";
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;
		try {
			// 쿼리 실행
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addFollow(int userId, int otherId) {
		String sql = "insert into follow(userIdx, followedUser) values('" + userId + "','" + otherId + "')";
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		try {
			// 쿼리 실행
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rmFollow(int userId, int otherId) {
		String sql = "delete from follow where userIdx = " + userId + "and followedUser = " + otherId + ")";
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[][] viewFollowing(int userId) {
		String sql = "select followed.userName,followed.userBio, followed.userID, f.followedUser, photo.photoAdress\r\n"
				+ "from (user as followed join follow f on followed.userIdx = f.followedUser)left outer join photo on followed.photoIdx = photo.photoIdx\r\n"
				+ "where f.userIdx = " + userId;
		String sql2 = "select count(followed.userIdx) as followCount\r\n"
				+ "from user as followed join follow on followed.userIdx = follow.userIdx\r\n"
				+ "where follow.userIdx = " + userId;
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;
		String[][] str = null;
		try {
			preparedStatement = connection.prepareStatement(sql2);
			selectResult = preparedStatement.executeQuery();
			selectResult.next();
			int count = Integer.parseInt(selectResult.getString(1));
			str = new String[count][5 + 1];
			preparedStatement = connection.prepareStatement(sql);
			selectResult = preparedStatement.executeQuery();
			int j = 0;
			while (selectResult.next()) {
				for (int i = 1; i <= 5; i++) {
					str[j][i] = selectResult.getString(i);
				}
				j++;
			}
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}

	public String[][] viewFollower(int userId) {
		String sql = "select followed.userName,followed.userBio, followed.userID, f.followedUser, photo.photoAdress\r\n"
				+ "from (user as followed join follow f on followed.userIdx = f.userIdx)left outer join photo on followed.photoIdx = photo.photoIdx\r\n"
				+ "where f.followedUser =" + userId;
		String sql2 = "select count(followed.userIdx) as followCount\r\n"
				+ "from user as followed join follow on followed.userIdx = follow.userIdx\r\n"
				+ "where follow.followedUser = " + userId;
		PreparedStatement preparedStatement = null;
		Connection connection = getConnection();
		ResultSet selectResult = null;
		String[][] str = null;
		try {
			preparedStatement = connection.prepareStatement(sql2);
			selectResult = preparedStatement.executeQuery();
			selectResult.next();
			int count = Integer.parseInt(selectResult.getString(1));
			str = new String[count][5 + 1];
			preparedStatement = connection.prepareStatement(sql);
			selectResult = preparedStatement.executeQuery();
			int j = 0;
			while (selectResult.next()) {
				for (int i = 1; i <= 5; i++) {
					str[j][i] = selectResult.getString(i);
				}
				j++;
			}
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
}
