/**
 * Das Paket enthält die Entitäten, die die Datenbanktabellen repräsentieren.
 * Jede Entität ist eine Abbildung einer Tabelle und enthält Felder, die mit den Spalten der jeweiligen Tabelle korrespondieren.
 * Die Entitäten werden für die Interaktion mit der Datenbank über Spring Data verwendet.
 *
 * <p>
 * Die Entitäten:
 * <ul>
 *     <li>Repräsentieren Tabellen in der relationalen Datenbank und sind mit der Annotation {@link org.springframework.data.relational.core.mapping.Table}
 *         und {@link org.springframework.data.relational.core.mapping.Column} verbunden.</li>
 *     <li>Verwenden die {@link org.springframework.data.annotation.Id}-Annotation, um Primärschlüssel zu kennzeichnen.</li>
 *     <li>Bieten Konstruktoren sowie Getter und Setter für den Zugriff auf Felder, die die Datenbankwerte repräsentieren.</li>
 *     <li>Nutzen das Builder-Pattern zur Erstellung von Entitäten.</li>
 * </ul>
 * </p>
 * @see exambyte.persistence.entities
 */
package exambyte.infrastructure.persistence.entities;
