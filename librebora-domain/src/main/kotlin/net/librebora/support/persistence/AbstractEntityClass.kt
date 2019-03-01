package net.librebora.support.persistence

import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.exceptions.EntityNotFoundException
import java.util.*

abstract class AbstractEntityClass<DomainType, EntityType : AbstractEntity<DomainType>>(
    table: UUIDTable
) : UUIDEntityClass<EntityType>(table) {

    fun saveOrUpdate(
        id: UUID,
        source: DomainType
    ): DomainType {
        return try {
            this[id].update(source)
        } catch (cause: EntityNotFoundException) {
            new(id) {
                create(source)
            }
        }.toDomainType()
    }
}
