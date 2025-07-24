package tn.fst.springproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tn.fst.springproject.Reservation.Reservation;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Chambre")
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChambre")
    private int idChambre;

    private long nombreChambre;
    private long capacite;

    @Enumerated(EnumType.STRING)
    @Column(name = "TypeChambre")
    private TypeChambre typeC;

    @ManyToOne
    @JsonBackReference
    private Bloc bloc;

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Reservation> reservations;

    @PrePersist
    @PreUpdate
    private void calculerCapaciteTotale() {
        if (typeC != null && nombreChambre > 0) {
            int parChambre = switch (typeC) {
                case SIMPLE -> 1;
                case DOUBLE -> 2;
                case TRIPLE -> 3;
            };
            this.capacite = parChambre * this.nombreChambre;
        }
    }
}
