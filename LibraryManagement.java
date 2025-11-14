import java.util.*;
import java.io.*;

// ------------------ Book Class ------------------
class Book implements Serializable {
    int id;
    String title;
    String author;
    boolean isBorrowed;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    @Override
    public String toString() {
        return "Book ID: " + id + 
               " | Title: " + title + 
               " | Author: " + author + 
               " | Status: " + (isBorrowed ? "Borrowed" : "Available");
    }
}

// ------------------ Member Class ------------------
class Member implements Serializable {
    int id;
    String name;
    List<Integer> borrowedBooks;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Member ID: " + id + 
               " | Name: " + name + 
               " | Borrowed Books: " + borrowedBooks;
    }
}

// ------------------ Library System ------------------
public class LibrarySystem {

    private static List<Book> books = new ArrayList<>();
    private static List<Member> members = new ArrayList<>();
    private static int bookCounter = 1;
    private static int memberCounter = 1;

    // ------------------ Add Book ------------------
    public static void addBook(Scanner sc) {
        System.out.print("Enter Title: ");
        sc.nextLine();
        String title = sc.nextLine();

        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        books.add(new Book(bookCounter++, title, author));
        System.out.println("Book Added Successfully!");
    }

    // ------------------ Add Member ------------------
    public static void addMember(Scanner sc) {
        System.out.print("Enter Member Name: ");
        sc.nextLine();
        String name = sc.nextLine();

        members.add(new Member(memberCounter++, name));
        System.out.println("Member Added Successfully!");
    }

    // ------------------ Borrow Book ------------------
    public static void borrowBook(Scanner sc) {
        System.out.print("Enter Member ID: ");
        int memberId = sc.nextInt();

        Member member = getMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.print("Enter Book ID to Borrow: ");
        int bookId = sc.nextInt();

        Book book = getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (book.isBorrowed) {
            System.out.println("Book is already borrowed!");
            return;
        }

        book.isBorrowed = true;
        member.borrowedBooks.add(book.id);

        System.out.println("Book borrowed successfully!");
    }

    // ------------------ Return Book ------------------
    public static void returnBook(Scanner sc) {
        System.out.print("Enter Member ID: ");
        int memberId = sc.nextInt();

        Member member = getMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.print("Enter Book ID to Return: ");
        int bookId = sc.nextInt();

        if (!member.borrowedBooks.contains(bookId)) {
            System.out.println("This member did not borrow this book!");
            return;
        }

        Book book = getBookById(bookId);
        book.isBorrowed = false;
        member.borrowedBooks.remove(Integer.valueOf(bookId));

        System.out.println("Book returned successfully!");
    }

    // ------------------ Search Book ------------------
    public static void searchBooks(Scanner sc) {
        System.out.print("Enter keyword (title/author): ");
        sc.nextLine();
        String keyword = sc.nextLine().toLowerCase();

        System.out.println("---- Search Results ----");
        for (Book book : books) {
            if (book.title.toLowerCase().contains(keyword) || 
                book.author.toLowerCase().contains(keyword)) {
                System.out.println(book);
            }
        }
    }

    // ------------------ Display All Books ------------------
    public static void displayBooks() {
        System.out.println("---- All Books ----");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // ------------------ Display All Members ------------------
    public static void displayMembers() {
        System.out.println("---- All Members ----");
        for (Member m : members) {
            System.out.println(m);
        }
    }

    // ------------------ Save to File ------------------
    public static void saveData() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("library.dat"));
            out.writeObject(books);
            out.writeObject(members);
            out.writeInt(bookCounter);
            out.writeInt(memberCounter);
            out.close();

            System.out.println("Data Saved Successfully!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
        }
    }

    // ------------------ Load from File ------------------
    public static void loadData() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("library.dat"));
            books = (List<Book>) in.readObject();
            members = (List<Member>) in.readObject();
            bookCounter = in.readInt();
            memberCounter = in.readInt();
            in.close();

            System.out.println("Data Loaded Successfully!");
        } catch (Exception e) {
            System.out.println("No saved data found!");
        }
    }

    // ------------------ Helper Methods ------------------
    private static Book getBookById(int id) {
        for (Book b : books) if (b.id == id) return b;
        return null;
    }

    private static Member getMemberById(int id) {
        for (Member m : members) if (m.id == id) return m;
        return null;
    }

    // ------------------ MAIN MENU ------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadData();

        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Display All Books");
            System.out.println("7. Display All Members");
            System.out.println("8. Save Data");
            System.out.println("9. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: addBook(sc); break;
                case 2: addMember(sc); break;
                case 3: borrowBook(sc); break;
                case 4: returnBook(sc); break;
                case 5: searchBooks(sc); break;
                case 6: displayBooks(); break;
                case 7: displayMembers(); break;
                case 8: saveData(); break;
                case 9: 
                    saveData();
                    System.out.println("Exiting... Bye!");
                    return;
                default: 
                    System.out.println("Invalid choice!");
            }
        }
    }
}
