package ru.java.behavioral.memento;

import java.time.LocalDateTime;

public class Demo {
    public static void main(String[] args) {
        // создаём Originator
        var originator = new Originator(LocalDateTime::now);

        // создаём объект и состояние с ним
        var abc = new State(new String[]{"A", "B", "C"});
        System.out.println(abc);

        // сохраняем состояние в Originator
        originator.saveState(abc);
        // меняем объект
        abc.getArray()[0] = "A1";
        System.out.println(abc);

        // снова сохраняем состояние
        originator.saveState(abc);
        // снова меняем объект
        abc.getArray()[0] = "A2";
        System.out.println(abc);

        originator.saveState(abc);
        abc.getArray()[0] = "A3";
        System.out.println(abc);

        System.out.println("  undo changes");

        // восстанавливаем состояние
        abc = originator.restoreState();
        System.out.println(abc);

        // еще раз восстанавливаем состояние
        abc = originator.restoreState();
        System.out.println(abc);

        abc = originator.restoreState();
        System.out.println(abc);
    }
}
