package ch.heigvd.amt.gamification.configuration;

import com.fasterxml.jackson.core.type.ResolvedType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.HttpAuthenticationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;


@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Gamification API")
            .description("This is the API that will make your site have a gamification service")
            .version("0.2.0")
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.OAS_30)
            .select()
                .apis(RequestHandlerSelectors.basePackage("ch.heigvd.amt.gamification.api"))
                .build()
            .directModelSubstitute(Void.class, Void.class)
            .securitySchemes(singletonList(securityScheme()))
            .securityContexts(singletonList(securityContext()))
            .apiInfo(apiInfo());
    }

    private SecurityScheme securityScheme() {
        /*
        return new HttpAuthenticationBuilder()
          .name("BearerAuthorization")
          .scheme("bearer")
          .build();
         */
        return new ApiKey("X-API-KEY", "api_key", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
          .securityReferences(securityReferences())
          .build();
    }

    private List<SecurityReference> securityReferences() {
        return singletonList(
          new SecurityReference("X-API-KEY", new AuthorizationScope[] {}));
          // new SecurityReference("BearerAuthorization", new AuthorizationScope[] {}));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
