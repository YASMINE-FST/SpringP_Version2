package tn.fst.springproject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Univercite")
public class Univercite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUniversite")
    private long idUniversite;
    private String nomUniversite ;
    private String adresse ;

    @OneToOne(mappedBy = "univercite")
    private Foyer foyer;
}
