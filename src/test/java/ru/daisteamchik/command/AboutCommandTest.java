package ru.daisteamchik.command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AboutCommandTest {
    private AboutCommand command;
    @BeforeEach void setUp() { command = new AboutCommand(); }
    @Test void getName() { assertEquals("/about", command.getName()); }
    @Test void getDescription() { assertNotNull(command.getDescription()); }
    @Test void execute() { assertTrue(command.execute(new String[]{}).contains("DaiSteamchik")); }
}