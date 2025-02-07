/**
 * Dieses Package enthält Entity-Mapper, die als <b>Ports</b> in der Onion-Architektur fungieren.
 * <p>
 * Die Interfaces in diesem Package definieren die Konvertierung zwischen
 * <b>Domain-Entitäten</b> und <b>Persistence-Entities</b>.
 * Sie werden von Adaptern in der <b>Persistence-Schicht</b> implementiert.
 * </p>
 *
 * <h2>Rolle in der Onion-Architektur</h2>
 * <p>
 * Dieses Package gehört zur <b>Domänenschicht</b> und bietet eine Abstraktion für
 * die Umwandlung von Datenmodellen. Die <b>Persistence-Schicht</b> (Adapter) implementiert
 * diese Interfaces, um die Infrastruktur von der Domäne zu entkoppeln.
 * </p>
 *
 * <h2>Typische Verwendung</h2>
 * <ul>
 *     <li>Definiert Schnittstellen für die Konvertierung zwischen Domain- und Datenbank-Modellen.</li>
 *     <li>Ermöglicht eine saubere Trennung der Schichten gemäß der Onion-Architektur.</li>
 *     <li>Wird von der <b>Persistence-Schicht</b> implementiert, um Datenbank-Zugriff zu kapseln.</li>
 * </ul>
 */
package exambyte.domain.entitymapper;