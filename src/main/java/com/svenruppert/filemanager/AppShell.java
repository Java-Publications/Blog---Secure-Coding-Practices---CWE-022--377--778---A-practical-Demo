package com.svenruppert.filemanager;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(
    name = "Securing Coding Practices for Vaadin Flow",
    shortName = "Hardening Vaadin Flow")
@Theme("my-theme")
public class AppShell implements AppShellConfigurator {

}