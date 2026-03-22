/**
 * This package contains repository interfaces that act as <b>ports</b> for the persistence layer.
 * <p>
 * The interfaces in this package define methods for interacting with the database,
 * especially for retrieving and storing <b>domain entities</b>.
 * </p>
 *
 * <h2>Role in the Onion Architecture</h2>
 * <p>
 * This package belongs to the <b>domain layer</b> and provides an abstraction for
 * database access. The implementations of these repositories reside in the
 * <b>persistence layer</b> and ensure a clean separation between the domain and
 * the underlying database infrastructure.
 * </p>
 *
 * <h2>Typical Usage</h2>
 * <ul>
 *     <li>Defines interfaces for database interaction to store and retrieve entities such as <b>Answer</b>.</li>
 *     <li>Uses the Repository Pattern to provide a well-defined contract for data access logic.</li>
 *     <li>Ensures that the rest of the application does not directly depend on the database,
 *     but instead interacts through the domain layer.</li>
 * </ul>
 */
package exambyte.domain.repository;
