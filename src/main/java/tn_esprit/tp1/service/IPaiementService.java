package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Paiement;

public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}
