/**
 * This package contains entity mappers that act as <b>ports</b> in the Onion Architecture.
 * <p>
 * The interfaces in this package define the conversion between
 * <b>domain entities</b> and <b>persistence entities</b>.
 * They are implemented by adapters in the <b>persistence layer</b>.
 * </p>
 *
 * <h2>Role in the Onion Architecture</h2>
 * <p>
 * This package belongs to the <b>domain layer</b> and provides an abstraction for
 * transforming data models. The <b>persistence layer</b> (adapters) implements
 * these interfaces to decouple the infrastructure from the domain.
 * </p>
 */
package exambyte.infrastructure.mapper;