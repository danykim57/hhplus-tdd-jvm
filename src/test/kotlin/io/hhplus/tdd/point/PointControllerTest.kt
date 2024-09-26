package io.hhplus.tdd.point

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest(val mockMvc: MockMvc) {


    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("포인트 추가 동시성 테스트")
    fun `charge amount concurrency test`() {

        mockMvc.perform(
            patch("/point/1/charge").content(objectMapper.writeValueAsString(1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("point", "1").exists())

        mockMvc.perform(
            patch("/point/1/charge").content(objectMapper.writeValueAsString(1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("point", "2").exists())

        mockMvc.perform(
            patch("/point/1/charge").content(objectMapper.writeValueAsString(1))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("point", "3").exists())

    }
}