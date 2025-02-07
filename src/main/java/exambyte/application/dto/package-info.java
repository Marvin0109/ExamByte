/**
 * Dieses Package enthält Data Transfer Objects (DTOs) für die Anwendung.
 * <p>
 * DTOs dienen zur Übertragung von Daten zwischen den Schichten der Anwendung,
 * insbesondere zwischen der Infrastrukturschicht und den Web-Controllern.
 * Sie enthalten keine Geschäftslogik und sind oft immutable,
 * um eine klare Trennung zur Domänenschicht zu gewährleisten.
 * </p>
 *
 * <h2>Typische Verwendung</h2>
 * <ul>
 *     <li>Transport von Daten zwischen Backend und Frontend (z. B. REST APIs).</li>
 *     <li>Reduktion der Kopplung zwischen Domänen- und Präsentationslogik.</li>
 *     <li>Optimierung der Serialisierung von Objekten.</li>
 * </ul>
 */
package exambyte.application.dto;