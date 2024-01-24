package BidToWinSunum;

import java.util.Scanner;

public class Seller extends User {
	public static int productId;
	public static String productName;
	public static String productType;
	public static String productOwnerEmail;
	public static int productQuantity;
	public static String productQuality;
	public static double productBasePrice;
	public static Scanner scanner = new Scanner(System.in);
	public static final String DATABASE_URL = "jdbc:sqlite:AuctionUserLists.db";
	public static final String DRIVER = "org.sqlite.JDBC";

	public Seller(String name, String address, String email, int memberId, String phoneNumber, String password,
			int userType, int productId, String productName, String productType, String productOwnerEmail,
			int productQuantity, String productQuality, double productBasePrice) {
		super(name, address, email, memberId, phoneNumber, password, userType);
		this.productId = productId;
		this.productName = productName;
		this.productType = productType;
		this.productOwnerEmail = productOwnerEmail;
		this.productQuantity = productQuantity;
		this.productQuality = productQuality;
		this.productBasePrice = productBasePrice;
		this.userType = userType;
	}

	public static void getProductAttribute(String currentUserEmail) {

		if (DatabaseManager.getUserTypeByEmail(currentUserEmail) == 0 && AuctionSystem.isAuth) {
			scanner.nextLine();
			System.out.println("Ürünün Adı : ");
			productName = scanner.nextLine();
			System.out.println("Ürünün Tipi: ");
			productType = scanner.nextLine();
			productOwnerEmail = AuctionSystem.userEmail;
			System.out.println("Ürünün Miktarı: ");
			productQuantity = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Ürünün Kalitesi: ");
			productQuality = scanner.nextLine();
			System.out.println("Ürünün Fiyatı : ");
			productBasePrice = scanner.nextDouble();
			DatabaseManager.createProductTable();
			DatabaseManager.insertProduct(productId, productName, productType, productOwnerEmail, productQuantity,
					productQuality, productBasePrice);
		} else {
			System.out.println("Bu işlem için yetkiniz yok.");
		}
	}
}
