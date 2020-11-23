package ch.heigvd.amt.gamification.api.util;

import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(1)
public class AuthorizationFilter implements Filter {

    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        String apiKey = req.getHeader("X-API-KEY");
        if (apiKey == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No api key in header");
            return;
        }
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(apiKey);
        if (applicationEntity == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token in header");
            return;
        }
        req.setAttribute("ApplicationEntity", applicationEntity);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> filterRegistrationBean(){
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(this);
        registrationBean.addUrlPatterns("/badges/*");
        registrationBean.addUrlPatterns("/events/*");
        registrationBean.addUrlPatterns("/rules/*");
        return registrationBean;
    }
}
