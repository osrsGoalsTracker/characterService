package com.osrsGoalTracker.character.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.repository.impl.CharacterRepositoryImpl;
import com.osrsGoalTracker.character.service.CharacterService;
import com.osrsGoalTracker.character.service.impl.CharacterServiceImpl;
import com.osrsGoalTracker.character.dao.CharacterDao;
import com.osrsGoalTracker.character.dao.impl.DynamoCharacterDao;
import com.osrsGoalTracker.shared.di.SharedDynamoDbModule;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Guice module for character-related bindings.
 */
public class CharacterModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new SharedDynamoDbModule());
        bind(CharacterRepository.class).to(CharacterRepositoryImpl.class);
        bind(CharacterService.class).to(CharacterServiceImpl.class);
    }

    @Provides
    @Singleton
    CharacterDao provideCharacterDao(DynamoDbClient dynamoDbClient) {
        return new DynamoCharacterDao(dynamoDbClient, System.getenv("CHARACTER_TABLE_NAME"));
    }
}