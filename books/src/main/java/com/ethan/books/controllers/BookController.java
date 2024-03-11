package com.ethan.books.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.ethan.books.models.Book;
import com.ethan.books.models.LoginUser;
import com.ethan.books.models.User;
import com.ethan.books.services.BookService;
import com.ethan.books.services.UserService;

@Controller
public class BookController {

	// Add once service is implemented:
	@Autowired
	private UserService users;
	@Autowired
	private BookService books;
    
    @GetMapping("/")
    public String index(Model model) {
    
      
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index.jsp";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {
        

    	User user = users.register(newUser, result);
    	
        
        if(result.hasErrors()) {
         
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }
    
        session.setAttribute("userId", user.getId());
    
        return "redirect:/home";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {
        
  
        User user = users.login(newLogin, result);
    
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "index.jsp";
        }
    
        session.setAttribute("userId", user.getId());
    
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
    	
    	if(session.getAttribute("userId") == null) {
    		return "redirect:/";
    	}
    	
    	model.addAttribute("books", books.all());
    	model.addAttribute("user", users.findById((Long)session.getAttribute("userId")));
    	return "home.jsp";
    }
    
    @GetMapping("/addPage")
    public String addPage(@ModelAttribute("book") Book book, Model model, HttpSession session) {
    	
    	User user = users.findById((Long)session.getAttribute("userId"));
    	model.addAttribute("user", user);
    	
    	return "addPage.jsp";
    }
    
    @PostMapping("/books")
    public String createBook(@Valid @ModelAttribute("book") Book book, BindingResult result) {

    	if (result.hasErrors()) {
    		return "addPage.jsp";
    	}
    	
    	books.create(book);
    	
    	return "redirect:/home";
    }
    
    @GetMapping("/books/{id}")
    public String showPage(Model model, @PathVariable("id") Long id, HttpSession session) {
    	if(session.getAttribute("userId") == null) {
    		return "redirect:/";
    	}
    	Book book = books.findById(id);
    	model.addAttribute("book", book);
    	model.addAttribute("user", users.findById((Long)session.getAttribute("userId")));
    	
    	return "book.jsp";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
    	return "redirect:/";
    }
    
    @GetMapping("/books/edit/{id}")
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
    	if(session.getAttribute("userId") == null){
    		return "redirect:/";
    	}
    	Book book = books.findById(id);
    	model.addAttribute("book", book);
    	model.addAttribute("user", users.findById((Long)session.getAttribute("userId")));
    	
		return "edit.jsp";
	}
	
	@RequestMapping(value="/books/edit/{id}", method=RequestMethod.PUT)
	public String update(

			Model model, 
			@Valid @ModelAttribute("book") Book updatedBook, 
			BindingResult result, 
			HttpSession session) {

		
		if(result.hasErrors()) {
//			model.addAttribute("book", book);
			return "edit.jsp";
		}else {

			books.updateBook(updatedBook);
			return "redirect:/home";
		}
	}
	
	@RequestMapping(value="/books/delete/{id}", method=RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		books.deleteBook(id);
		return "redirect:/home";
	}
}