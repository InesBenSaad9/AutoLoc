package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Vehicule;

public interface IVehiculeService {

    Vehicule create(Vehicule entite);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule entite);

    void deleteById(Long id);
}
