// package com.example.quanlisanbay.config;

// import org.springframework.context.MessageSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.support.ReloadableResourceBundleMessageSource;
// import org.springframework.web.servlet.LocaleResolver;
// import org.springframework.web.servlet.i18n.CookieLocaleResolver;
// import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

// import java.util.Locale;

// @Configuration
// public class InternationalizationConfig {

//     @Bean
//     public MessageSource messageSource() {
//         ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//         messageSource.setBasename("classpath:lang/messages");
//         messageSource.setDefaultEncoding("UTF-8");
//         return messageSource;
//     }

//     @Bean
//     public LocaleResolver localeResolver() {
//         CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
//         cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//         return cookieLocaleResolver;
//     }

//     @Bean
//     public LocaleChangeInterceptor localeChangeInterceptor() {
//         LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//         localeChangeInterceptor.setParamName("lang");
//         return localeChangeInterceptor;
//     }
// }
package com.example.quanlisanbay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("vi"));  // Ngôn ngữ mặc định là tiếng Việt
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");  // Tham số URL để thay đổi ngôn ngữ
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
