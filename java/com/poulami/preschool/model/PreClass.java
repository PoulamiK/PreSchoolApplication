package com.poulami.preschool.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Data
@Entity
@Table(name = "class")
public class PreClass extends BaseEntity{

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int classId;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @OneToMany(mappedBy = "preClass", fetch = FetchType.LAZY,
                cascade = CascadeType.PERSIST, targetEntity = Person.class)
    private Set<Person> persons;
}
