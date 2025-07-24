package tn.fst.springproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Foyer")
public class Foyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFoyer")
    private Long idFoyer;

    private String nomFoyer;
    private long capaciteFoyer;

    @OneToOne
    @JoinColumn(name = "univercite_id")
    private Univercite univercite;

    //@OneToMany(mappedBy = "foyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonManagedReference
    //private Set<Bloc> blocs;

    @OneToMany(mappedBy="foyer")
    @JsonManagedReference  // côté "géré" de la relation
    private Set<Bloc> blocs;


    public Foyer(String nomFoyer, long capaciteFoyer) {
        this.nomFoyer = nomFoyer;
        this.capaciteFoyer = capaciteFoyer;
    }


}

