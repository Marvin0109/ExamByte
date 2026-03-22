/**
 * This package contains mapper interfaces that act as <b>ports</b> for the infrastructure and web layers.
 * <p>
 * The interfaces in this package define the transformation between
 * <b>domain entities</b> and <b>DTOs</b> for the web and infrastructure layers.
 * They are implemented by adapters in the <b>infrastructure layer</b>.
 * </p>
 *
 * <h2>Role in the Onion Architecture</h2>
 * <p>
 * This package belongs to the <b>domain layer</b> and provides an abstraction for
 * transforming business objects into DTOs. The implementations of these mappers
 * reside in the <b>infrastructure layer</b> and enable a clean separation
 * between the domain and outer layers such as web controllers.
 * </p>
 */
package exambyte.domain.mapper;