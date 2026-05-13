package com.dongato.inventory.interfaces.rest.controller;

import com.dongato.inventory.sqa.QualityReport;
import com.dongato.inventory.sqa.QualityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class QualityControllerTest {

    private final QualityService qualityService = mock(QualityService.class);
    private final QualityController controller = new QualityController(qualityService);

    @Test
    @DisplayName("Debe exponer el reporte de calidad calculado por el servicio")
    void shouldExposeQualityStatusFromService() {
        QualityReport expected = new QualityReport(
                95.0,
                0,
                2,
                0,
                98.8,
                "PASSED",
                List.of("Quality standards met. Continue with continuous monitoring."));
        when(qualityService.getQualityStatus(95.0, 0, 2, 0)).thenReturn(expected);

        QualityReport actual = controller.getStatus(95.0, 0, 2, 0);

        assertSame(expected, actual);
        verify(qualityService).getQualityStatus(95.0, 0, 2, 0);
    }
}
