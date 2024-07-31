package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarUsuarioPorId(Long id);
    void eliminarUsuario(Long id);
}
