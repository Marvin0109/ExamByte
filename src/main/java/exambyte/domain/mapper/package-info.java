/**
 * Dieses Package enthält Mapper-Interfaces, die als <b>Ports</b> für die Infrastruktur- und Web-Schicht dienen.
 * <p>
 * Die Interfaces in diesem Package definieren die Umwandlung zwischen
 * <b>Domain-Entitäten</b> und <b>DTOs</b> für die Web- und Infrastruktur-Schicht.
 * Sie werden von Adaptern in der <b>Infrastruktur-Schicht</b> implementiert.
 * </p>
 *
 * <h2>Rolle in der Onion-Architektur</h2>
 * <p>
 * Dieses Package gehört zur <b>Domänenschicht</b> und stellt eine Abstraktion für die
 * Umwandlung von Geschäftsobjekten zu DTOs bereit. Die Implementierungen dieser Mapper
 * befinden sich in der <b>Infrastruktur-Schicht</b> und ermöglichen eine saubere Trennung
 * zwischen Domäne und äußeren Schichten wie Web-Controllern.
 * </p>
 *
 * <h2>Typische Verwendung</h2>
 * <ul>
 *     <li>Definiert Schnittstellen für die Konvertierung zwischen Domain-Entitäten und DTOs.</li>
 *     <li>Ermöglicht eine lose Kopplung zwischen Web- und Infrastruktur-Schicht.</li>
 *     <li>Wird von der <b>Infrastruktur-Schicht</b> implementiert, um Daten für externe Systeme bereitzustellen.</li>
 * </ul>
 */
package exambyte.domain.mapper;