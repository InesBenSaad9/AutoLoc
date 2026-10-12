package tn_esprit.tp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn_esprit.tp1.domain.Agence;

public interface IAgenceRepository extends JpaRepository<Agence, Long> {
}
