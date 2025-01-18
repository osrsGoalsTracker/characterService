package com.osrsGoalTracker.character.repository.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.google.inject.Inject;
import com.osrsGoalTracker.character.model.Character;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.repository.entity.SortKeyUtil;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

/**
 * Default implementation of the CharacterRepository interface.
 */
@Log4j2
public class CharacterRepositoryImpl implements CharacterRepository {
    private static final String PK = "pk";
    private static final String SK = "sk";
    private static final String USER_PREFIX = "USER#";
    private static final String USER_ID = "userId";
    private static final String CHARACTER_NAME = "characterName";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";
    private static final String TABLE_NAME = System.getenv("CHARACTER_TABLE_NAME");

    private final DynamoDbClient dynamoDbClient;

    /**
     * Constructs a new DefaultCharacterRepository.
     *
     * @param dynamoDbClient The DynamoDbClient instance to use for data operations
     */
    @Inject
    public CharacterRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private void validateAddCharacterToUserInput(String userId, String characterName) {
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Attempted to add character with null or empty user ID");
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
        if (characterName == null || characterName.trim().isEmpty()) {
            log.warn("Attempted to add character with null or empty name");
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }
    }

    private Map<String, AttributeValue> createNewCharacterItem(String userId, String characterName, Instant timestamp) {
        Map<String, AttributeValue> item = new LinkedHashMap<>();
        item.put(PK, AttributeValue.builder().s(USER_PREFIX + userId).build());
        item.put(SK, AttributeValue.builder().s(SortKeyUtil.getCharacterMetadataSortKey(characterName)).build());
        item.put(CHARACTER_NAME, AttributeValue.builder().s(characterName).build());
        item.put(USER_ID, AttributeValue.builder().s(userId).build());
        item.put(CREATED_AT, AttributeValue.builder().s(timestamp.toString()).build());
        item.put(UPDATED_AT, AttributeValue.builder().s(timestamp.toString()).build());
        return item;
    }

    @Override
    public Character addCharacterToUser(String userId, String characterName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (characterName == null || characterName.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        String trimmedCharacterName = characterName.trim();

        log.info("Adding character {} to user {}", trimmedCharacterName, trimmedUserId);
       log.debug("Attempting to add character {} to user {}", characterName, userId);

        validateAddCharacterToUserInput(userId, characterName);

        Instant now = Instant.now();

        Map<String, AttributeValue> item = createNewCharacterItem(userId, characterName, now);

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        log.debug("Putting new character item in DynamoDB for user {} with name {}", userId, characterName);
        dynamoDbClient.putItem(putItemRequest);
        log.info("Successfully added character {} to user {}", characterName, userId);

        return Character.builder()
                .name(characterName)
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Override
    public List<Character> getCharactersForUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.error("User ID cannot be null or empty");
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        log.info("Getting players for user {} from DAO", trimmedUserId);
                log.debug("Getting characters for user {}", userId);

        if (userId == null || userId.trim().isEmpty()) {
            log.warn("Attempted to get characters with null or empty user ID");
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", AttributeValue.builder().s(USER_PREFIX + userId).build());
        expressionAttributeValues.put(":sk_prefix",
                AttributeValue.builder().s(SortKeyUtil.CHARACTER_METADATA_PREFIX).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("pk = :pk AND begins_with(sk, :sk_prefix)")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        log.debug("Querying DynamoDB for characters with user ID: {}", userId);
        QueryResponse response = dynamoDbClient.query(queryRequest);

        return response.items().stream()
                .map(item -> Character.builder()
                        .userId(item.get(USER_ID).s())
                        .name(item.get(CHARACTER_NAME).s())
                        .createdAt(Instant.parse(item.get(CREATED_AT).s()))
                        .updatedAt(Instant.parse(item.get(UPDATED_AT).s()))
                        .build())
                .collect(Collectors.toList());
    }
}
