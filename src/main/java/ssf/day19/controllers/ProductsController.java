package ssf.day19.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/buy")
    public ModelAndView postBuyProduct(@RequestParam String id) {
        ModelAndView mav = new ModelAndView();

        Product prodBought = productSvc.buyProdIfAvail(id);

        // Fail to buy product
        if(prodBought == null)
            mav.setViewName("buy-failed");
        else
        {
            mav.addObject("prodBought", prodBought);
            mav.setViewName("buy-success");
        }

        return mav;
    }
}
