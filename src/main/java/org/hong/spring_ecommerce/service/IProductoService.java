package org.hong.spring_ecommerce.service;


import org.hong.spring_ecommerce.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<Producto> listarProductos();
    Producto guardarProducto(Producto producto);
    Optional<Producto> buscarProductoPorId(Long id);
    void actualizarProducto(Producto producto);
    void eliminarProducto(Long id);

}
