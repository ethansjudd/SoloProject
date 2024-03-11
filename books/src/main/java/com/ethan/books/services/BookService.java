package com.ethan.books.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ethan.books.models.Book;
import com.ethan.books.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository repo;
	
	public Book findById(Long id) {
		
		Optional<Book> result = repo.findById(id);
		if(result.isPresent()) {
			return result.get();
		}
		
		return null;
	}
	

	public List<Book> all() {
		return repo.findAll();
	}
	
	public Book create(Book book) {
		return repo.save(book);
	}
	
	public Book updateBook(Book book) {
		return repo.save(book);
	}
	
	public void deleteBook (Long id) {
		repo.deleteById(id);
	}
}