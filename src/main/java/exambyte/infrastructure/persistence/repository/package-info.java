/**
 * This package contains repositories responsible for data access and persistence of entities.
 *
 * <p>
 * The repositories:
 * <ul>
 *     <li>Extend {@link org.springframework.data.repository.CrudRepository} and provide CRUD operations
 *         for application entities, including saving, loading, updating, and deleting entities.</li>
 *     <li>Provide additional custom queries to retrieve entities based on specific criteria.</li>
 *     <li>Act as an interface between the domain layer and the database, enabling simple access to entities.</li>
 * </ul>
 * </p>
 *
 * @see exambyte.infrastructure.persistence.repository.AnswerDAO
 * @see org.springframework.data.repository.CrudRepository
 */
package exambyte.infrastructure.persistence.repository;