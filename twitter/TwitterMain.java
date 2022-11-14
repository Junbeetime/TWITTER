package twitter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

public class TwitterMain {
	static final String TRY_AGAIN = "Try again";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int menu = -1;
		int userId = -1;

		System.out.println("Loading...");
		System.out.println("Connect to Ninestagram");
		System.out.println("-----------------------------------------------");
		// TODO Auto-generated method stub
		while (menu != 0) {
			System.out.println("1. login 0. Exit");
			menu = scanner.nextInt();
			if (menu == 1) {
				userId = login();
				if (userId > 0) {
					System.out.println("Login Success!");
					twitter(userId);
				} else {
					System.out.println(TRY_AGAIN);
				}
			} else if (menu == 0) {
				System.out.println("Loading...");
				break;
			} else {
				System.out.println(TRY_AGAIN);
			}
		}
	}

	public static int login() {
		Scanner scanner = new Scanner(System.in);
		int userId = -1;
		String ID = null, password = null;

		System.out.println("----------------------LOGIN---------------------");

		try {
			JdbcConnection jdbcConnection = new JdbcConnection();
			boolean flag = false;

			while (flag == false) {
				System.out.print("Enter a ID: ");
				ID = scanner.nextLine();
				flag = jdbcConnection.isExistingID(ID);
			}

			System.out.print("Enter a password: ");
			password = scanner.nextLine();
			userId = jdbcConnection.login(ID, password);

			if (userId < 0) {
				System.out.println(TRY_AGAIN);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TRY_AGAIN);
		}

		return userId;
	}

	public static void twitter(int userId) {
		Scanner scanner = new Scanner(System.in);
		int menu = -1;
		String[] pofileInfo = new String[8];
		try {
			JdbcConnection jdbcConnection = new JdbcConnection();
			while (menu != 0) {
				System.out.println("1. Profile 2. edit profile 3. following 4. follower");
				menu = scanner.nextInt();
				if (menu == 1) {
					pofileInfo = jdbcConnection.profile(userId);
					System.out.println("User Name: " + pofileInfo[1]);
					System.out.println("User Bio: " + pofileInfo[2]);
					System.out.println("User Location: " + pofileInfo[3]);
					System.out.println("USer idx: " + pofileInfo[4]);
					System.out.println("User CreatAt: " + pofileInfo[5]);
					System.out.println("User following: " + pofileInfo[6]);
					System.out.println("USer follower: " + pofileInfo[7]);
				} else if (menu == 2) {
					String photoIdx, headerPhotoIdx, userBio, userLocatoin, userName;
					System.out.printf("insert photoAdress, headerPhotoAdress, userBio, userLocatoin, userName");
					photoIdx = scanner.next();
					headerPhotoIdx = scanner.next();
					userBio = scanner.next();
					userLocatoin = scanner.next();
					userName = scanner.next();
					jdbcConnection.editProfile(userId, photoIdx, headerPhotoIdx, userBio, userLocatoin, userName);
				} else if (menu == 3) {
					String[][] str = jdbcConnection.viewFollowing(userId);
					for (int i = 0; i < str.length; i++) {
						System.out.println(Arrays.toString(str[i]));
					}
				} else if (menu == 4) {
					String[][] str = jdbcConnection.viewFollower(userId);
					for (int i = 0; i < str.length; i++) {
						System.out.println(Arrays.toString(str[i]));
					}
				} else
					break;

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TRY_AGAIN);
		}

	}

}
