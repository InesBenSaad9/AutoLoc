package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Equipement;

public interface IEquipementService {

    Equipement create(Equipement entite);

    Equipement findById(Long id);

    List<Equipement> findAll();

    Equipement update(Long id, Equipement entite);

    void deleteById(Long id);
}
