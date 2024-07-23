package org.hong.spring_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "username")
    @NotEmpty(message = "Campo requerido")
    private String username;

    @Column(name = "email")
    @NotBlank(message = "Campo requerido")
    @Email(message = "Email debe ser válido")
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<Orden> ordenes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();

}
