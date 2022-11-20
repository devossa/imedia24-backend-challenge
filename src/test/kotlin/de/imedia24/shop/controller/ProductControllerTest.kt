package de.imedia24.shop.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.db.entity.ProductEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
internal class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
//    Basic testing, still got a lot to learn !
    @Test
    fun `Get products by SKU`() {
        mockMvc.get("/products?sku=1,2,3,4")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `Partially update product`() {
        val newProduct = ProductEntity("1", "first product", "desc goes here", BigDecimal.TEN, 3)

        val performPost = mockMvc.post("/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newProduct)
        }

        performPost.andExpect { status { isCreated() } }

        val updatedValues = mutableMapOf<String, String>()
        updatedValues.set("sku", "1")
        updatedValues.set("name", "new name")
        updatedValues.set("description", "new desc")

        mockMvc.patch("/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedValues)
        }.andExpect { status { isOk() } }
    }
}