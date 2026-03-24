/**
 * This package contains entities that represent database tables.
 * Each entity is a mapping of a table and contains fields that correspond to the columns of the respective table.
 * The entities are used for database interaction via Spring Data.
 *
 * <p>
 * The entities:
 * <ul>
 *     <li>Represent tables in the relational database and are associated with the annotations
 *     {@link org.springframework.data.relational.core.mapping.Table}
 *     and {@link org.springframework.data.relational.core.mapping.Column}.</li>
 *     <li>Use the {@link org.springframework.data.annotation.Id} annotation to mark primary keys.</li>
 *     <li>Provide constructors, getters, and setters for accessing fields that represent database values.</li>
 *     <li>Use the Builder Pattern for entity creation.</li>
 *     <li>Perform field validation to prevent invalid inputs.</li>
 * </ul>
 * </p>
 *
 * @see exambyte.infrastructure.entity
 */
package exambyte.infrastructure.entity;
