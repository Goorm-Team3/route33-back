package io.goorm.route33.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class GsonConfig implements WebMvcConfigurer {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting().create();
    }

    @Bean
    public HttpMessageConverter<Object> gsonHttpMessageConverter(Gson gson) {
        return new GsonHttpMessageConverter(gson);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(gsonHttpMessageConverter(gson()));
    }
}
