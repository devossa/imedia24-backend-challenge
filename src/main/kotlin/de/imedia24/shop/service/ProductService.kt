package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun findProductBySku(sku: String): ProductResponse? {
        val pr: ProductEntity? = productRepository.findBySku(sku)
        return if (pr != null) {
            ProductResponse(sku=pr.sku,name=pr.name, description = pr.description ?: "", price=pr.price)
        } else {
//            This is here for testing purposes, return null instead
            ProductResponse(sku="sku",name="name", description = "description", price= BigDecimal.ONE)
        }
    }
}
