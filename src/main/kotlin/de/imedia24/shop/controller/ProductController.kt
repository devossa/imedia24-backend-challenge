package de.imedia24.shop.controller

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import javax.websocket.server.PathParam

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/product/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product)
        }
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findProducts(
        @PathParam("skus") skus: String?
    ): ResponseEntity<List<ProductResponse>> {
        if (skus == null) return ResponseEntity.ok(productService.getAllProducts())

        val skusList: List<String> = skus.split(',')
        logger.info("Request for products ${skusList.toString()}")

        return ResponseEntity.ok(productService.findProductsBySku(skusList))
    }

    @PostMapping("/products", produces = ["application/json;charset=utf-8"])
    fun createProduct(
        @RequestBody body: Map<String, String>
    ): ResponseEntity<ProductResponse> {
        if (!body.containsKey("sku") or !body.containsKey("name")) return ResponseEntity.badRequest().build()

        val existingProduct = productService.findProductBySku(body["sku"]!!)
        if (existingProduct != null) return ResponseEntity.status(400).build()
//        Should check for type casting errors here
        val newProduct = ProductEntity(
            sku=body["sku"]!!,
            name=body.getOrDefault("name", ""),
            description = body.getOrDefault("description", ""),
            price= BigDecimal(body.getOrDefault("price", "0")),
            stock=body.getOrDefault("stock", "0").toInt(),
        )

        val savedProduct: ProductEntity = productService.save(newProduct)
        logger.info("Adding product to db with sku=${newProduct.sku}")

        return ResponseEntity.status(201).body(ProductResponse(
            sku = savedProduct.sku,
            name = savedProduct.name,
            description = savedProduct.description ?: "",
            price = savedProduct.price,
            stock = savedProduct.stock ?: 0
        ))
    }

    @PatchMapping("/products", produces = ["application/json;charset=utf-8"])
    fun updateProduct(
        @RequestBody body: Map<String, String>,
    ): ResponseEntity<ProductResponse> {
        if (!body.containsKey("sku")) return ResponseEntity.notFound().build()
        val existingProduct = productService.findProductBySku(body["sku"]!!) ?: return ResponseEntity.notFound().build()

        val updatedProduct = ProductEntity(
            existingProduct.sku,
            body.getOrDefault("name", existingProduct.name),
            body.getOrDefault("description", existingProduct.description),
            body.getOrDefault("price", existingProduct.price) as BigDecimal,
            existingProduct.stock,
        )

        productService.save(updatedProduct)
        logger.info("Updating product with sku=${body["sku"]}")


        return ResponseEntity.ok(ProductResponse(
            sku = updatedProduct.sku,
            name = updatedProduct.name,
            description = updatedProduct.description ?: "",
            price = updatedProduct.price,
            stock = updatedProduct.stock ?: 0
        ))
    }
}
