package com.saigou.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "${custom.info.title}",
                version = "${custom.info.version}",
                description = "${custom.info.description}",
                contact = @Contact(name = "${custom.info.contact}"),
                license = @License(name = "${custom.license.name}", url = "${custom.license.terms-of-service}")
        ),
        security = @SecurityRequirement(name = "JWT"),
        externalDocs = @ExternalDocumentation(url = "https://github.com/saiGou-14H",description = "github"

        ),
        servers = @Server(url = "${custom.info.gateway-url}")
)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class Swagger3Config {
//        @Bean
//        public GroupedOpenApi PayApi()
//        {
//                return GroupedOpenApi.builder().group("用户服务模块").pathsToMatch("/user/**").build();
//        }
}
