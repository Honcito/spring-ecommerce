package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Orden;

import java.util.List;

public interface IOrdenService {
    List<Orden> listarOrdenes();
    Orden buscarOrdenPorId(Long id);
    void eliminarOrden(Long id);
    Orden guardarOrden(Orden orden);
}
