package com.osrsGoalTracker.character.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.osrsGoalTracker.character.model.Character;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class CharacterRepositoryImplTest {
    private DynamoDbClient dynamoDbClient;
    private CharacterRepositoryImpl characterRepository;
    private static final String USER_ID = "testUser";
    private static final String CHARACTER_NAME = "testCharacter";

    @BeforeEach
    void setUp() {
        dynamoDbClient = mock(DynamoDbClient.class);
        characterRepository = new CharacterRepositoryImpl(dynamoDbClient);
    }

    @Test
    void addCharacterToUser_Success() {
        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(null);

        Character result = characterRepository.addCharacterToUser(USER_ID, CHARACTER_NAME);

        assertEquals(USER_ID, result.getUserId());
        assertEquals(CHARACTER_NAME, result.getName());
        verify(dynamoDbClient).putItem(any(PutItemRequest.class));
    }

    @Test
    void addCharacterToUser_NullUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> characterRepository.addCharacterToUser(null, CHARACTER_NAME));
    }

    @Test
    void addCharacterToUser_EmptyUserId() {
        assertThrows(IllegalArgumentException.class, () -> characterRepository.addCharacterToUser("", CHARACTER_NAME));
    }

    @Test
    void addCharacterToUser_NullCharacterName() {
        assertThrows(IllegalArgumentException.class, () -> characterRepository.addCharacterToUser(USER_ID, null));
    }

    @Test
    void addCharacterToUser_EmptyCharacterName() {
        assertThrows(IllegalArgumentException.class, () -> characterRepository.addCharacterToUser(USER_ID, ""));
    }

    @Test
    void getCharactersForUser_Success() {
        Instant now = Instant.now();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", AttributeValue.builder().s(USER_ID).build());
        item.put("characterName", AttributeValue.builder().s(CHARACTER_NAME).build());
        item.put("createdAt", AttributeValue.builder().s(now.toString()).build());
        item.put("updatedAt", AttributeValue.builder().s(now.toString()).build());

        QueryResponse queryResponse = QueryResponse.builder()
                .items(Arrays.asList(item))
                .build();

        when(dynamoDbClient.query(any(QueryRequest.class))).thenReturn(queryResponse);

        List<Character> result = characterRepository.getCharactersForUser(USER_ID);

        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(CHARACTER_NAME, result.get(0).getName());
        verify(dynamoDbClient).query(any(QueryRequest.class));
    }

    @Test
    void getCharactersForUser_NullUserId() {
        assertThrows(IllegalArgumentException.class, () -> characterRepository.getCharactersForUser(null));
    }

    @Test
    void getCharactersForUser_EmptyUserId() {
        assertThrows(IllegalArgumentException.class, () -> characterRepository.getCharactersForUser(""));
    }
}