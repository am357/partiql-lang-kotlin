package main.kotlin.org.partiql.catalog

import org.partiql.lang.types.SchemaType

data class Table(
    val name: String,
    val schema: List<SchemaType>
)
