package edu.metro.grocerystore.controller;

import edu.metro.grocerystore.model.User;
import edu.metro.grocerystore.service.UserService;
import edu.metro.grocerystore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public HomeController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String getHome(Model model, HttpSession session) {
        // BaseController automatically adds user to model
        
        // Add dynamic product data for the homepage
        model.addAttribute("featuredProducts", productService.getFeaturedProducts(12));
        model.addAttribute("allProducts", productService.getAllActiveProducts());
        
        return "home";
    }

}
