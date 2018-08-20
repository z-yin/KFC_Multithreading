package kfc;

import kfc.Threads.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    public static void main (String args[]) throws Exception {

        ExecutorService orderThread = Executors.newCachedThreadPool();
        ExecutorService deliveryThreads = Executors.newFixedThreadPool(4);
        ExecutorService fontDeskThreads = Executors.newFixedThreadPool(4);
        ExecutorService cateringThreads = Executors.newFixedThreadPool(2);

        BlockingQueue<Order> orderQueue = new LinkedBlockingDeque<>();

        orderThread.submit(new OrderThread());

        String[] foodTypes = {"Burger", "Side", "Beverage", "Dessert"};

        for (String foodType : foodTypes) {
            deliveryThreads.submit(new DeliveryThread(foodType));
        }

        for (int i = 1; i < 5; i++) {
            fontDeskThreads.submit(new FontDeskThread(i, orderQueue));
        }

        for (int i = 1; i < 3; i++) {
            cateringThreads.submit(new CateringThread(i, orderQueue));
        }

        new Scanner(System.in).nextLine();
        ThreadBase threadBase = new ThreadBase();
        ThreadBase.setRunning(false);

        orderThread.shutdown();
        fontDeskThreads.shutdown();
        deliveryThreads.shutdown();
        cateringThreads.shutdown();
    }
}
