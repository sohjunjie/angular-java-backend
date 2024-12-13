package org.example.config;



import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.example.exception.ApiExceptionResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.example.utils.Constants.API_MDC_CORRELATION_ID_KEY;

@Configuration
public class SwaggerConfig {

    public static final Map<String, String> API_HEADERS = Map.of(
            API_MDC_CORRELATION_ID_KEY, UUID.randomUUID().toString()
    );

    public static final String API_NAME = "ANGULAR BACKEND API";

    public static final String API_VERSION = "v1";

    @Bean("groupedOpenApi")
    GroupedOpenApi groupedOpenApi(OpenApiCustomizer customizer) {
        return GroupedOpenApi.builder()
                .displayName(API_VERSION)
                .group(API_VERSION)
                .pathsToMatch("/**")
                .addOpenApiCustomizer(customizer)
                .build();
    }

    @Bean("openApiCustomizer")
    OpenApiCustomizer openApiCustomizer() {
        return openApi -> {

            Info info = openApi.getInfo();
            info.setTitle(API_NAME);
            info.setVersion(API_VERSION);

            SecurityScheme securityScheme = new SecurityScheme()
                    .name(HttpHeaders.AUTHORIZATION)
                    .type(SecurityScheme.Type.HTTP)
                    .in(SecurityScheme.In.HEADER)
                    .scheme("bearer")
                    .bearerFormat("JWT");

            openApi.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
            openApi.getComponents().addSecuritySchemes(HttpHeaders.AUTHORIZATION, securityScheme);

            Schema<?> errorResponseSchema = ModelConverters.getInstance().readAllAsResolvedSchema(ApiExceptionResponse.class).schema;
            openApi.getComponents().getSchemas().put(ApiExceptionResponse.class.getSimpleName(), errorResponseSchema);

            openApi.getPaths().forEach((path, pathItem) ->
                    pathItem.readOperationsMap().forEach((method, op) -> {
                        getApiHeaders().forEach(op::addParametersItem);
                        op.getResponses().addApiResponse(
                                "Error",
                                getApiErrorResponse(path, method.name(), errorResponseSchema)
                        );
                    })
            );

        };
    }

    private List<Parameter> getApiHeaders() {
        return API_HEADERS.entrySet().stream().map(e ->
                new Parameter()
                        .name(e.getKey())
                        .in(ParameterIn.HEADER.toString())
                        .required(false)
                        .schema(new StringSchema())
                        .example(e.getValue())
        ).toList();
    }

    private ApiResponse getApiErrorResponse(String endpoint, String method, Schema<?> schema) {
        ApiExceptionResponse errorResponse = new ApiExceptionResponse();
        errorResponse.setCorrelationId(UUID.randomUUID().toString());
        errorResponse.setEndpoint(endpoint);
        errorResponse.setMethod(method);
        errorResponse.setTimestamp(
                ZonedDateTime.now(ZoneId.of("UTC"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
        );
        errorResponse.setErrorDescription("sample error description");

        MediaType mediaType = new MediaType();
        mediaType.schema(schema);
        mediaType.example(errorResponse);

        Content content = new Content();
        content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);

        return new ApiResponse().content(content);
    }

}
