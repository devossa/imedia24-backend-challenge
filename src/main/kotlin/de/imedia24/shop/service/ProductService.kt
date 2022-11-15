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
        } else null
    }

    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { ProductResponse(sku = it.sku, name = it.name, description = it.description ?: "", price = it.price, stock = it.stock) }
    }
    fun findProductsBySku(skus: List<String>): List<ProductResponse> {
        val products: MutableList<ProductResponse> = mutableListOf<ProductResponse>()
        for (sku: String in skus) {
            val pr: ProductEntity? = productRepository.findBySku(sku)
            if (pr != null) {
                products.add(
                    ProductResponse(
                        sku = pr.sku,
                        name = pr.name,
                        description = pr.description ?: "",
                        price = pr.price
                    )
                )
            }
        }
        return products
    }

    fun save(product: ProductEntity): ProductEntity {
        return productRepository.save(product)
    }
}
