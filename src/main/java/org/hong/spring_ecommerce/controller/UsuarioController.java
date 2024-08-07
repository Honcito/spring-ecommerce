package org.hong.spring_ecommerce.controller;

import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.service.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private UsuarioServiceImpl usuarioService;

    @Autowired
    public UsuarioController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
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
}
