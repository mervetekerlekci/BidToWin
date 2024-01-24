package BidToWinSunum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final String DATABASE_URL = "jdbc:sqlite:AuctionUserLists.db";
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String URL = "jdbc:sqlite:AuctionBids.db";

	public static void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS AuctionUserLists (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT," + "address TEXT," + "email TEXT," + "member_id INTEGER," + "phone_number TEXT,"
				+ "password TEXT, " + "userType INTEGER)";

		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				Statement statement = connection.createStatement()) {

			statement.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(User newUser) {
		String sql = "INSERT INTO AuctionUserLists (name, address, email, member_id, phone_number, password, userType) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, newUser.name);
			preparedStatement.setString(2, newUser.address);
			preparedStatement.setString(3, newUser.email);
			preparedStatement.setInt(4, newUser.memberId);
			preparedStatement.setString(5, newUser.phoneNumber);
			preparedStatement.setString(6, newUser.password);
			preparedStatement.setInt(7, newUser.userType);
			preparedStatement.executeUpdate();
			System.out.println("Üye kaydı başarılı !");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean authenticateUser(String email, String password) {
		try {
			Class.forName(DRIVER);
			try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
				String sql = "SELECT * FROM AuctionUserLists WHERE email = ? AND password = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, email);
					statement.setString(2, password);
					try (ResultSet resultSet = statement.executeQuery()) {
						return resultSet.next();
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void createProductTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS Products (" + "productId INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "productName TEXT," + "productType TEXT," + "productOwnerEmail TEXT," + "productQuantity INTEGER,"
				+ "productQuality TEXT," + "productBasePrice REAL" + ");";
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				Statement statement = conn.createStatement()) {
			statement.execute(createTableSQL);
		} catch (SQLException e) {
			System.out.println("Tablo oluşturma hatası! " + e.getMessage());
		}
	}

	public static void insertProduct(int productId, String productName, String productType, String productOwnerEmail,
			int productQuantity, String productQuality, double productBasePrice) {
		String insertSQL = "INSERT INTO Products (productId, productName, productType, productOwnerEmail, "
				+ "productQuantity, productQuality, productBasePrice) VALUES (?,?, ?, ?, ?, ?, ?);";

		productId = getMaxProductId() + 1;

		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setInt(1, productId);
			pstmt.setString(2, productName);
			pstmt.setString(3, productType);
			pstmt.setString(4, productOwnerEmail);
			pstmt.setInt(5, productQuantity);
			pstmt.setString(6, productQuality);
			pstmt.setDouble(7, productBasePrice);
			pstmt.executeUpdate();
			System.out.println("Ürün, ürün tablosune eklendi. ");
		} catch (SQLException e) {
			System.out.println("Ürün ekleme hatası: " + e.getMessage());
		}
	}

	public static void listProductsByOwnerEmail(String ownerEmail) {
		String selectSQL = "SELECT * FROM Products WHERE productOwnerEmail = ?;";
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL)) {

			preparedStatement.setString(1, ownerEmail);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				System.out.println(ownerEmail + " için ürün listesi: ");
				System.out.println("---------------------------------------------------------");
				System.out.printf("%-10s %-20s %-15s %-25s %-15s %-20s %-15s\n", "ID", "Name", "Type", "Owner Email",
						"Quantity", "Quality", "Base Price");
				System.out.println("---------------------------------------------------------");

				while (resultSet.next()) {
					int productId = resultSet.getInt("productId");
					String productName = resultSet.getString("productName");
					String productType = resultSet.getString("productType");
					String productOwnerEmail = resultSet.getString("productOwnerEmail");
					int productQuantity = resultSet.getInt("productQuantity");
					String productQuality = resultSet.getString("productQuality");
					double productBasePrice = resultSet.getDouble("productBasePrice");

					System.out.printf("%-10s %-20s %-15s %-25s %-15s %-20s %-15s\n", productId, productName,
							productType, productOwnerEmail, productQuantity, productQuality, productBasePrice);
				}
			}
		} catch (SQLException e) {
			System.out.println("Ürün listeleme hatası!  " + e.getMessage());
		}
	}

	public static void updateProduct(String productOwnerEmail, int productId1, int newQuantity, double newBasePrice) {
		String updateSQL = "UPDATE Products SET productQuantity = ?, productBasePrice = ? WHERE productId = ? AND productOwnerEmail = ?";
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setDouble(2, newBasePrice);
			pstmt.setInt(3, productId1);
			pstmt.setString(4, productOwnerEmail);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("Ürün başarıyla güncellendi.");
			} else {
				System.out.println("Ürün güncelleme hatası! Lütfen ürün ID ve mail bilgilerinizi kontrol edin! ");
			}
		} catch (SQLException e) {
			System.out.println("Error updating product: " + e.getMessage());
		}
	}

	public static void deleteProduct(String ownerEmail, int productId) {
		String deleteSQL = "DELETE FROM Products WHERE productId = ? AND productOwnerEmail = ?";
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
			pstmt.setInt(1, productId);
			pstmt.setString(2, ownerEmail);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Ürün başarıyla  silindi.");
			} else {
				System.out.println("Ürün silinemedi. Bilgileri kontrol edin.");
			}
		} catch (SQLException e) {
			System.out.println("Ürün silme hatası: " + e.getMessage());
		}
	}

	public static double getBasePrice(String productName) {
		String selectSQL = "SELECT productBasePrice FROM Products WHERE productName = ?;";
		double basePrice = 0.0;

		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

			pstmt.setString(1, productName);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					basePrice = resultSet.getDouble("productBasePrice");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving base price: " + e.getMessage());
		}
		return basePrice;
	}

	public static void createBid() {
		try (Connection connection = DriverManager.getConnection(URL);
				Statement statement = connection.createStatement()) {

			String createTableSQL = "CREATE TABLE IF NOT EXISTS AuctionBids (email TEXT, bid INT)";
			statement.execute(createTableSQL);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertBid(String email, double money) {
		try (Connection connection = DriverManager.getConnection(URL)) {

			String checkIfExistsQuery = "SELECT * FROM AuctionBids WHERE email = ?";
			try (PreparedStatement checkStatement = connection.prepareStatement(checkIfExistsQuery)) {
				checkStatement.setString(1, email);
				try (ResultSet resultSet = checkStatement.executeQuery()) {
					if (resultSet.next()) {

						System.out.println("Bu email adresi ile zaten bir teklif verilmiÅŸ.");
					} else {

						String insertQuery = "INSERT INTO AuctionBids VALUES (?, ?)";
						try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
							insertStatement.setString(1, email);
							insertStatement.setDouble(2, money);
							insertStatement.executeUpdate();
							System.out.println("Teklif başarıyla eklendi.");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void viewBids() {
		try (Connection connection = DriverManager.getConnection(URL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM AuctionBids")) {

			System.out.println("Email\t\tBid");
			System.out.println("------------------------");

			while (resultSet.next()) {
				String email = resultSet.getString("email");
				double bid = resultSet.getDouble("bid");
				System.out.println(email + "\t\t" + bid);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteAllBids() {
		try (Connection connection = DriverManager.getConnection(URL);
				Statement statement = connection.createStatement()) {

			String deleteQuery = "DELETE FROM AuctionBids";
			int rowsAffected = statement.executeUpdate(deleteQuery);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static User getAuthenticatedUser(String email, String password) {
		try {
			Class.forName(DRIVER);
			try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
				String sql = "SELECT * FROM AuctionUserLists WHERE email = ? AND password = ?";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, email);
					statement.setString(2, password);
					try (ResultSet resultSet = statement.executeQuery()) {
						if (resultSet.next()) {

							int userId = resultSet.getInt("id");
							String name = resultSet.getString("name");
							String address = resultSet.getString("address");
							int memberId = resultSet.getInt("member_id");
							String phoneNumber = resultSet.getString("phone_number");
							int userType = resultSet.getInt("userType");

							return new User(name, address, email, memberId, phoneNumber, password, userType);
						}
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getBidCount() {
		int bidCount = 0;

		try (Connection connection = DriverManager.getConnection(URL)) {
			String countQuery = "SELECT COUNT(*) FROM AuctionBids";
			try (PreparedStatement countStatement = connection.prepareStatement(countQuery);
					ResultSet resultSet = countStatement.executeQuery()) {
				if (resultSet.next()) {
					bidCount = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bidCount;
	}

	public static double getMaxBid() {
		double maxBid = 0.0;

		try (Connection connection = DriverManager.getConnection(URL)) {
			String maxBidQuery = "SELECT MAX(bid) FROM AuctionBids";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(maxBidQuery)) {
				if (resultSet.next()) {
					maxBid = resultSet.getDouble(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxBid;
	}

	public static int getMaxProductId() {
		int maxProductId = 0;

		try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
			String maxProIdQuery = "SELECT MAX(productId) FROM Products";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(maxProIdQuery)) {
				if (resultSet.next()) {
					maxProductId = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxProductId;
	}

	public static int getMaxUserId() {
		int maxUserId = 0;

		try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
			String maxUserIdQuery = "SELECT MAX(id) FROM AuctionUserLists";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(maxUserIdQuery)) {
				if (resultSet.next()) {
					maxUserId = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxUserId;
	}

	public static int getUserTypeByEmail(String email) {
		String sql = "SELECT userType FROM AuctionUserLists WHERE email = ?";
		int userType = -1;

		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, email);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					userType = resultSet.getInt("userType");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userType;
	}

}