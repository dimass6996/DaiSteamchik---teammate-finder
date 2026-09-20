package ru.daisteamchik.command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthorCommandTest {
    private AuthorCommand command;
    @BeforeEach void setUp() { command = new AuthorCommand(); }
    @Test void getName() { assertEquals("/author", command.getName()); }
    @Test void getDescription() { assertNotNull(command.getDescription()); }
    @Test void execute() { assertTrue(command.execute(new String[]{}).contains("Авторы")); }
}