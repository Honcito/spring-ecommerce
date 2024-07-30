package org.hong.spring_ecommerce.controller;

import org.hong.spring_ecommerce.model.DetalleOrden;
import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    private ProductoServiceImpl productoService;

    //Para almacenar los detalles de la orden
    private List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    //Almacenar los datos de la orden
    private Orden orden = new Orden();

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
    public String productoHome(@PathVariable Long id, Model model) {
        log.info("Id producto enviado como parámetro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.buscarProductoPorId(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Long id, @RequestParam Integer cantidad) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.buscarProductoPorId(id);
        log.info("Producto añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);

        return "usuario/carrito";
    }

}
