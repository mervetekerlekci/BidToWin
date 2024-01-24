package BidToWinSunum;

import java.util.Scanner;

public class Buyer extends User {
	private static final String DATABASE_URL = "jdbc:sqlite:AuctionUserLists.db";
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:AuctionBids.db";

	public Buyer(String name, String address, String email, int memberId, String phoneNumber, String password,
			int userType) {
		super(name, address, email, memberId, phoneNumber, password, userType);
	}

	public static void EnterBidForAuction(String productName) {

		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Mail Adresinizi Giriniz: ");
			String userMail = scanner.nextLine();
			System.out.println("Şifrenizi Giriniz: ");
			String userPassword = scanner.nextLine();

			User bidUser = DatabaseManager.getAuthenticatedUser(userMail, userPassword);

			if (bidUser != null) {

				if (bidUser.getUserType() == 1) {
					System.out.print("Teklifinizi Giriniz: ");
					double amount = scanner.nextDouble();
					scanner.nextLine();

					if (amount < DatabaseManager.getBasePrice(productName)) {
						System.out.println(
								"Bu ürün için en düşük teklif fiyatı: " + DatabaseManager.getBasePrice(productName));
					}

					else if (amount < DatabaseManager.getMaxBid()) {

						System.out.println("Önceki tekliflerden yüksek bir teklif girmelisiniz " + "En yüksek Teklif: "
								+ DatabaseManager.getMaxBid());
					}

					else {
						DatabaseManager.insertBid(userMail, amount);
					}

				} else {

					System.out.println("Auctiona katılmak için geçerli bir kullanıcı adı ve şifre giriniz. ");
				}
			} else
				System.out.println("Auctiona katılmak için geçerli bir kullanıcı adı ve şifre giriniz. ");

		} while (DatabaseManager.getBidCount() < 3);
	}

}
