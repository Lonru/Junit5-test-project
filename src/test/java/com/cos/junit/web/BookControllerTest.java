package com.cos.junit.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.junit.domain.Book;
import com.cos.junit.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id AUTO_INCREMENT = 1");
	}
	
	@Test
	public void save테스트() throws Exception {
		Book book = new Book(null, "FirstStep", 1.2, 4000);
		String content = new ObjectMapper().writeValueAsString(book);
		
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction
			.andExpect(status().isCreated())
			.andExpect((jsonPath("$.title").value("FirstStep")))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll테스트() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(1, "끝나감", 8.3, 9000));
		books.add(new Book(2, "그런 감은 없다", 4.3, 6500));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction
		 	.andExpect(status().isOk())
		 	.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$[0].rating").value(8.3))
			.andExpect(jsonPath("$[1].rating").value(4.3))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById테스트() throws Exception {
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(1, "거의 다함", 9.1, 7900));
		books.add(new Book(2, "은 침몰하였다", 2.1, 1000));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(get("/book/1")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.price").value(7900))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update테스트() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(1, "흐음", 5.4, 2000));
		books.add(new Book(2, "하암", 6.6, 3000));
		bookRepository.saveAll(books);
		
		Book newBook = new Book(1, "크르릉", 7.0, 4000);
		String content = new ObjectMapper().writeValueAsString(newBook);
		
		ResultActions resultAction = mockMvc.perform(put("/book/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("크르릉"))
			.andExpect(jsonPath("$.price").value(4000))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete테스트() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(1, "88888", 8.5, 5000));
		books.add(new Book(2, "99999", 10.1, 9000));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(delete("/book/1")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}
}
