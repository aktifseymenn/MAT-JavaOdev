import java.util.ArrayList;
import java.util.Scanner;

// 1. Kitap Sınıfı (Book)
class Book {
    private int id;
    private String title;
    private String author;
    private boolean isBorrowed; // true: Ödünçte, false: Müsait

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false; // Yeni eklenen kitap başlangıçta müsaittir
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? "Ödünçte" : "Müsait";
        return "ID: " + id + " | " + title + " | " + author + " | " + status;
    }
}

public class LibraryApp {
    private static ArrayList<Book> library = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Test için başlangıçta birkaç örnek kitap ekleyelim (İsteğe bağlı)
        library.add(new Book(15, "Suç ve Ceza", "Fyodor Dostoyevski"));
        library.add(new Book(23, "1984", "George Orwell"));
        library.add(new Book(31, "Simyacı", "Paulo Coelho"));

        int choice;
        do {
            printMenu();
            System.out.print("Seçiminiz: ");
            
            // Kullanıcı sayı yerine yanlış bir şey girerse programın çökmemesini sağlayan kontrol
            while (!scanner.hasNextInt()) {
                System.out.print("Lütfen geçerli bir sayı giriniz: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Dummy enter fix

            System.out.println();
            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    searchBook();
                    break;
                case 0:
                    System.out.println("Programdan çıkılıyor. Başarılar dileriz!");
                    break;
                default:
                    System.out.println("Geçersiz seçim! Lütfen tekrar deneyin.");
            }
            System.out.println();
        } while (choice != 0);
    }

    // Menüyü ekrana basan fonksiyon
    public static void printMenu() {
        System.out.println("===== KÜTÜPHANE =====");
        System.out.println("1 - Kitapları Listele");
        System.out.println("2 - Kitap Ekle");
        System.out.println("3 - Kitap Ödünç Al");
        System.out.println("4 - Kitabı İade Et");
        System.out.println("5 - Kitap Ara");
        System.out.println("0 - Çıkış");
    }

    // 3. Kitapları Listeleme
    public static void listBooks() {
        if (library.isEmpty()) {
            System.out.println("Kütüphanede hiç kitap yok.");
            return;
        }
        for (Book book : library) {
            System.out.println(book);
        }
    }

    // 2. Kitap Ekleme
    public static void addBook() {
        System.out.print("Kitap adı: ");
        String title = scanner.nextLine();
        
        System.out.print("Yazar: ");
        String author = scanner.nextLine();
        
        System.out.print("ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Lütfen geçerli bir ID (sayı) giriniz: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // Dummy enter fix

        // Aynı ID'ye sahip başka bir kitap var mı kontrolü
        for (Book book : library) {
            if (book.getId() == id) {
                System.out.println("Hata: Bu ID'ye sahip bir kitap zaten mevcut!");
                return;
            }
        }

        library.add(new Book(id, title, author));
        System.out.println("Kitap başarıyla eklendi.");
    }

    // 4. Kitap Ödünç Alma
    public static void borrowBook() {
        System.out.print("Ödünç almak istediğiniz kitabın ID'si: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Lütfen geçerli bir ID giriniz: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine();

        Book targetBook = findBookById(id);
        if (targetBook == null) {
            System.out.println("Kitap bulunamadı.");
        } else if (targetBook.isBorrowed()) {
            System.out.println("Bu kitap zaten ödünç alınmış.");
        } else {
            targetBook.setBorrowed(true);
            System.out.println("Kitap başarıyla ödünç alındı.");
        }
    }

    // 5. Kitap İade Etme
    public static void returnBook() {
        System.out.print("İade etmek istediğiniz kitabın ID'si: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Lütfen geçerli bir ID giriniz: ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine();

        Book targetBook = findBookById(id);
        if (targetBook == null) {
            System.out.println("Kitap bulunamadı.");
        } else if (!targetBook.isBorrowed()) {
            System.out.println("Bu kitap zaten kütüphanede (müsait durumda).");
        } else {
            targetBook.setBorrowed(false);
            System.out.println("Kitap başarıyla iade edildi.");
        }
    }

    // 6. Kitap Arama (İsim veya Yazara Göre)
    public static void searchBook() {
        System.out.print("Arama (Kitap adı veya yazar): ");
        String query = scanner.nextLine().toLowerCase();

        boolean found = false;
        for (Book book : library) {
            // Girilen kelime kitap adında veya yazar adında geçiyorsa eşleşir (büyük/küçük harf duyarsız)
            if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                System.out.println(book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Arama kriterine uygun kitap bulunamadı.");
        }
    }

    // Yardımcı metod: ID'ye göre kitap arama
    private static Book findBookById(int id) {
        for (Book book : library) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
}