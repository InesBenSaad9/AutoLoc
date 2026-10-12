package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Employe;

public interface IEmployeService {

    Employe create(Employe entite);

    Employe findById(Long id);

    List<Employe> findAll();

    Employe update(Long id, Employe entite);

    void deleteById(Long id);
}
