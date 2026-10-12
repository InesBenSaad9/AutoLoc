package tn_esprit.tp1.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn_esprit.tp1.domain.Paiement;
import tn_esprit.tp1.exception.ResourceNotFoundException;
import tn_esprit.tp1.repository.IPaiementRepository;

@Service
@RequiredArgsConstructor
public class PaiementServicelmpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }
}
