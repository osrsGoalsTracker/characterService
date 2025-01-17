package com.osrsGoalTracker.character.di;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.osrsGoalTracker.character.repository.CharacterRepository;
import com.osrsGoalTracker.character.repository.impl.CharacterRepositoryImpl;
import com.osrsGoalTracker.character.service.CharacterService;
import com.osrsGoalTracker.character.service.impl.CharacterServiceImpl;
import com.osrsGoalTracker.character.dao.CharacterDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterModuleTest {

    private CharacterDao mockCharacterDao;
    private Injector injector;

    @BeforeEach
    void setUp() {
        mockCharacterDao = mock(CharacterDao.class);

        AbstractModule testModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(CharacterDao.class).toInstance(mockCharacterDao);
            }
        };

        injector = Guice.createInjector(
                Modules.override(new CharacterModule())
                        .with(testModule));
    }

    @Test
    void testCharacterModuleBindings() {
        // When
        CharacterService characterService = injector.getInstance(CharacterService.class);
        CharacterRepository characterRepository = injector.getInstance(CharacterRepository.class);

        // Then
        assertNotNull(characterService, "CharacterService should be bound");
        assertNotNull(characterRepository, "CharacterRepository should be bound");
        assertTrue(characterService instanceof CharacterServiceImpl,
                "CharacterService should be bound to CharacterServiceImpl");
        assertTrue(characterRepository instanceof CharacterRepositoryImpl,
                "CharacterRepository should be bound to CharacterRepositoryImpl");
    }
}