package org.hong.spring_ecommerce.controller;

import jakarta.servlet.http.HttpSession;
import org.hong.spring_ecommerce.model.DetalleOrden;
import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.service.IDetalleOrdenService;
import org.hong.spring_ecommerce.service.IOrdenService;
import org.hong.spring_ecommerce.service.IUsuarioService;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    private ProductoServiceImpl productoService;

    //Para almacenar los detalles de la orden
    private List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    //Almacenar los datos de la orden
    private Orden orden = new Orden();

    private IUsuarioService usuarioService;

    private IOrdenService ordenService;

    private IDetalleOrdenService detalleOrdenService;

    @Autowired
    public HomeController(ProductoServiceImpl productoService, IUsuarioService usuarioService, IOrdenService ordenService, IDetalleOrdenService detalleOrdenService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.ordenService = ordenService;
        this.detalleOrdenService = detalleOrdenService;
    }

    @GetMapping("")
    public String home(Model model, HttpSession session) {
        //Obtener el id del usuario logueado
        log.info("Sesión del usuario: {}", session.getAttribute("idusuario"));
        List<Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        //session
        model.addAttribute("sesion", session.getAttribute("idusuario"));
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

        //Validación para que no se pueda agregar un producto de nuevo y lo presente por separado
        Long idproducto = producto.getId();
        //Función lambda para verificar si el id que se añade al carrito ya está en la lista, es como un for que recorre la lista
        boolean productoExistente = detalles.stream().anyMatch(p -> p.getProducto().getId() == idproducto);

        if (!productoExistente) {
            detalles.add(detalleOrden);
        }


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

    //Método para acceder al carrito desde cualquier lugar de nuestra aplicación
    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session) {
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {
        //Para probar el único usuario que tengo
//        Usuario usuario = usuarioService.buscarUsuarioPorId(1L).get();
        //Obtenemos el id del usuario con HttpSession
        Usuario usuario = usuarioService.buscarUsuarioPorId(Long.parseLong(session.getAttribute("idusuario").toString())).get();

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);

        return "/usuario/resumenorden";
    }

    // Guardar la orden
    @GetMapping("/saveOrder")
    public String saverOrder(HttpSession session) {
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());

        // Usuario que realiza la orden
        Usuario usuario = usuarioService.buscarUsuarioPorId(Long.parseLong(session.getAttribute("idusuario").toString())).get();

        orden.setUsuario(usuario);
        ordenService.guardarOrden(orden);

        //Guardar detalles
        for (DetalleOrden dt: detalles) {
            dt.setOrden(orden);
            detalleOrdenService.guardarDetalleOrden(dt);
        }
        //Limpiar lista y orden
        orden = new Orden();
        detalles.clear();
        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model) {
        log.info("Nombre del Producto: {}", nombre );
        //Obtener todos los productos y hace una búsqueda filtrada con una función anónima de los productos que contengan el nombre que buscamos
        List<Producto> productos = productoService.listarProductos().stream()
                                                    .filter(producto -> producto.getNombre().toLowerCase()
                                                            .contains(nombre)).collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }




}
