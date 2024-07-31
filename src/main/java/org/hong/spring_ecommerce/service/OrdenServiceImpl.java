package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenServiceImpl implements IOrdenService{

    private OrdenRepository ordenRepository;

    @Autowired
    public OrdenServiceImpl(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Override
    public List<Orden> listarOrdenes() {
        return List.of();
    }

    @Override
    public Orden buscarOrdenPorId(Long id) {
        return null;
    }

    @Override
    public void eliminarOrden(Long id) {

    }

    @Override
    public Orden guardarOrden(Orden orden) {
        return ordenRepository.save(orden);
    }
}
