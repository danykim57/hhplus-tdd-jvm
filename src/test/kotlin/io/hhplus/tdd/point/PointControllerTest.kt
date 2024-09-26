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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.random.Random

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest(val mockMvc: MockMvc) {


    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("포인트 추가 및 사용 랜덤 통시성 테스트")
    fun `point charge and use concurrency integration test`() {

        val numberOfThreads = 10
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val appliedNum = 1;
        var originalPoint = 100;

        mockMvc.perform(
            patch("/point/1/charge").content(objectMapper.writeValueAsString(originalPoint))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("point", originalPoint).exists())

        for (i in 0 until numberOfThreads) {
            val randomFlag = Random.nextInt(2)
            if (randomFlag == 1)
            executorService.submit {
                originalPoint += appliedNum
                mockMvc.perform(
                    patch("/point/1/charge").content(objectMapper.writeValueAsString(appliedNum))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("point", originalPoint).exists())
            }
            else if (randomFlag == 0) {
                originalPoint -= appliedNum
                mockMvc.perform(
                    patch("/point/1/use").content(objectMapper.writeValueAsString(appliedNum))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("point", originalPoint).exists())

            }

            latch.countDown()
        }
    }

    @Test
    @DisplayName("포인트 추가 통시성 테스트")
    fun `charge amount concurrency integration test`() {

        val numberOfThreads = 10
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val incrementNum = 1;
        var originalPoint = 0;

        for (i in 0 until numberOfThreads) {
            executorService.submit {
                originalPoint += incrementNum
                mockMvc.perform(
                    patch("/point/1/charge").content(objectMapper.writeValueAsString(incrementNum))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("point", originalPoint).exists())
                latch.countDown()
            }
        }
    }

    @Test
    @DisplayName("포인트 추가 테스트")
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