package cc.catman.object;

import cc.catman.object.po.Author;
import cc.catman.object.po.Book;
import cc.catman.object.po.Library;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mock {

    public static Library mockLibrary(int bookSize, int authorSize) {
        Library library = new Library();
        library.setName("图书馆");
        library.setAddress("北京市朝阳区");
        List<Book> books = mockBooks(bookSize);
        List<Author> authors = mockAuthors(authorSize);
        RandomBookAndAuthor(books, authors);
        library.setBooks(books);
        return library;
    }

    public static void RandomBookAndAuthor(List<Book> books, List<Author> authors) {
        for (Book book : books) {
            for (Author value : authors) {
                if (Math.random() > 0.5) {
                    if (!book.getAuthors().contains(value)) {
                        book.getAuthors().add(value);
                        if (value.getBooks() == null) {
                            value.setBooks(new ArrayList<>());
                        }
                        if (!value.getBooks().contains(book)) {
                            value.getBooks().add(book);
                        }
                    }
                }
            }
        }
    }
   public static List<Book> mockBooks(int size) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            books.add(mockBook());
        }
        return books;
    }

    public static Book mockBook() {
        Book book = new Book();
        book.setName("Java编程思想");
        book.setIsbn("978-7-121-30499-6");
        book.setPrice(Math.toIntExact(Math.round(100.0)));
        book.setDiscount(Math.random());
        book.setPublishDate(new Date());
        return book;
    }

    public static List<Author> mockAuthors(int size) {
        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            authors.add(mockAuthor());
        }
        return authors;
    }

    public static Author mockAuthor() {
       Author author = new Author();
        author.setName("Bruce Eckel");
        author.setAge(Math.toIntExact(Math.round(50.0)));
        return author;
    }

    public static void main(String[] args) {
        Library library = mockLibrary(20,10);
        System.out.println(library);
    }
}
