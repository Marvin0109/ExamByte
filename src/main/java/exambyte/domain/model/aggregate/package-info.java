/**
 * Dieses Package enthält Aggregate-Klassen der Anwendung.
 * <p>
 * Die Aggregate in diesem Package folgen dem <b>Builder-Pattern</b> zur schrittweisen Konstruktion von Objekten.
 * Sie werden verwendet, um zusammengehörige Entitäten zu kapseln und konsistente Änderungen zu gewährleisten.
 * </p>
 *
 * <h2>UUID als Fach-ID</h2>
 * <p>
 * Alle Aggregate verwenden eine <b>UUID</b> als Fach-ID zur Identifikation und Referenzierung der Objekte.
 * Diese ID dient auch als Schlüssel in der Datenbank, um eine eindeutige Zuordnung sicherzustellen.
 * </p>
 *
 * <h2>Typische Eigenschaften der Aggregate</h2>
 * <ul>
 *     <li>Verwenden das <b>Builder-Pattern</b> für eine flexible und sichere Objekterstellung.</li>
 *     <li>Nutzen <b>UUID</b> als eindeutige Identifikation für Fach-Referenzen.</li>
 *     <li>Kapseln mehrere Entitäten in einer konsistenten Einheit.</li>
 *     <li>Stellen sicher, dass Änderungen nur über definierte Methoden erfolgen.</li>
 * </ul>
 */
package exambyte.domain.model.aggregate;