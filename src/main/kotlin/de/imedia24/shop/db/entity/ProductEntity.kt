package de.imedia24.shop.db.entity

import org.hibernate.annotations.UpdateTimestamp
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @Column(name = "sku", nullable = false)
    @NotNull
    val sku: String,

    @Column(name = "name", nullable = false)
    @NotNull
    val name: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "price", nullable = false)
    @NotNull
    val price: BigDecimal,

    @Column(name = "stock")
    val stock: Int = 0,

    @UpdateTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
)
