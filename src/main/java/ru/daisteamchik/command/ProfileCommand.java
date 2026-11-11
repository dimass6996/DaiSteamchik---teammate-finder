package ru.daisteamchik.command;

// ИСПРАВЛЕНИЕ 1: Добавлен правильный импорт для InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.daisteamchik.model.User;
import ru.daisteamchik.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ProfileCommand implements Command {

    private final UserRepository userRep = new UserRepository();

    @Override
    public String getName() {
        return "/profile";
    }

    @Override
    public String getDescription() {
        return "Посмотреть свою сохраненную анкету";
    }

    @Override
    public CommandResponse execute(long userId, String[] args) {
        User user = userRep.getUser(userId);

        if (user == null) {
            return new CommandResponse("У тебя еще нет анкеты. Напиши /reg, чтобы зарегистрироваться.");
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

        InlineKeyboardButton btnEdit = new InlineKeyboardButton();
        btnEdit.setText("✏️ Изменить");
        btnEdit.setCallbackData("EDIT_PROFILE"); // Это "скрытый" текст, который бот получит при нажатии

        InlineKeyboardButton btnDelete = new InlineKeyboardButton();
        btnDelete.setText("❌ Удалить");
        btnDelete.setCallbackData("DELETE_PROFILE");

        rowInline1.add(btnEdit);
        rowInline1.add(btnDelete);

        rowsInline.add(rowInline1);
        markupInline.setKeyboard(rowsInline);

        return new CommandResponse(user.toProfileCard(), markupInline);
    }
}