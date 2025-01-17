package com.osrsGoalTracker.character.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.osrsGoalTracker.character.di.CharacterModule;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.service.CharacterService;
import com.osrsGoalTracker.utils.JsonUtils;

import lombok.extern.log4j.Log4j2;

/**
 * Lambda handler for retrieving all characters associated with a user.
 */
@Log4j2
public class GetCharactersForUserHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;

    private final CharacterService characterService;

    /**
     * Default constructor for AWS Lambda.
     * This constructor is required by AWS Lambda to instantiate the handler.
     */
    public GetCharactersForUserHandler() {
        Injector injector = Guice.createInjector(new CharacterModule());
        this.characterService = injector.getInstance(CharacterService.class);
    }

    /**
     * Constructor for testing purposes.
     *
     * @param characterService The CharacterService instance to use for retrieving
     *                         characters
     */
    @Inject
    public GetCharactersForUserHandler(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("HOWDY Received request to get characters for user");
        try {
            String userId = parseAndValidateInput(input);
            List<Character> characters = executeRequest(userId);
            return createSuccessResponse(characters);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage());
        }
    }

    private String parseAndValidateInput(APIGatewayProxyRequestEvent input) {
        if (input == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Map<String, String> pathParameters = input.getPathParameters();
        if (pathParameters == null) {
            throw new IllegalArgumentException("Path parameters cannot be null");
        }

        String userId = pathParameters.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return userId.trim();
    }

    private List<Character> executeRequest(String userId) {
        return characterService.getCharactersForUser(userId);
    }

    /**
     * Creates a success response with the list of characters.
     *
     * @param characters The list of character entities
     * @return The API Gateway response event
     */
    private APIGatewayProxyResponseEvent createSuccessResponse(List<Character> characters) {
        List<String> characterNames = characters.stream()
                .map(Character::getName)
                .collect(Collectors.toList());

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HTTP_OK)
                .withBody(JsonUtils.toJson(characterNames));
    }

    /**
     * Creates an error response with the given message.
     *
     * @param message The error message
     * @return The API Gateway response event
     */
    private APIGatewayProxyResponseEvent createErrorResponse(String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HTTP_BAD_REQUEST)
                .withBody(String.format("{\"message\":\"%s\"}", message));
    }
}