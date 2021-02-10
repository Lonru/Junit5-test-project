package com.cos.junit.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.junit.domain.Book;
import com.cos.junit.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {

	private final BookRepository bookRepository;
	
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){
		return new ResponseEntity<>(bookRepository.save(book),HttpStatus.CREATED);
	}
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(bookRepository.findAll(),HttpStatus.OK);
	}
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findAll(@PathVariable int id){
		return new ResponseEntity<>(bookRepository.findById(id),HttpStatus.OK);
	}

	@PutMapping("/book/{id}")
	public ResponseEntity<?> update(@PathVariable int id,@RequestBody Book book){
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException());
		bookEntity.setPrice(book.getPrice());
		bookEntity.setRating(book.getRating());
		bookEntity.setTitle(book.getTitle());
		return new ResponseEntity<>(bookEntity, HttpStatus.OK);
	}

	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id){
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException());
						return new ResponseEntity<>(bookEntity,HttpStatus.OK);
	}
}