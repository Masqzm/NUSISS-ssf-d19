package ssf.day19.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.day19.models.Product;
import ssf.day19.repo.ProductRepo;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.getAllProducts();
    }
}
