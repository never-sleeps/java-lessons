package ru.java.services;

import ru.java.model.Player;

public class PlayerServiceImpl implements PlayerService {

    private final IOService ioService;

    public PlayerServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Player getPlayer() {
        ioService.out("Давай познакомимся");
        String playerName = ioService.readLn("Введите имя: ");
        return new Player(playerName);
    }
}
