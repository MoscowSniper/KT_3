import java.util.ArrayList;
import java.util.List;

abstract class Book implements Readable {
    protected String title;
    protected String author;
    protected int publicationYear;

    public Book(String title, String author, int publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public abstract void read();
}

class FictionBook extends Book {
    public FictionBook(String title, String author, int publicationYear) {
        super(title, author, publicationYear);
    }

    @Override
    public void read() {
        System.out.println("читает художественную книгу : " + title);
    }
}

class NonFictionBook extends Book {
    public NonFictionBook(String title, String author, int publicationYear) {
        super(title, author, publicationYear);
    }

    @Override
    public void read() {
        System.out.println("читает не художественную книгу: " + title);
    }
}

interface Readable {
    void read();
}

class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}

class Library {
    public List<Book> books;
    private List<Book> issuedBooks;

    public Library() {
        books = new ArrayList<>();
        issuedBooks = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void issueBook(String title) throws BookUnavailableException {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                books.remove(book);
                issuedBooks.add(book);
                System.out.println("Выпущенная книга: " + title);
                return;
            }
        }
        throw new BookUnavailableException("Книга \"" + title + "\" не доступна.");
    }

    public void returnBook(String title) {
        for (Book book : issuedBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                issuedBooks.remove(book);
                books.add(book);
                System.out.println("Книга возвращена: " + title);
                return;
            }
        }
        System.out.println("книга \"" + title + "\" не была выпущена из этой библиотеки.");
    }

    public void searchBooksByGenre(Genre genre) {
        books.forEach(book -> {
            if ((genre == Genre.FICTION && book instanceof FictionBook) ||
                    (genre == Genre.NON_FICTION && book instanceof NonFictionBook)) {
                System.out.println("Найдена книга: " + book.getTitle());
            }
        });
    }

    public static class LibraryHelper {
        public static Book findBookByTitle(List<Book> books, String title) {
            for (Book book : books) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    return book;
                }
            }
            return null;
        }
    }
}

enum Genre {
    FICTION,
    NON_FICTION
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        library.addBook(new FictionBook("1984", "Роберт Полсон", 1949));
        library.addBook(new NonFictionBook("Sapiens", "Тайлер Жирден", 2011));

        try {
            library.issueBook("1984");
        } catch (BookUnavailableException e) {
            System.out.println(e.getMessage());
        }

        library.returnBook("1984");

        System.out.println("Ищю художественные книги...");
        library.searchBooksByGenre(Genre.FICTION);

        System.out.println("Ищю не художественные книги...");
        library.searchBooksByGenre(Genre.NON_FICTION);

        Book foundBook = Library.LibraryHelper.findBookByTitle(library.books, "Sapiens");
        if (foundBook != null) {
            System.out.println("Помошник нашел книгу: " + foundBook.getTitle());
        } else {
            System.out.println("Помошник не нашел книгу.");
        }
    }
}
