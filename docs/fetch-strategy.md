# Stratégie de fetch et de cascade — AutoLoc

Toutes les associations sont en `FetchType.LAZY` : on ne charge un objet lié que lorsque le code en a besoin, ce qui évite de charger toute la chaîne d'objets à chaque lecture. Le côté propriétaire (celui qui porte la clé étrangère) est toujours le côté `@ManyToOne` / `@OneToOne` ; le côté inverse utilise `mappedBy`.

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition). Supprimer le contrat supprime ses paiements, et retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence : on ne veut jamais supprimer la flotte avec l'agence. |
| Agence → Employe | LAZY | Aucune | Un employé reste une entité à part entière ; sa suppression ou sa réaffectation ne doit pas dépendre de l'agence. |
| Vehicule ↔ Equipement | LAZY | Aucune | Les équipements (GPS, siège bébé…) sont partagés entre plusieurs véhicules : supprimer un véhicule ne doit pas supprimer un équipement utilisé ailleurs. On utilise un `Set` pour éviter les doublons. |
| Client → Reservation | LAZY | PERSIST | Enregistrer un client avec de nouvelles réservations les enregistre aussi. Pas de REMOVE : l'historique des réservations doit rester. |
| Reservation → Vehicule | LAZY | Aucune | Le véhicule existe indépendamment de ses réservations. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Le contrat est généré à partir de la réservation et n'a pas de sens sans elle : la sauvegarde et la suppression de la réservation se propagent au contrat. |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance enregistrée avec son véhicule est sauvegardée en même temps. Pas de REMOVE : l'historique d'entretien est conservé. |

## Remarques

- `LAZY` impose de rester dans une transaction pour lire une collection (sinon `LazyInitializationException`), ce qui sera traité avec les requêtes dédiées et `@Transactional`.
- Sur le côté inverse d'un `@OneToOne`, Hibernate ne peut pas toujours garder le chargement paresseux sans amélioration du bytecode : l'objet lié peut donc être chargé tout de suite. Le choix `LAZY` reste le bon réglage de départ.
- Pas de `@Data` sur les entités : `toString` et `hashCode` parcourraient les deux côtés des associations bidirectionnelles et provoqueraient un `StackOverflowError`. On utilise `@Getter` et `@Setter`.
