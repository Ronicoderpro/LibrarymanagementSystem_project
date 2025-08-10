# LibrarymanagementSystem_project
import java.io.*;
import java.util.*;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }

    public void displayBookInfo() {
        System.out.println("ID: " + id + " | Title: " + title + " | Author: " + author + 
                           " | Status: " + (isAvailable ? "Available" : "Borrowed"));
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private static final String FILE_NAME = "library_data.ser";

    public Library() {
        loadFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        saveToFile();
        System.out.println("Book added successfully!");
    }

    public void removeBook(int id) {
        boolean removed = books.removeIf(book -> book.getId() == id);
        if (removed) {
            saveToFile();
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    public void searchBook(String keyword) {
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || 
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                book.displayBookInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No books found!");
    }

    public void borrowBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                if (book.isAvailable()) {
                    book.setAvailable(false);
                    saveToFile();
                    System.out.println("You borrowed the book successfully!");
                } else {
                    System.out.println("Book is already borrowed!");
                }
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void returnBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                if (!book.isAvailable()) {
                    book.setAvailable(true);
                    saveToFile();
                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("This book was not borrowed!");
                }
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            book.displayBookInfo();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore, file will be created when saving first time
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data.");
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book (Admin)");
            System.out.println("2. Remove Book (Admin)");
            System.out.println("3. Search Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. View All Books");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    library.addBook(new Book(id, title, author));
                    break;
                case 2:
                    System.out.print("Enter Book ID to remove: ");
                    int removeId = sc.nextInt();
                    library.removeBook(removeId);
                    break;
                case 3:
                    sc.nextLine();
                    System.out.print("Enter keyword (title/author): ");
                    String keyword = sc.nextLine();
                    library.searchBook(keyword);
                    break;
                case 4:
                    System.out.print("Enter Book ID to borrow: ");
                    int borrowId = sc.nextInt();
                    library.borrowBook(borrowId);
                    break;
                case 5:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = sc.nextInt();
                    library.returnBook(returnId);
                    break;
                case 6:
                    library.displayAllBooks();
                    break;
                case 7:
                    System.out.println("Exiting system...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
