package test;


import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import main.book.*;
import main.system.Password;
import main.users.*;

class TestBook {
	Customer customer = new Customer("customer", new Password("Cust123"));
	PrintStream oldPrintStream;
    ByteArrayOutputStream bos;
    
    private void setOutput() throws Exception {
        oldPrintStream = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    private String getOutput() {
        System.setOut(oldPrintStream);
        return bos.toString();
    }
    
	@Test
	void testBook_01() throws Exception {
		//Add rental book copy, expected result: "Add one rental copy of book\n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		RentalBookCopy copy1 = new RentalBookCopy(book);
		book.addRentalCopy(copy1);
		assertEquals("Add one rentable copy of book\n", getOutput());
	}
	
	@Test
	void testBook_02() throws Exception {
		//Add salable book copy, expected result: "Add one salable copy of book\n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		SalableBookCopy copy2 = new SalableBookCopy(book);
		book.addSaleableCopy(copy2);
		assertEquals("Add one seleable copy of book\n", getOutput());
	}
	
	@Test
	void testBook_03() throws Exception{
		//Lend a book, expected result: "Lending Successfully for book To Kill a Mockingbird \n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		book.addRentalCopy(new RentalBookCopy(book));
		getOutput();
		setOutput();
		book.Lend(customer);
		assertEquals("Lending Successfully for book\n", getOutput());
	}
	
	@Test
	void testBook_04() throws Exception {
		//buy a book, expected result: "Buying Successfully for book To Kill a Mockingbird \n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		book.addSaleableCopy(new SalableBookCopy(book));
		getOutput();
		setOutput();
		book.Buy(customer);
		assertEquals("Buying Successfully for book\n", getOutput());
		
	}
	
	@Test
	void testBook_05() throws Exception {
		//lend a book when these is no rental copy of that book, expected result: "Buying Successfully for book To Kill a Mockingbird \n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		book.Lend(customer);
		assertEquals("No book available for lending.\n", getOutput());
	}
	
	
	
	@Test
	void testBook_06() throws Exception {
		//lend a book when these is no rental copy of that book, expected result: "Buying Successfully for book To Kill a Mockingbird \n"
		setOutput();
		Book book = new Book("978-0-06-112008-4","To Kill a Mockingbird","Harper Lee","J.B. Lippincott & Co.", "1960-07-11", "touching story", 250);
		book.Buy(customer);
		assertEquals("No book available for selling.\n", getOutput());
	}
}