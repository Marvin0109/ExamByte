/**
 * Dieses Package enthält Service-Interfaces, die als <b>Ports</b> für die Infrastruktur-Schicht dienen.
 * <p>
 * Die Interfaces in diesem Package definieren Geschäftslogik und Anwendungsdienste für
 * die Bearbeitung von z.B. <b>Antworten</b> im Kontext von Prüfungsfragen.
 * Die Implementierungen dieser Services befinden sich in der <b>Infrastruktur-Schicht</b>
 * und werden vom Controller verwendet, um die Geschäftslogik auszuführen.
 * </p>
 *
 * <h2>Rolle in der Onion-Architektur</h2>
 * <p>
 * Dieses Package gehört zur <b>Domänenschicht</b> und stellt eine Abstraktion für die
 * Geschäftslogik zur Verfügung. Die Implementierungen dieser Interfaces werden in der
 * <b>Infrastruktur-Schicht</b> implementiert, um die Interaktion mit der zugrunde liegenden
 * Infrastruktur (z.B. Datenbanken) zu ermöglichen.
 * </p>
 *
 * <h2>Typische Verwendung</h2>
 * <ul>
 *     <li>Definiert Schnittstellen für die Geschäftslogik zum Abrufen und Speichern von z.B. <b>Antworten</b>.</li>
 *     <li>Ermöglicht es, die Implementierungen in der <b>Infrastruktur-Schicht</b> zu kapseln und vom Controller zu verwenden.</li>
 *     <li>Stellt sicher, dass der Controller lediglich auf die Geschäftslogik zugreift, ohne sich um Implementierungsdetails zu kümmern.</li>
 * </ul>
 */
package exambyte.domain.service;