package com.sa.healthplan.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass

public class Base extends RepresentationModel<HealthPlan> implements Serializable{
    
   @Id
   @GeneratedValue (strategy = GenerationType.IDENTITY)
   private Long id;
   private String nameplan;
   
   

}
