/**
 * This package contains service interfaces that act as <b>ports</b> for the infrastructure layer.
 * <p>
 * The interfaces in this package define business logic and application services for handling
 * entities such as <b>Answer</b> in the context of exam questions.
 * The implementations of these services reside in the <b>infrastructure layer</b>
 * and are used by controllers to execute business logic.
 * </p>
 *
 * <h2>Role in the Onion Architecture</h2>
 * <p>
 * This package belongs to the <b>domain layer</b> and provides an abstraction for business logic.
 * The implementations of these interfaces are provided in the <b>infrastructure layer</b>,
 * enabling interaction with the underlying infrastructure (e.g., databases).
 * </p>
 *
 * <h2>Typical Usage</h2>
 * <ul>
 *     <li>Defines interfaces for business logic to retrieve and store entities such as <b>Answer</b>.</li>
 *     <li>Allows implementations to be encapsulated in the <b>infrastructure layer</b>
 *     and used by controllers.</li>
 *     <li>Ensures that controllers only interact with business logic without being concerned
 *     with implementation details.</li>
 * </ul>
 */
package exambyte.domain.service;