package org.hong.spring_ecommerce.controller;

import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private ProductoServiceImpl productoService;

    @Autowired
    public HomeController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    @GetMapping("")
    public String home(Model model) {
        List<Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        return "usuario/home";
    }

}
