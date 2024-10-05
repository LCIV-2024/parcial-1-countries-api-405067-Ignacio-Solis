package ar.edu.utn.frc.tup.lciii;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.dispatcher.JavaDispatcher;

import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name="countries")
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private long population;
    private double area;
    private String code;



}