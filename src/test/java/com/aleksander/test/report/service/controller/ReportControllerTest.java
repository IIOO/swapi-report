package com.aleksander.test.report.service.controller;

import com.aleksander.test.report.dto.GenerateReportCriteriaDto;
import com.aleksander.test.report.dto.ReportDto;
import com.aleksander.test.report.persistance.ReportEntity;
import com.aleksander.test.report.repository.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deleteAll() throws Exception {
        // when
        final MvcResult result = mockMvc.perform(delete("/report")).andReturn();
        //then
        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void deleteByIdNotEmpty() throws Exception {
        // given
        long reportId = 1L;
        reportRepository.save(buildValidReportEntity(reportId));
        // when
        final MvcResult result = mockMvc.perform(delete("/report/" + reportId)).andReturn();
        //then
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deleteByIdEmpty() throws Exception {
        // when
        final MvcResult result = mockMvc.perform(delete("/report/1")).andReturn();
        //then
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void readAllEmpty() throws Exception {
        // when
        final MvcResult result = mockMvc.perform(get("/report")).andReturn();
        // then
        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void readAll() throws Exception {
        //given
        reportRepository.save(buildValidReportEntity(1L));
        reportRepository.save(buildValidReportEntity(2L));

        // when
        final MvcResult result = mockMvc.perform(get("/report")).andReturn();

        // then
        assertEquals(200, result.getResponse().getStatus());
        final List<ReportDto> returned = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), ReportDto[].class));
        assertEquals(2, returned.size());
    }

    @Test
    @DirtiesContext
    void getByExistingId() throws Exception {
        //given
        reportRepository.save(buildValidReportEntity(1L));

        // when
        final MvcResult result = mockMvc.perform(get("/report/1")).andReturn();

        //then
        assertEquals(200, result.getResponse().getStatus());
        final ReportDto returned = objectMapper.readValue(result.getResponse().getContentAsString(), ReportDto.class);
        assertEquals(1L, returned.getReportId());
    }

    @Test
    void getByNonExistingId() throws Exception {
        // when
        final MvcResult result = mockMvc.perform(get("/report/50")).andReturn();

        //then
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @DirtiesContext
    void put() throws Exception {
        // given
        GenerateReportCriteriaDto criteria = new GenerateReportCriteriaDto("Luke", "Tatoo");
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/report/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria));

        // when
        final MvcResult result = mockMvc.perform(builder).andReturn();

        //then
        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void putBadRequest() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/report/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("BAD REQUEST");

        // when
        final MvcResult result = mockMvc.perform(builder).andReturn();

        //then
        assertEquals(400, result.getResponse().getStatus());
    }


    private ReportEntity buildValidReportEntity(long reportId) {
        return ReportEntity.builder()
                .id(reportId)
                .queryCriteriaPlanetName("test")
                .queryCriteriaCharacterPhrase("test")
                .result(new HashSet<>())
                .build();
    }
}
