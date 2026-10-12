package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Client;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IClientRepository;

@Service
@RequiredArgsConstructor
public class ClientServicelmpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client entite) {
        if (entite.getIdClient() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant");
        }
        valider(entite);
        return clientRepository.save(entite);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client entite) {
        Client existant = findById(id);
        valider(entite);
        existant.setNom(entite.getNom());
        existant.setPrenom(entite.getPrenom());
        existant.setEmail(entite.getEmail());
        existant.setTelephone(entite.getTelephone());
        existant.setNumPermis(entite.getNumPermis());
        existant.setDateInscription(entite.getDateInscription());
        // les associations ne sont pas recopiées ici (Chapitre 9)
        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private void valider(Client entite) {
        if (entite.getNom() == null || entite.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (entite.getEmail() == null || entite.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'e-mail du client est obligatoire");
        }
    }
}
