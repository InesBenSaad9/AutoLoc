package tn_esprit.tp1.service;

import java.util.List;
import tn_esprit.tp1.domain.Maintenance;

public interface IMaintenanceService {

    Maintenance create(Maintenance entite);

    Maintenance findById(Long id);

    List<Maintenance> findAll();

    Maintenance update(Long id, Maintenance entite);

    void deleteById(Long id);
}
