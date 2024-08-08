package org.hong.spring_ecommerce.service;

import org.hong.spring_ecommerce.model.Orden;
import org.hong.spring_ecommerce.model.Usuario;
import org.hong.spring_ecommerce.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return ordenRepository.findAll();
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

    //Generar un número de orden
    //00010

    public String generarNumeroOrden() {
        int numero = 0;
        String numeroConcatenado = "";

        List<Orden> ordenes = ordenRepository.findAll();

        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

        if (ordenes.isEmpty()) {
            numero = 1;
        }else {
            //Obtiene el número mayor de la lista
            numero = numeros.stream().max(Integer::compare).get();
            numero ++;
        }
        if (numero < 10) { //0000000001, cuando llega a 10 se quita un cero de atrás, etc...
            numeroConcatenado = "000000000" + String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "000000" + String.valueOf(numero);
        }else if (numero < 10000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        }


        return numeroConcatenado;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }
}
