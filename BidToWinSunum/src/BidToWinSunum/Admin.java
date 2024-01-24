package BidToWinSunum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {

	private static final String DATABASE_URL = "jdbc:sqlite:AuctionUserLists.db";

	private static final String URL = "jdbc:sqlite:AuctionBids.db";

	public static void selectUsers() {

		String sql = "SELECT * FROM AuctionUserLists";
		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String address = resultSet.getString("address");
				String email = resultSet.getString("email");
				int memberId = resultSet.getInt("member_id");
				String phoneNumber = resultSet.getString("phone_number");
				String password = resultSet.getString("password");
				int userType = resultSet.getInt("userType");
				System.out.println("ID: " + id);
				System.out.println("Name: " + name);
				System.out.println("Address: " + address);
				System.out.println("Email: " + email);
				System.out.println("Member ID: " + memberId);
				System.out.println("Phone Number: " + phoneNumber);
				System.out.println("Password: " + password);
				System.out.println("UserType: " + userType);
				System.out.println("-----------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String PrintWinner() {
		String emailWithMaxBid = null;
		String bidWithMaxBid = null;

		try (Connection connection = DriverManager.getConnection(URL);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT email, bid FROM AuctionBids ORDER BY bid DESC LIMIT 1")) {

			if (resultSet.next()) {
				emailWithMaxBid = resultSet.getString("email");
				bidWithMaxBid = resultSet.getString("bid");
				System.out.println("Email: " + emailWithMaxBid + " bid: " + bidWithMaxBid);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return emailWithMaxBid;
	}
}
