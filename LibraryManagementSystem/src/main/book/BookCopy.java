package main.book;

import java.util.UUID;

public abstract class BookCopy {
    private final String copyId; 
    private Book book;

    public BookCopy(Book book) {
        this.copyId = UUID.randomUUID().toString();
        this.book = book;
    }

    public String getCopyId() {
        return copyId;
    }
}