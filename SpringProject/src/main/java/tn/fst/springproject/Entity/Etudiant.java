package tn.fst.springproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tn.fst.springproject.Reservation.Reservation;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "etudiant")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "nom_et")
    private String nomEt;

    @Column(name = "prenom_et")
    private String prenomEt;

    @Column(name = "cin")
    private Long cin;

    @Column(name = "ecole")
    private String ecole;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_naissance")
    private Date dateNaissance;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")  // Juste une string : "USER" ou "ADMIN"
    private String role;

    // Jointure Bloc
    @ManyToMany
    @JoinTable(
            name = "etudiant_blocs",
            joinColumns = @JoinColumn(name = "etudiant_id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "blocs_id_bloc")
    )
    @JsonIgnore
    private Set<Bloc> blocs;

    // Jointure Reservation
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Reservation> reservations;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "reset_code_expiry")
    private Date resetCodeExpiry;

}
