package com.example.fleamarket.api;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

public abstract class AbstractKeycloakContainerTest {
    @ServiceConnection
    protected static final KeycloakContainer keycloak;
    static {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.4");

        keycloak
            .withBootstrapAdminDisabled()
            .withRealmImportFiles(
            "/keycloak/master-realm.json",
            "/keycloak/master-users-0.json"
        );
        keycloak.start();
    }
}
