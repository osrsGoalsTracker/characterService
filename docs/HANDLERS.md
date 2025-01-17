# Lambda Handlers

## Overview

The service exposes its functionality through AWS Lambda handlers. Each handler strictly follows a 4-step pattern:

1. Parse input from API Gateway event
2. Validate input
3. Run service function
4. Return formatted response

## Implementation Pattern

All handlers MUST follow this implementation pattern:

```java
public class SomeHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Inject
    private SomeService service;
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            // 1. Parse input
            SomeRequest request = parseInput(input);
            
            // 2. Validate input
            validateRequest(request);
            
            // 3. Run service function
            SomeResponse response = service.doSomething(request);
            
            // 4. Return formatted response
            return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(response)
                .build();
                
        } catch (Exception e) {
            return handleError(e);
        }
    }
}
```

## Available Handlers

### Character Management

#### AddCharacterToUserHandler
- **Path**: `POST /users/{userId}/characters`
- **Package**: `com.osrsGoalTracker.character.handler.AddCharacterToUserHandler`
- **Request**: `AddCharacterToUserRequest`
- **Response**: `APIGatewayProxyResponseEvent` with association details

#### GetCharactersForUserHandler
- **Path**: `GET /users/{userId}/characters`
- **Package**: `com.osrsGoalTracker.character.handler.GetCharactersForUserHandler`
- **Request**: Path parameter `userId`
- **Response**: `APIGatewayProxyResponseEvent` with list of characters

## Error Handling

All handlers use a standardized error handling approach:

```java
private APIGatewayProxyResponseEvent handleError(Exception e) {
    if (e instanceof ResourceNotFoundException) {
        return ApiGatewayResponse.builder()
            .setStatusCode(404)
            .setObjectBody(new ErrorResponse("NOT_FOUND", e.getMessage()))
            .build();
    }
    
    if (e instanceof BadRequestException) {
        return ApiGatewayResponse.builder()
            .setStatusCode(400)
            .setObjectBody(new ErrorResponse("BAD_REQUEST", e.getMessage()))
            .build();
    }
    
    // Default to internal server error
    return ApiGatewayResponse.builder()
        .setStatusCode(500)
        .setObjectBody(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"))
        .build();
}
```

## Response Format

### Success Response
```json
{
    "statusCode": 200,
    "body": {
        // Response data
    },
    "headers": {
        "Content-Type": "application/json"
    }
}
```

### Error Response
```json
{
    "statusCode": 400,
    "body": {
        "error": {
            "code": "BAD_REQUEST",
            "message": "Invalid input provided",
            "details": {
                // Error details
            }
        }
    },
    "headers": {
        "Content-Type": "application/json"
    }
}
```

## Testing

Each handler must have comprehensive tests covering:

1. Input parsing
2. Input validation
3. Service interaction
4. Response formatting
5. Error handling

Example test structure:

```java
@ExtendWith(MockitoExtension.class)
class AddCharacterToUserHandlerTest {
    @Mock
    private CharacterService characterService;
    
    @InjectMocks
    private AddCharacterToUserHandler handler;
    
    @Test
    void handleRequest_ValidInput_Success() {
        // Given
        APIGatewayProxyRequestEvent input = createValidInput();
        when(characterService.addCharacterToUser(any())).thenReturn(createCharacter());
        
        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(input, null);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(200);
        // Additional assertions
    }
    
    @Test
    void handleRequest_InvalidInput_BadRequest() {
        // Test invalid input handling
    }
    
    @Test
    void handleRequest_ServiceError_InternalError() {
        // Test service error handling
    }
} 