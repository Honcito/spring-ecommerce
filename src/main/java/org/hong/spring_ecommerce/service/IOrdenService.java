package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IOrdenService {
    List<Orden> listarOrdenes();
    Optional<Orden> buscarOrdenPorId(Long id);
    void eliminarOrden(Long id);
    Orden guardarOrden(Orden orden);
   String generarNumeroOrden();
   List<Orden> findByUsuario(Usuario usuario);
}
