package com.goku.rk.SpringEcom.service;

import com.goku.rk.SpringEcom.repo.ProductRepo;
import com.goku.rk.SpringEcom.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductId(int pId) {
        return repo.findById(pId).orElse(null);
    }

    public Product addProductOrUpdateProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    public void deleteProductById(int pId) {
        repo.deleteById(pId);
    }
}
