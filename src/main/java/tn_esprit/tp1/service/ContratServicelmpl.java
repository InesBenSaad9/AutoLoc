package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Contrat;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IContratRepository;

@Service
@RequiredArgsConstructor
public class ContratServicelmpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat create(Contrat entite) {
        if (entite.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return contratRepository.save(entite);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat entite) {
        Contrat existant = findById(id);
        valider(entite);
        existant.setDateSignature(entite.getDateSignature());
        existant.setMontantTotal(entite.getMontantTotal());
        existant.setValide(entite.isValide());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return contratRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    private void valider(Contrat entite) {
        if (entite.getMontantTotal() != null && entite.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }
}
