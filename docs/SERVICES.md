# Service Layer

## Overview

The service layer contains the core business logic of the application. Services follow these principles:

1. Interface-based design
2. Constructor injection for dependencies
3. Clear separation of concerns
4. Transaction management
5. Comprehensive error handling

## Service Interfaces

### Character Service

```java
public interface CharacterService {
    /**
     * Associates a character with a user.
     *
     * @param userId The user's ID
     * @param request The character association request
     * @return The created association
     * @throws ResourceNotFoundException if user doesn't exist
     * @throws ConflictException if character already associated
     */
    Character addCharacterToUser(String userId, AddCharacterToUserRequest request);

    /**
     * Gets all characters for a user.
     *
     * @param userId The user's ID
     * @return List of characters
     * @throws ResourceNotFoundException if user doesn't exist
     */
    List<Character> getCharactersForUser(String userId);
}
```

## Implementation Pattern

Services follow this implementation pattern:

```java
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;
    private final ValidationService validationService;

    @Inject
    public CharacterServiceImpl(
            CharacterRepository characterRepository,
            ValidationService validationService) {
        this.characterRepository = characterRepository;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public Character addCharacterToUser(String userId, AddCharacterToUserRequest request) {
        // 1. Validate
        validationService.validate(request);

        // 2. Check if character already exists
        if (characterRepository.existsByUserIdAndName(userId, request.getCharacterName())) {
            throw new ConflictException("Character already associated");
        }

        // 3. Create character
        Character character = Character.builder()
            .userId(userId)
            .name(request.getCharacterName())
            .lastUpdated(LocalDateTime.now())
            .build();

        // 4. Save and return
        return characterRepository.save(character);
    }
}
```

## Error Handling

Services use custom exceptions for different error cases:

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Validation

Services use a common validation service:

```java
@Singleton
public class ValidationService {
    private final Validator validator;

    @Inject
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new BadRequestException(formatViolations(violations));
        }
    }
}
```

## Testing

Services must have comprehensive unit tests:

```java
@ExtendWith(MockitoExtension.class)
class CharacterServiceImplTest {
    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private CharacterServiceImpl characterService;

    @Test
    void addCharacterToUser_ValidRequest_Success() {
        // Given
        String userId = "user123";
        AddCharacterToUserRequest request = new AddCharacterToUserRequest();
        request.setCharacterName("testCharacter");

        // When
        Character result = characterService.addCharacterToUser(userId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getCharacterName());
        verify(characterRepository).save(any(Character.class));
    }

    @Test
    void addCharacterToUser_AlreadyExists_ThrowsConflict() {
        // Test duplicate character handling
    }

    @Test
    void addCharacterToUser_InvalidRequest_ThrowsBadRequest() {
        // Test invalid input handling
    }
} 