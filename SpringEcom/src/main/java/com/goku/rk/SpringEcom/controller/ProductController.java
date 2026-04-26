package com.goku.rk.SpringEcom.controller;

import com.goku.rk.SpringEcom.model.Product;
import com.goku.rk.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService serv;

    @GetMapping("/Products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(serv.getAllProducts(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/Products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int pId) {
        Product pr = serv.getProductId(pId);
        if (pr != null) {
            return new ResponseEntity<>(pr, HttpStatus.ACCEPTED);
        } else
            return new ResponseEntity<>("Not found with particular Id.", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {
        Product savedProduct = serv.addProductOrUpdateProduct(product, imageFile);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<?> getProductImage(@PathVariable("id") int pId) {
        Product pr = serv.getProductId(pId);
        if (pr != null) {
            return new ResponseEntity<>(pr.getImageData(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("No Image found for this Id", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/productUpdate")
    public ResponseEntity<?> updateProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {
        Product savedProduct = serv.addProductOrUpdateProduct(product, imageFile);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int pId) {
        Product pr = serv.getProductId(pId);
        if (pr != null) {
            serv.deleteProductById(pId);
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not found data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
