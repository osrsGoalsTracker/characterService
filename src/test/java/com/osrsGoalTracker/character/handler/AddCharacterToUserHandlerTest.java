package com.osrsGoalTracker.character.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.service.CharacterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddCharacterToUserHandlerTest {

    @Mock
    private CharacterService characterService;

    @Mock
    private Context context;

    private AddCharacterToUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AddCharacterToUserHandler(characterService);
    }

    @Test
    void handleRequest_ValidInput_ReturnsSuccessResponse() {
        // Given
        String userId = "testUser123";
        String characterName = "TestChar";
        LocalDateTime now = LocalDateTime.now();

        Character character = Character.builder()
                .name(characterName)
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", userId);
        pathParameters.put("name", characterName);

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        when(characterService.addCharacterToUser(userId, characterName)).thenReturn(character);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(characterService).addCharacterToUser(userId, characterName);
    }

    @Test
    void handleRequest_NullInput_ReturnsBadRequest() {
        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(null, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Request cannot be null\"}", response.getBody());
    }

    @Test
    void handleRequest_NullPathParameters_ReturnsBadRequest() {
        // Given
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Path parameters cannot be null\"}", response.getBody());
    }

    @Test
    void handleRequest_MissingUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("name", "TestChar");

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"User ID cannot be null or empty\"}", response.getBody());
    }

    @Test
    void handleRequest_MissingCharacterName_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", "testUser123");

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Character name cannot be null or empty\"}", response.getBody());
    }

    @Test
    void handleRequest_ServiceThrowsException_ReturnsBadRequest() {
        // Given
        String userId = "testUser123";
        String characterName = "TestChar";

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", userId);
        pathParameters.put("name", characterName);

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        when(characterService.addCharacterToUser(anyString(), anyString()))
                .thenThrow(new RuntimeException("Service error"));

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Error processing request: Service error\"}", response.getBody());
    }
}