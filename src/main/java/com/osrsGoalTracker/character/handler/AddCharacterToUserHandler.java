package com.osrsGoalTracker.character.handler;

import java.util.Map;

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
 * Lambda handler for adding a RuneScape player to a user's account.
 */
@Log4j2
public class AddCharacterToUserHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;

    private final CharacterService characterService;

    /**
     * Default constructor for AWS Lambda.
     * This constructor is required by AWS Lambda to instantiate the handler.
     */
    @Inject
    public AddCharacterToUserHandler() {
        Injector injector = Guice.createInjector(new CharacterModule());
        this.characterService = injector.getInstance(CharacterService.class);
    }


    /**
     * Constructor for AddPlayerToUserHandler.
     * 
     * @param characterService The character service to use for adding a character
     *                         to a user.
     */
    @Inject
    public AddCharacterToUserHandler(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("HOWDY Received request to add player to user");
        try {
            UserCharacterPair userCharacterPair = parseAndValidateInput(input);
            Character character = executeRequest(userCharacterPair);
            return createSuccessResponse(character);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage());
        } catch (Exception e) {
            log.error("Error processing request", e);
            return buildErrorResponse("Error processing request: " + e.getMessage());
        }
    }

    private record UserCharacterPair(String userId, String characterName) {
    }

    private UserCharacterPair parseAndValidateInput(APIGatewayProxyRequestEvent input) {
        if (input == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Map<String, String> pathParameters = input.getPathParameters();
        if (pathParameters == null) {
            throw new IllegalArgumentException("Path parameters cannot be null");
        }

        String userId = pathParameters.get("userId");
        String characterName = pathParameters.get("name");

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (characterName == null || characterName.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }

        return new UserCharacterPair(userId.trim(), characterName.trim());
    }

    private Character executeRequest(UserCharacterPair userCharacterPair) {
        log.info("Adding character {} to user {}", userCharacterPair.characterName(), userCharacterPair.userId());
        return characterService.addCharacterToUser(userCharacterPair.userId(), userCharacterPair.characterName());
    }

    private APIGatewayProxyResponseEvent createSuccessResponse(Character character) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HTTP_OK)
                .withBody(JsonUtils.toJson(character));
    }

    private APIGatewayProxyResponseEvent buildErrorResponse(String message) {
        log.error(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HTTP_BAD_REQUEST)
                .withBody(String.format("{\"message\":\"%s\"}", message));
    }
}