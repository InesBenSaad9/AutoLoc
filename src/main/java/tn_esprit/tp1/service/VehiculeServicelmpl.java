package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Vehicule;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
public class VehiculeServicelmpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Vehicule create(Vehicule entite) {
        if (entite.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau vehicule ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return vehiculeRepository.save(entite);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule entite) {
        Vehicule existant = findById(id);
        valider(entite);
        existant.setImmatriculation(entite.getImmatriculation());
        existant.setMarque(entite.getMarque());
        existant.setModele(entite.getModele());
        existant.setCategorie(entite.getCategorie());
        existant.setTarifJournalier(entite.getTarifJournalier());
        existant.setStatut(entite.getStatut());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return vehiculeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    private void valider(Vehicule entite) {
        if (entite.getImmatriculation() == null || entite.getImmatriculation().isBlank()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }
        if (entite.getTarifJournalier() != null && entite.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
    }
}
