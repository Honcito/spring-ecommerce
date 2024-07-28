package org.hong.spring_ecommerce.controller;

import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.repository.ProductoRepository;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {


    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    private ProductoServiceImpl productoService;

    private List<Producto> productos;

    @Autowired
    public ProductoController(ProductoServiceImpl productoService, List<Producto> productos) {
        this.productoService = productoService;
        this.productos = productos;
    }


//    @GetMapping("")
//    public String show() {
//        return "productos/show";
//    }

    @GetMapping("")
    public String listar(Model model) {
        List<Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto){
        LOGGER.info("Este es el objeto producto {}", producto);
        Usuario usuario = new Usuario(1L, "test","test","test@test.com","test","632598741","ADMIN","");
        producto.setUsuario(usuario);
        productoService.guardarProducto(producto);
        return "redirect:/productos";
    }

}
