package helloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @RequestMapping(value = "/products",method = RequestMethod.GET)
    public String productView() {
        return "productView";
    }
}