package org.hong.spring_ecommerce.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hong.spring_ecommerce.model.Producto;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.repository.ProductoRepository;
import org.hong.spring_ecommerce.service.ProductoServiceImpl;
import org.hong.spring_ecommerce.service.UploadFileService;
import org.hong.spring_ecommerce.service.IUsuarioService;
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
    
    private IUsuarioService usuarioService;


    @Autowired
    public ProductoController(ProductoServiceImpl productoService, List<Producto> productos, UploadFileService upload, IUsuarioService usuarioService) {
        this.productoService = productoService;
        this.productos = productos;
        this.upload = upload;
        this.usuarioService = usuarioService;
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
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto {}", producto);
//        Usuario usuario = new Usuario(1L, "test","test","test@test.com","test","632598741","ADMIN","");
        Usuario usuario = usuarioService.buscarUsuarioPorId(Long.parseLong(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(usuario);
        
        //Lógica para guardar la imagen
        if (producto.getId() == null) { // Cuando se crea un producto el id viene null
            String nombreImagen = upload.saveImages(file);
            //Guardar en el campo imagen de la tabla productos la imagen
            producto.setImagen(nombreImagen);
        } else {

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
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        //Obtener el usuario para el producto
        Producto p = new Producto();
        p = productoService.buscarProductoPorId(producto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        //Cuando se modifique el producto se cargue la misma imagen que tenía
        if (file.isEmpty()) { // Editar el producto pero no se cambia la imagen

            producto.setImagen(p.getImagen());
        } else { //Cuando se edita también la imagen
            // Si queremos cambiar la imagen al editar el producto
            //Eliminar desde el servidor la imagen

            // Eliminar cuando la imagen no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }

            String nombreImagen = upload.saveImages(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.actualizarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@Valid @PathVariable Long id) {
        //Eliminar desde el servidor la imagen
        Producto p = new Producto();
        p = productoService.buscarProductoPorId(id).get();
        // Eliminar cuando la imagen no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) {
            upload.deleteImage(p.getImagen());
        }
        productoService.eliminarProducto(id);

        return "redirect:/productos";
    }

}
