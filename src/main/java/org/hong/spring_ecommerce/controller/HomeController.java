package org.hong.spring_ecommerce.controller;

import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

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

    //Método para llevarnos del botón producto a productoHome
    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Long id) {
        log.info("Id producto enviado como parámetro {}", id);
        return "usuario/productohome";
    }

}
