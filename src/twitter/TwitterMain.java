package twitter;

import java.util.Scanner;

public class TwitterMain {
	static final String WrongInput = "Something wrong. Try again.";
	
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        
        int option = -1;
        int userIdx = -1;

        System.out.println("· · · Connecting to Twitter · · ·");
        System.out.println("----------------------------------");

        while (option != 0){
            System.out.println("| (1) Sign Up | (2) Login | (3) Change Password | (0) Exit |");
            option = scanner.nextInt();
            if(option == 1){
                signUp();
            } else if(option == 2){
                userIdx = login();
                if(userIdx > 0){
                    System.out.println("Login Success!");
                    twitter(userIdx);
                } else {
                    System.out.println(WrongInput);
                }

            } else if(option == 3){
            	changePasswordNotLogin();
                option = -1;
            } else if(option == 0) {
                System.out.println("· · · · · Loading · · · · ·");
                break;
            } else {
                System.out.println(WrongInput);
            }
        }
        System.out.println("Quit Twitter");
        System.out.println("----------------------------------");
    }
    
    public static void signUp() {
        Scanner scanner = new Scanner(System.in);
        String userID = null, userPassword = null, email = null, phoneNumber = null, userName = null;
        int selection = 0;
        
        System.out.println("----------- Create an account -----------");
        System.out.println("| (1) Sign up to email | (2) Sign up to phone number |");
        selection = scanner.nextInt();
        scanner.nextLine();
        
       // 이메일로 회원가입
       if (selection == 1) {
    	   try {
               JdbcConnection JdbcConnection = new JdbcConnection();
               boolean flag = false;

               while(!flag){
                   System.out.println("| Enter your ID | ");
                   userID = scanner.nextLine();
                   flag = !JdbcConnection.existUserID(userID);
               }

               flag = false;

               while(!flag){
                   System.out.println("| Enter your email | ");
                   email = scanner.nextLine();
                   flag = !JdbcConnection.existEmail(email);
               }
               
               flag = false;
               
               while(!flag){
                   System.out.println("| Enter your name | ");
                   userName = scanner.nextLine();
                   flag = !JdbcConnection.existEmail(userName);
               }

               System.out.println("| Enter your password | ");
               userPassword = scanner.nextLine();
               flag = JdbcConnection.signUp(userID, email, userPassword, userName);
               
               
               if(flag) {
                   System.out.println("Welcome to Twitter!");
               }
           } catch (Exception e){
               e.printStackTrace();
               System.out.println(WrongInput);
           }
       } // 휴대폰 번호로 회원가입
       else {
    	   try {
               JdbcConnection JdbcConnection = new JdbcConnection();
               boolean flag = false;

               while(!flag){
                   System.out.println("| Enter your ID | ");
                   userID = scanner.nextLine();
                   flag = !JdbcConnection.existUserID(userID);
               }

               flag = false;

               while(!flag){
                   System.out.println("| Enter your phone number | ");
                   phoneNumber = scanner.nextLine();
                   flag = !JdbcConnection.existPhoneNumber(phoneNumber);
               }
               
               flag = false;
               
               while(!flag){
                   System.out.println("| Enter your name | ");
                   userName = scanner.nextLine();
                   flag = !JdbcConnection.existPhoneNumber(userName);
               }

               System.out.println("| Enter your password | ");
               userPassword = scanner.nextLine();
               flag = JdbcConnection.singUpPhoneNumber(userID, phoneNumber, userPassword, userName);
               
               if(flag) {
                   System.out.println("Welcome to Twitter!");
               }
           } catch (Exception e){
               e.printStackTrace();
               System.out.println(WrongInput);
           }
       }     
    }
    
    public static int login() {
        Scanner scanner = new Scanner(System.in);
        
        int userIdx = -1;
        String userID = null, userPassword = null;

        System.out.println("----------- Log in to Twitter -----------");
        
        try{
            JdbcConnection JdbcConnection = new JdbcConnection();
            boolean flag = false;

            while(flag == false){
                System.out.println("| Enter your ID | ");
                userID = scanner.nextLine();
                flag = JdbcConnection.existUserID(userID);
            }

            System.out.println("| Enter your password |");
            userPassword = scanner.nextLine();

            userIdx = JdbcConnection.login(userID, userPassword);

            if(userIdx < 1) {
                System.out.println(WrongInput);
            }  

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(WrongInput);
        }

        return userIdx;
    }

    public static void changePasswordNotLogin(){
        Scanner scanner = new Scanner(System.in);
        String userID = null, userPassword = null, email = null;

        System.out.println("----------- Setting a new password -----------");

        try{
        	JdbcConnection dbConnection = new JdbcConnection();
            boolean flag = false;

            while(flag == false){
                System.out.println("| Enter your ID |");
                userID = scanner.nextLine();
                flag = dbConnection.existUserID(userID);
            }

            flag = false;

            while(flag == false){
                System.out.println("| Enter your email |");
                email = scanner.nextLine();
                flag = dbConnection.userConfirm(userID, email);
            }

            System.out.println("| Enter your new password |");
            userPassword = scanner.nextLine();

            flag = dbConnection.changeUserPassword(userID, userPassword);

            if(flag) {
                System.out.println("Password's changed!");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(WrongInput);
        }
    }

    public static void changePasswordLogin(int userIdx){
        Scanner scanner = new Scanner(System.in);
        String userPassword = null;

        System.out.println("----------- Setting a new password -----------");

        try{
        	JdbcConnection JdbcConnection = new JdbcConnection();
            boolean flag = false;

            System.out.println("| Enter your new password |");
            userPassword = scanner.nextLine();
            flag = JdbcConnection.changeUserPassword(userIdx, userPassword);

            if(flag) {
                System.out.println("Password's changed!");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(WrongInput);
        }
    }
    
    public static void twitter(int userIdx){
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        
        while(option != 0){
            System.out.println("| (1) Following | (2) Follower | (3) Timeline | (4) Write | (5) Change Password | (6) Logout |");
            option = scanner.nextInt();

            if(option == 1){
               //여기서 팔로우할 닉네임 입력값 받기 
               //(로그인 한 사람의 userID)
               //insert to JDBC로 닉네임 넘겨서 리스트에 추가
               //팔로잉 리스트 JDBCconnect로 넘기기 (리스트 뽑기)
            } else if(option == 2){
               //팔로우 리스트 JDBCconnect 넘기기 (리스트 뽑기) 
               //(팔로우한nickname 사람의 userID)
            } else if(option == 3){

            } else if(option == 4){

            } else if(option == 5){
            	changePasswordLogin(userIdx);
            } else if(option == 6) {
            	break;
            }
        }
        System.out.println("Logout a Twitter");
        System.out.println("-----------------------------------------------");
    }

}
