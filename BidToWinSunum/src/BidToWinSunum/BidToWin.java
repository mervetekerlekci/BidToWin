package BidToWinSunum;

import java.util.Scanner;



public class BidToWin {
    public static final String DATABASE_URL = "jdbc:sqlite:userLists.db";
    private static final String URL = "jdbc:sqlite:AuctionProductList.db";
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        boolean sellerMenu = false;
        int userType;

        DatabaseManager.createTable();
        DatabaseManager.createBid();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("Açık arttırma sistemine üye olmak için 1");
            System.out.println("Zaten üye iseniz sisteme giriş yapmak için 2");
            System.out.println("Zaten üyeyseniz açık arttırmaya katılmak istiyorsanız 3");
            System.out.println("Sistemden cikis yapmak icin 0");
            System.out.println("Lütfen yapmak istediğiniz işlemin numarasını giriniz.");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    AuctionSystem.EnterNewUserInfo();
                    break;
                case 2:

                    String currentUser = AuctionSystem.enterRegisteredUserInfo();
                    userType = DatabaseManager.getUserTypeByEmail(currentUser);

                    if(userType == 0) {
                        System.out.println("Sisteme satici olarak giris yapildi.");
                        sellerMenu = true;
                    }

                    if(userType == 1) {
                        System.out.println("3 e basarak açık artırmaya katılabilirsiniz.");
                        System.out.println("--------------------------------------------");
                    }

                    while (sellerMenu) {

                        try {

                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                        System.out.println("Ürün ekleme için 1");
                        System.out.println("Ürün listeleme için 2");
                        System.out.println("Ürün silme için 3");
                        System.out.println("Ürün güncelleme için 4");
                        System.out.println("Alt menüden çıkmak için 0");

                        int sellerOption = scanner.nextInt();
                        scanner.nextLine();

                        switch (sellerOption) {
                            case 1:
                                Seller.getProductAttribute(currentUser);
                                break;
                            case 2:
                                String emailForListProduct = currentUser;
                                DatabaseManager.listProductsByOwnerEmail(emailForListProduct);

                                break;
                            case 3:
                                String ownerEmailOfProduct = currentUser;
                                System.out.println("Silmek istediğiniz ürününüzün ID'sini girin: ");
                                int productId = scanner.nextInt();
                                DatabaseManager.deleteProduct(ownerEmailOfProduct, productId);
                                break;
                            case 4:
                                String email = currentUser;
                                System.out.println("Güncellenecek ürünün ID'sini girin:");
                                int productIdToUpdate = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Yeni ürünün miktarını girin:");
                                int newQuantity = scanner.nextInt();
                                System.out.println("Yeni ürün fiyatını girin:");
                                double newBasePrice = scanner.nextDouble();
                                DatabaseManager.updateProduct(email, productIdToUpdate, newQuantity, newBasePrice);
                                break;
                            case 0:
                                sellerMenu = false;
                                break;
                            default:
                                System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
                                break;
                        }
                    }
                    break;
                case 3:
                    boolean auctionMenu = true;
                    while (auctionMenu) {
                        System.out.println("Satin almak istediginiz urunu girmek icin 1:");
                        System.out.println("Açık arttırma kazananını görüntüleme için 2");
                        System.out.println("Alt menüden çıkmak için 0");

                        int auctionOption = scanner.nextInt();
                        scanner.nextLine();

                        switch (auctionOption) {
                            case 1:
                                System.out.println("ADMİN PANEL: Açık artırmaya katilmak istediğiniz ürünün ismini giriniz:");
                                String productName = scanner.nextLine();
                                System.out.println(productName + " için açık arttırma başlamıştır !");
                                DatabaseManager.createBid();
                                Buyer.EnterBidForAuction(productName);
                                break;
                            case 2:
                                Admin.PrintWinner();
                                DatabaseManager.deleteAllBids();
                                break;
                            case 0:
                                auctionMenu = false;
                                break;
                            default:
                                System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
                                break;
                        }
                    }
                    break;
                case 0:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Sistemden cikis yapildi.");
    }
}
