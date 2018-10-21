package net.borak.domain.bora.model

import org.joda.time.DateTime

data class Cursor(val sectionName: String,
                  val date: DateTime,
                  val offset: Int,
                  val itemsPerPage: Int)