package ssf.day19.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ssf.day19.models.Product;
import ssf.day19.services.ProductService;

@Controller
@RequestMapping
public class ProductsController {
    @Autowired
    ProductService productSvc;

    @GetMapping("/products")
    public ModelAndView getProductsList() {
        ModelAndView mav = new ModelAndView();

        List<Product> productsList = productSvc.getAllProducts();

        mav.addObject("productsList", productsList);
        mav.setViewName("products");

        return mav;
    }
}
