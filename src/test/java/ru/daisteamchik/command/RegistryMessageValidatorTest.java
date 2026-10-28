package ru.daisteamchik.registration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistryMessageValidatorTest {

    private final RegistryMessageValidator validator = new RegistryMessageValidator();

    @Test
    void validateName() {
        assertNull(validator.getNameError("dimass6996"));
        assertNotNull(validator.getNameError("A"));
    }

    @Test
    void validateSteamUrl() {
        assertNull(validator.getUrlError("https://steamcommunity.com/id/dimass6996/"));
        assertNotNull(validator.getUrlError("vk.com/id123"));
    }

    @Test
    void validatePrimeTime() {
        assertNull(validator.getPrimeTimeError("18:00 - 22:00"));
        assertNotNull(validator.getPrimeTimeError("25:00 - 12:00"));
    }

    @Test
    void validateRolesAndGames() {
        assertNull(validator.getRolesError("керри, мидер"));
        assertNotNull(validator.getRolesError("димас"));

        assertNull(validator.getGamesError("dota 2, cs 2"));
        assertNotNull(validator.getGamesError("warcraft"));
    }
}