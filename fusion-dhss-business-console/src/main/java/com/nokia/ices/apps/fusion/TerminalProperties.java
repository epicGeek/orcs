package com.nokia.ices.apps.fusion;


import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.terminal")
public class TerminalProperties implements BeanClassLoaderAware {

    private static String logBasePath;
    private static String proxyHost;
    private static String proxyUserName;
    private static String proxyPassword;
    private static String termType;



    private ClassLoader classLoader;

    @PostConstruct
    public void init() {
        
        File CONSOLE_LOG_DIR = new File(logBasePath);
        if (!CONSOLE_LOG_DIR.exists()) {
            CONSOLE_LOG_DIR.mkdirs();
        }
    }



    public static String getLogBasePath() {
        return logBasePath;
    }


    public static void setLogBasePath(String logBasePath) {
        TerminalProperties.logBasePath = logBasePath;
    }


    public static String getProxyHost() {
        return proxyHost;
    }


    public static void setProxyHost(String proxyHost) {
        TerminalProperties.proxyHost = proxyHost;
    }


    public static String getProxyUserName() {
        return proxyUserName;
    }


    public static void setProxyUserName(String proxyUserName) {
        TerminalProperties.proxyUserName = proxyUserName;
    }


    public static String getProxyPassword() {
        return proxyPassword;
    }


    public static void setProxyPassword(String proxyPassword) {
        TerminalProperties.proxyPassword = proxyPassword;
    }

    public static String getTermType() {
        return termType;
    }



    public static void setTermType(String termType) {
        TerminalProperties.termType = termType;
    }



    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }


}
