package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        //return usuarioRepository.getOne(id); otra forma de hacerlo
        return usuario;
    }

    @Override
    public void eliminarUsuario(Long id) {

    }
}
