package hu.zerotohero.verseny.crud.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

@Entity
@Table
@Data
public class Employee {

    public enum Job {
        MANAGER,
        CASHIER,
        COOK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Job job;

    @Column(nullable = false)
    private Integer salary;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonProperty("worksat")
    private Location worksAt;

    @OneToOne
    @JoinColumn(nullable = false)
    private Equipment operates;
}
