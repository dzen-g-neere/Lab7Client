package commands;

import connection.Client;
import connection.ExchangeClass;
import connection.User;
import exceptions.WrongArgumentException;
import utility.LabWorkAsker;

public class Register extends AbstractCommand implements Command{
    private Client client;
    private LabWorkAsker labWorkAsker;
    public Register(Client client, LabWorkAsker labWorkAsker) {
        super("login", " - авторизоваться в системе");
        this.client = client;
        this.labWorkAsker = labWorkAsker;
    }
    /**
     * Execute of 'login' command.
     */
    @Override
    public void execute(String argument) {
        try {
            User user = new User();
            user.setLogin(labWorkAsker.askLogin());
            user.setPassword(labWorkAsker.askPassword());
            Client.setUser(user);
            ExchangeClass exchangeClass = new ExchangeClass("register", argument, null);
            client.send(exchangeClass);
            user.setLogin("");
            user.setPassword("");
            Client.setUser(user);
        } catch (Exception e) {
            System.out.println("Ошибка. Повторите ввод.");
        }
    }
}

