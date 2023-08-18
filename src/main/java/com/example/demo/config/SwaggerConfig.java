package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
//@EnableSwagge
public class SwaggerConfig {

//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select() // API 문서에 포함할 API들을 선택하기 위한 설정 시작
////                .apis(RequestHandlerSelectors.any()) // 모든 컨트롤러를 문서에 포함
//                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
//                .paths(PathSelectors.any()) // 모든 엔드포인트를 문서에 포함
//                .build();
//    }

    @Bean
    public Docket login() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .groupName("로그인")
                .apiInfo(this.apiInfo())
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
//                .paths(PathSelectors.ant("/login/**"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Toy-Project")
                .description("Account, Login, Board")
                .version("1.0.0")
                .contact(new Contact("권오수","localhost:8080", "kwon524@bns.co.kr"))
                .build();
    }
}

