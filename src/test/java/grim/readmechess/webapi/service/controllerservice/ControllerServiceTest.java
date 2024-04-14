package grim.readmechess.webapi.service.controllerservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ControllerServiceTest {

    @Autowired
    private ControllerService controllerService;

    @Test
    void shouldGetEngineResponse() throws ControllerServiceException {
        assertEquals(new EngineResponseDTO("e2e4", 0.09), controllerService.getEngineResponse());
    }
}