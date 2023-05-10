package com.akul.dsp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Server local = new Server();
        local.setUrl("http://localhost:8080");
        local.setDescription("Server in Local environment");

        License mit = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title(pom().getDescription())
                .version(pom().getVersion())
                .description(String.format("API - %s", pom().getDescription()))
                .license(mit);

        return new OpenAPI().info(info).servers(List.of(local));
    }

    private Model pom() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            return reader.read(new FileReader("pom.xml"));
        } catch (IOException | XmlPullParserException ex) {
            return new Model();
        }
    }
}
