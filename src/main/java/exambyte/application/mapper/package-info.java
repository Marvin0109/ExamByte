/**
 * This package contains mapper that act as <b>ports</b> for the infrastructure and web layers.
 * <p>
 * The interfaces in this package define the transformation between
 * <b>domain entities</b> and <b>DTOs</b> for the web and infrastructure layers.
 * </p>
 *
 * <h2>Role in the Onion Architecture</h2>
 * <p>
 * This package belongs to the <b>application layer</b> and provides an abstraction for
 * transforming business objects into DTOs. The implementations of these mappers
 * reside in the same layer and enable a clean separation
 * between the domain and outer layers such as web controllers.
 * </p>
 */
package exambyte.application.mapper;