package hu.zerotohero.verseny.crud.entity;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
@Table
public class Equipment {

    public enum Type {
        CASH_REGISTER,
        OVEN
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated
    @Column(nullable = false)
    private Type type;

    @OneToOne
    @JoinColumn(nullable = false)
    private Location locatedAt;

    public Equipment() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Location getLocatedAt() {
        return locatedAt;
    }

    public void setLocatedAt(Location locatedAt) {
        this.locatedAt = locatedAt;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", locatedAt=" + locatedAt +
                '}';
    }
}
