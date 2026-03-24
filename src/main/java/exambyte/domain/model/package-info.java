/**
 * This package contains aggregate classes of the application.
 * <p>
 * The aggregates in this package follow the <b>Builder Pattern</b> for step-by-step object construction.
 * They are used to encapsulate related entities and ensure consistent state changes.
 * </p>
 *
 * <h2>UUID as ID</h2>
 * <p>
 * All aggregates use a <b>UUID</b> as their identifier for object identification and referencing.
 * This ID also serves as the database key to ensure unique identification.
 * </p>
 *
 * <h2>Typical Characteristics of Aggregates</h2>
 * <ul>
 *     <li>Use the <b>Builder Pattern</b> for flexible and safe object creation.</li>
 *     <li>Use <b>UUID</b> as a unique identifier for references.</li>
 * </ul>
 */
package exambyte.domain.model;