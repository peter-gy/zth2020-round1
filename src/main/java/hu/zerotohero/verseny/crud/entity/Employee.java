package hu.zerotohero.verseny.crud.entity;

import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Location worksat;

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

    public Location getWorksat() {
        return worksat;
    }

    public void setWorksat(Location worksAt) {
        this.worksat = worksAt;
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
                ", worksAt=" + worksat +
                ", operates=" + operates +
                '}';
    }
}
