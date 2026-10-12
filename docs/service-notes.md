# Atelier 4 — Notes : couche Service, IoC et DI

## A. Modes d'injection comparés

| Critère | Constructeur (`@RequiredArgsConstructor` + `final`) | Attribut (`@Autowired` sur le champ) |
|---|---|---|
| Champ `final` possible ? | Oui | Non |
| Dépendance visible dans la signature de la classe ? | Oui (paramètre du constructeur) | Non (dépendance cachée) |
| Utilisable avec `new` hors conteneur sans dépendance `null` ? | Oui : le constructeur impose le repository | Non : le champ reste `null` |
| Signalé par SonarQube for IDE ? | Non | Oui (règle S6813 — à vérifier dans le panneau) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Interface injectée, champ final, service sans état |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Idem ; refuse un tarif négatif |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem |
| ClientServiceImpl | IClientRepository | Constructeur | Idem |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem ; refuse dateFin < dateDebut |
| ContratServiceImpl | IContratRepository | Constructeur | Idem ; refuse un montant négatif ; suppression en cascade des paiements |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule : les paiements passent par le Contrat |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem ; refuse dateFin < dateDebut |

## C. Messages d'erreur

**Message 1 — `NoSuchBeanDefinitionException`** : aucun bean de type `IContratService` pour le constructeur de `ContratController`.
Causes probables : `@Service` oublié sur `ContratServiceImpl` ; classe placée hors du package racine scanné.
Correction : ajouter `@Service` ; vérifier que la classe est sous le package racine de `@SpringBootApplication`.

**Message 2 — `NoUniqueBeanDefinitionException`** : deux beans (`emailNotificateur`, `smsNotificateur`) pour un même type.
Correction 1 : `@Primary` sur la **classe** du bean par défaut.
Correction 2 : `@Qualifier("emailNotificateur")` sur le **paramètre du constructeur** du contrôleur.

**Message 3 — dépendance circulaire** entre `clientServiceImpl` et `reservationServiceImpl`.
Avec l'injection par constructeur, chaque bean doit exister **complet** avant d'être injecté : A attend B qui attend A, la création est impossible (contrairement au setter/attribut où l'objet peut être créé puis complété).
Piste : extraire la responsabilité commune dans un troisième service (ou ne garder qu'un sens de dépendance, par ex. Reservation → Client).

## D. Anomalies SonarQube for IDE

| Règle | Explication | Correction |
|---|---|---|
| S6813 | Injection par attribut (`@Autowired` sur champ) | Retour à l'injection par constructeur + `final` |
| S112 | Levée d'une `RuntimeException` générique | Utiliser `ResourceNotFoundException` |
| à compléter | à relever dans votre IDE (import inutile, commentaire de code…) | à compléter |

## E. Questions de compréhension

1. Le `new` est exécuté par le conteneur Spring (`ApplicationContext`) au démarrage, grâce à `@Service` et au scan des composants ; il appelle le constructeur en lui passant le proxy du repository.
2. Le contrôleur dépend de l'interface pour rester découplé de l'implémentation : on peut changer ou remplacer l'implémentation sans toucher au contrôleur, et c'est plus testable.
3. Un singleton est partagé par tous les threads. Un attribut modifiable (ex. un `contratCourant`) serait écrasé par une requête concurrente : l'utilisateur A pourrait modifier ou voir le contrat de l'utilisateur B.
4. Passer directement l'objet reçu à `save` déclencherait un `merge` qui écraserait toutes les colonnes (champs non fournis = `null`) et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge donc l'existant puis on recopie les attributs modifiables.
5. `@Component` s'appose sur une classe qu'on écrit ; `@Bean` est une méthode dans une `@Configuration`, pour une classe qu'on ne peut pas annoter (bibliothèque, JDK) ou une construction particulière. `@Primary` (sur la classe) désigne le candidat par défaut ; `@Qualifier` (à l'injection) choisit explicitement un bean par son nom. `IPaiementService` est en lecture seule car un paiement n'existe que rattaché à son contrat (composition) : création, modification et suppression passent par le Contrat (cascade ALL + orphanRemoval).
