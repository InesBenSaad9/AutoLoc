package tn_esprit.tp1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    // Côté propriétaire (clé étrangère client_id_client)
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    // Côté propriétaire (clé étrangère vehicule_id_vehicule), aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicule vehicule;

    // Côté inverse du OneToOne : ALL (le contrat dépend de sa réservation)
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contrat contrat;
}
