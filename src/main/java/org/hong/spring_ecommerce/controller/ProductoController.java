package org.hong.spring_ecommerce.controller;

import jakarta.validation.Valid;
import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.repository.ProductoRepository;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.hong.spring_ecommerce.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {


    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    private ProductoServiceImpl productoService;

    private List<Producto> productos;

    private UploadFileService upload;

    @Autowired
    public ProductoController(ProductoServiceImpl productoService, List<Producto> productos, UploadFileService upload) {
        this.productoService = productoService;
        this.productos = productos;
        this.upload = upload;
    }


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
    //El @Requestparam("img") viene de la vista create = <input type="file" class="form-control-file" id="img" name="img">
    // "img" lo coloque en la variable MultipartFile file
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Este es el objeto producto {}", producto);
        Usuario usuario = new Usuario(1L, "test","test","test@test.com","test","632598741","ADMIN","");
        producto.setUsuario(usuario);

        //Lógica para guardar la imagen
        if (producto.getId() == null) { // Cuando se crea un producto el id viene null
            String nombreImagen = upload.saveImages(file);
            //Guardar en el campo imagen de la tabla productos la imagen
            producto.setImagen(nombreImagen);
        } else {
            //Cuando se modifique el producto se cargue la misma imagen que tenía
            if (file.isEmpty()) { // Editar el producto pero no se cambia la imagen
                Producto p = new Producto();
                p = productoService.buscarProductoPorId(producto.getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                producto.setImagen(p.getImagen());
            } else {
                // Si queremos cambiar la imagen al editar el producto
                String nombreImagen = upload.saveImages(file);
                producto.setImagen(nombreImagen);
            }
        }
        productoService.guardarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@Valid @PathVariable Long id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.buscarProductoPorId(id);
            producto = optionalProducto.get();

            LOGGER.info("Producto encontrado: {}", producto);
            model.addAttribute("producto", producto);

            return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto) {
        productoService.actualizarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@Valid @PathVariable Long id) {
        productoService.eliminarProducto(id);

        return "redirect:/productos";
    }

}
