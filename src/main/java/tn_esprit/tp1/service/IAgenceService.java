package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Agence;

public interface IAgenceService {

    Agence create(Agence entite);

    Agence findById(Long id);

    List<Agence> findAll();

    Agence update(Long id, Agence entite);

    void deleteById(Long id);
}
