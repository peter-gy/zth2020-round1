package hu.zerotohero.verseny.crud.entity;


import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

@Entity
@Table
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
}
