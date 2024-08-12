package org.hong.spring_ecommerce.controller;

import jakarta.servlet.http.HttpSession;
import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.service.IOrdenService;
import org.hong.spring_ecommerce.service.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private UsuarioServiceImpl usuarioService;
    private IOrdenService ordenService;

    @Autowired
    public UsuarioController(UsuarioServiceImpl usuarioService, IOrdenService ordenService) {
        this.usuarioService = usuarioService;
        this.ordenService = ordenService;
    }

    @GetMapping("/registro")
    public String create() {

        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario) {
        log.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuarioService.guardarUsuario(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {

        return "usuario/login";
    }

    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session) {
        log.info("Accesos: {}", usuario);
        Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());

        if (user.isPresent()) {
            Usuario usuarioDb = user.get();
            log.info("Usuario de db: {}", usuarioDb);
            session.setAttribute("idusuario", usuarioDb.getId());

            if (usuarioDb.getTipo().equals("ADMIN")) {
                return "redirect:/administrador";
            } else {
                return "redirect:/";
            }
        } else {
            log.info("El Usuario no existe");
            return "redirect:/";
        }
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = usuarioService.buscarUsuarioPorId(Long.parseLong(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);
        model.addAttribute("ordenes", ordenes);
        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Long id, HttpSession session, Model model) {
        log.info("Id de la orden: {}", id);
        Optional<Orden> orden = ordenService.buscarOrdenPorId(id);

        model.addAttribute("detalles", orden.get().getDetalle());
        //session
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/detallecompra";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        return "redirect:/";
    }

}


