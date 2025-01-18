package com.osrsGoalTracker.character.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.repository.impl.CharacterRepositoryImpl;
import com.osrsGoalTracker.character.service.CharacterService;
import com.osrsGoalTracker.character.service.impl.CharacterServiceImpl;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


/**
 * Guice module for character-related bindings.
 */
public class CharacterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CharacterRepository.class).to(CharacterRepositoryImpl.class);
        bind(CharacterService.class).to(CharacterServiceImpl.class);
    }

    @Provides
    @Singleton
    DynamoDbClient provideDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .build();
    }
}