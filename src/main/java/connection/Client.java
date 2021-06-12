package connection;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class Client {
    DatagramSocket socket;
    SocketAddress socketAddress;
    private static User user = new User();

    public Client(DatagramSocket socket, SocketAddress socketAddress) {
        this.socket = socket;
        this.socketAddress = socketAddress;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Client.user = user;
    }

    public void send(ExchangeClass exchangeClass) {
        byte[] sending;
        try {
            exchangeClass.setUser(user);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(exchangeClass);
            out.flush();
            sending = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(sending, sending.length, socketAddress);
            socket.send(packet);
            //System.out.println(packet);
            //System.out.println("exchangeClass = " + exchangeClass);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        byte[] message = new byte[65535]; //Массив байт, который мы получаем
        try {
            DatagramPacket packet = new DatagramPacket(message, message.length);
            socket.setSoTimeout(10000); //Задержка для возможности обработки запроса клиента
            socket.receive(packet);
            ExchangeClass exchangeClass = deserialize(message);
            System.out.println(exchangeClass.getAnswer());
        } catch (SocketTimeoutException e) {
            System.out.println("Время ожидания превышено");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка");
        }
    }

    public ExchangeClass deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return (ExchangeClass) o.readObject();
            }
        }
    }
}
