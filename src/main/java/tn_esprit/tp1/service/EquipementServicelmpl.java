package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Equipement;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IEquipementRepository;

@Service
@RequiredArgsConstructor
public class EquipementServicelmpl implements IEquipementService{

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement entite) {
        if (entite.getIdEquipement() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau equipement ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return equipementRepository.save(entite);
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement update(Long id, Equipement entite) {
        Equipement existant = findById(id);
        valider(entite);
        existant.setLibelle(entite.getLibelle());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return equipementRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }

    private void valider(Equipement entite) {
        if (entite.getLibelle() == null || entite.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
    }
}
