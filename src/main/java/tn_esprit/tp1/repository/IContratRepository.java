package tn_esprit.tp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn_esprit.tp1.domain.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
}
