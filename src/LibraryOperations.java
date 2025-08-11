public interface LibraryOperations {
    void addBook(Book book);
    void removeBook(int id);
    void searchBook(String keyword);
    void borrowBook(int id);
    void returnBook(int id);
    void displayAllBooks();
}
