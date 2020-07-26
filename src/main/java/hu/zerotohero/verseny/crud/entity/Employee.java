package hu.zerotohero.verseny.crud.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Location worksAt;

    @OneToOne
    @JoinColumn(nullable = false)
    private Equipment operates;

    public Employee() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @JsonProperty("worksat")
    public Location getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(Location worksAt) {
        this.worksAt = worksAt;
    }

    public Equipment getOperates() {
        return operates;
    }

    public void setOperates(Equipment operates) {
        this.operates = operates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id) &&
                name.equals(employee.name) &&
                job == employee.job &&
                worksAt.equals(employee.worksAt) &&
                operates.equals(employee.operates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, job, worksAt, operates);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job=" + job +
                ", worksAt=" + worksAt +
                ", operates=" + operates +
                '}';
    }
}
