package hu.zerotohero.verseny.crud.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

@Entity
@Table
@Data
public class Equipment {

    public enum Type {
        CASH_REGISTER,
        OVEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonProperty("locatedat")
    private Location locatedAt;

}
