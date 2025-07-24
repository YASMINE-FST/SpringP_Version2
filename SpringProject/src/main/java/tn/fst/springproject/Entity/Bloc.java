package tn.fst.springproject.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "Bloc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bloc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBloc")
    long idBloc;

    String nomBloc;


    long capaciteBloc ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_foyer") // indique la colonne FK en base
    @JsonBackReference
    private Foyer foyer;





    @OneToMany(mappedBy = "bloc", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Chambre> chambres;


    @ManyToMany(mappedBy = "blocs")
    @JsonIgnore
    private Set<Etudiant> etudiants;

}
