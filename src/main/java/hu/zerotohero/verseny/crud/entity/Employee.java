package hu.zerotohero.verseny.crud.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Employee {

    public enum Job {
        MANAGER,
        CASHIER,
        COOK
    }

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Job job;
    private Location worksAt;
    private Equipment operates;

}
