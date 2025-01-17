package com.osrsGoalTracker.character.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.service.CharacterService;
import com.osrsGoalTracker.utils.JsonUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCharactersForUserHandlerTest {

    @Mock
    private CharacterService characterService;

    @Mock
    private Context context;

    private GetCharactersForUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetCharactersForUserHandler(characterService);
    }

    @Test
    void handleRequest_ValidInput_ReturnsSuccessResponse() {
        // Given
        String userId = "testUser123";
        LocalDateTime now = LocalDateTime.now();

        List<Character> characters = Arrays.asList(
                Character.builder()
                        .name("TestChar1")
                        .userId(userId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                Character.builder()
                        .name("TestChar2")
                        .userId(userId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", userId);

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        when(characterService.getCharactersForUser(userId)).thenReturn(characters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        List<String> expectedNames = Arrays.asList("TestChar1", "TestChar2");
        assertEquals(JsonUtils.toJson(expectedNames), response.getBody());
        verify(characterService).getCharactersForUser(userId);
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
    void handleRequest_EmptyUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", "");
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
    void handleRequest_WhitespaceUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", "   ");
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"User ID cannot be null or empty\"}", response.getBody());
    }
}