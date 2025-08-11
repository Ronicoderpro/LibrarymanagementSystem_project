import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryImpl implements LibraryOperations{

    private List<Book> books = new ArrayList<>();
    private static final String FILE_NAME = "library_data.ser";

    public LibraryImpl() {
        loadFromFile();
    }
    @Override
    public void addBook(Book book) {
        books.add(book);
        saveToFile();
        System.out.println("Book added successfully!");
    }

    @Override
    public void removeBook(int id) {
        boolean removed = books.removeIf(book -> book.getId() == id);
        if (removed) {
            saveToFile();
            System.out.println("Book removed successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            book.displayBookInfo();
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore, file will be created on save
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data.");
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}
