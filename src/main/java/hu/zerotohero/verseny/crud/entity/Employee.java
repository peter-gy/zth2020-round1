package hu.zerotohero.verseny.crud.entity;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Table
public class Employee {

    public enum Job {
        MANAGER,
        CASHIER,
        COOK
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated
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
