package com.danyatheworst.config;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // Root application context configuration, if needed
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // Configuration for the DispatcherServlet context
        return new Class<?>[] {SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        // Map DispatcherServlet to the root URL
        return new String[] {"/"};
    }
}