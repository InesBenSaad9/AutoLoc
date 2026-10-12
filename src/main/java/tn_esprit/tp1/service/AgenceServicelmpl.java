package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Agence;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IAgenceRepository;

@Service
@RequiredArgsConstructor
public class AgenceServicelmpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public Agence create(Agence entite) {
        if (entite.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau agence ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return agenceRepository.save(entite);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence entite) {
        Agence existant = findById(id);
        valider(entite);
        existant.setNom(entite.getNom());
        existant.setVille(entite.getVille());
        existant.setAdresse(entite.getAdresse());
        existant.setTelephone(entite.getTelephone());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return agenceRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }

    private void valider(Agence entite) {
        if (entite.getNom() == null || entite.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }
}
