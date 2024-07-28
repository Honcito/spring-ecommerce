package org.hong.spring_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_orden")
    private Long id;

    private String numero;
    private Date fechaCreacion;
    private Date fechaRecibida;
    private double total;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalle;

}

