package hu.zerotohero.verseny.crud.entity;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @OneToOne
    @JoinColumn(nullable = false)
    private Location locatedat;

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

    public Location getLocatedat() {
        return locatedat;
    }

    public void setLocatedat(Location locatedAt) {
        this.locatedat = locatedAt;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", locatedAt=" + locatedat +
                '}';
    }
}
