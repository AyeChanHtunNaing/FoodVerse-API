package dev.peacechan.foodverse.common.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

@Schema(description = "Standard error response returned by the API.")
public record ApiErrorResponse(
        @Schema(description = "Time when the error occurred.", example = "2026-07-02T09:45:00Z")
        Instant timestamp,
        @Schema(description = "HTTP status code.", example = "400")
        int status,
        @Schema(description = "HTTP status reason phrase.", example = "Bad Request")
        String error,
        @Schema(description = "Human-readable error message.", example = "Validation failed")
        String message,
        @Schema(description = "Request path that caused the error.", example = "/api/restaurants")
        String path,
        @Schema(description = "Field validation errors keyed by field name.")
        Map<String, String> validationErrors
) {
}
