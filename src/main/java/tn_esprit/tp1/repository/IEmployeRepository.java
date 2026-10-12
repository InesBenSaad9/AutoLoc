package tn_esprit.tp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn_esprit.tp1.domain.Employe;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}
