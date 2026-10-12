package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Employe;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IEmployeRepository;

@Service
@RequiredArgsConstructor
public class EmployeServicelmpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe entite) {
        if (entite.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau employe ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return employeRepository.save(entite);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe entite) {
        Employe existant = findById(id);
        valider(entite);
        existant.setNom(entite.getNom());
        existant.setPrenom(entite.getPrenom());
        existant.setRole(entite.getRole());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    private void valider(Employe entite) {
        if (entite.getNom() == null || entite.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (entite.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
    }
}
