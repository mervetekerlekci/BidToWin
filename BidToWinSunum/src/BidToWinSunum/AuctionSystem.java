package BidToWinSunum;

import java.util.Scanner;

import java.util.Scanner;

public class AuctionSystem {

    public static Scanner scanner = new Scanner(System.in);
    public static int userType;
    public static String userEmail;
    public static int authCount = 0;
    public static boolean isAuth = false;

    public static void EnterNewUserInfo() {

        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Adınız: ");
        String name = scanner1.nextLine();
        System.out.println("Adresiniz: ");
        String address = scanner1.nextLine();
        System.out.println("E-posta adresiniz: ");
        String email = scanner1.nextLine();
        int memberId = DatabaseManager.getMaxUserId() + 1;
        System.out.println("Telefon numaranız: ");
        String phoneNumber = scanner1.nextLine();
        System.out.println("Şifreniz: ");
        String password = scanner1.nextLine();
        System.out.println("Satıcı olarak katılmak istiyorsanız 0, Alıcı olarak katılmak istiyorsanız 1 e basınız:  ");
        userType = scanner1.nextInt();

        User newUser = new User(name, address, email, memberId, phoneNumber, password, userType);

        DatabaseManager.createUser(newUser);
    }

    public static String enterRegisteredUserInfo() {

        do {

            Scanner scanner = new Scanner(System.in);
            System.out.println("mail adresiniz");
            userEmail = scanner.nextLine();
            System.out.println("sifreniz");
            String userPassword = scanner.nextLine();

            User authenticatedUser = DatabaseManager.getAuthenticatedUser(userEmail, userPassword);
            if (authenticatedUser != null) {
                int userType = authenticatedUser.getUserType();

                if (userType == 1) {

                    System.out.println("Sisteme alıcı olarak başarıyla giriş yaptınız.");

                }

                isAuth = true;

            } else {
                System.out.println("Kullanıcı doğrulanamadı. Lütfen bilgilerinizi kontrol ediniz.");
                authCount++;
            }

            if (authCount >= 3) {
                System.out.println("Çok sayıda başarısız giriş denemesi. Çıkış yapılıyor...");
                break;
            }
        } while (!isAuth);

        return userEmail;
    }
}