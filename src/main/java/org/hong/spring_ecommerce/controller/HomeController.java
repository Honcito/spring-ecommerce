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
    public String addCart(@RequestParam Long id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.buscarProductoPorId(id);
        //Con log en consola
        log.info("Producto añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);
        producto = optionalProducto.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        //Foreing key id_producto
        detalleOrden.setProducto(producto);



        detalles.add(detalleOrden);



        sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();

        orden.setTotal(sumaTotal);

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);


        for(int i=0; i<detalles.size(); i++){
            System.out.println(detalles.get(i));
        }
        return "usuario/carrito";
    }

    //Método para poder quitar un producto del carro de compra
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Long id, Model model) {
        //Lista nueva de productos del carrito
        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
        for (DetalleOrden detalleOrden: detalles) {
            //Búsqueda del id en la lista del carrito, y si es igual al que pasamos por parámetro
            //se añade a la nueva lista, si no coincide no se añade
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }
        //Poner la nueva lista con los productos restantes
        detalles = ordenesNueva;

        //Sumatoria
        double sumaTotal = 0;
        sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "usuario/carrito";
    }

}
