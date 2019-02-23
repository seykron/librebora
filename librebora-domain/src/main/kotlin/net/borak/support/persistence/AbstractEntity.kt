package net.borak.support.persistence

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import java.util.*

abstract class AbstractEntity<DomainType>(id: EntityID<UUID>) : UUIDEntity(id) {

    abstract fun create(source: DomainType): AbstractEntity<DomainType>
    abstract fun update(source: DomainType): AbstractEntity<DomainType>
    abstract fun toDomainType(): DomainType
}
