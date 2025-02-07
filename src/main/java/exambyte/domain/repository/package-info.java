/**
 * Dieses Package enthält Repository-Interfaces, die als <b>Ports</b> für die Persistence-Schicht dienen.
 * <p>
 * Die Interfaces in diesem Package definieren Methoden zur Interaktion mit der Datenbank,
 * insbesondere zum Abrufen und Speichern von <b>Domain-Entitäten</b>.
 * </p>
 *
 * <h2>Rolle in der Onion-Architektur</h2>
 * <p>
 * Dieses Package gehört zur <b>Domänenschicht</b> und stellt eine Abstraktion für die
 * Datenbankzugriffe bereit. Die Implementierungen dieser Repositories befinden sich in der
 * <b>Persistence-Schicht</b> und ermöglichen eine saubere Trennung zwischen der Domäne und
 * der zugrunde liegenden Datenbankinfrastruktur.
 * </p>
 *
 * <h2>Typische Verwendung</h2>
 * <ul>
 *     <li>Definiert Schnittstellen zur Interaktion mit der Datenbank, um Entitäten wie z.B. <b>Antworten</b> zu speichern und abzurufen.</li>
 *     <li>Verwendet das Repository-Pattern, um eine klar definierte Schnittstelle für die Datenzugriffslogik zu bieten.</li>
 *     <li>Stellt sicher, dass der Rest der Anwendung keine direkte Abhängigkeit zur Datenbank hat, sondern über die Domänenschicht interagiert.</li>
 * </ul>
 */
package exambyte.domain.repository;