package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.sqa.QualityReport;
import com.dongato.inventory.sqa.QualityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QualityControllerTest {

    @Mock
    private QualityService qualityService;

    @InjectMocks
    private QualityController qualityController;

    @Test
    @DisplayName("Should return quality report from service")
    void shouldReturnQualityReport() {
        QualityReport report = new QualityReport(90.0, 0, 1, 0, 96.0, "PASSED",
                List.of("Quality standards met. Continue with continuous monitoring."));

        when(qualityService.getQualityStatus(90.0, 0, 1, 0)).thenReturn(report);

        QualityReport response = qualityController.getStatus(90.0, 0, 1, 0);

        assertNotNull(response);
        assertEquals(96.0, response.score());
        assertEquals("PASSED", response.status());
    }
}
