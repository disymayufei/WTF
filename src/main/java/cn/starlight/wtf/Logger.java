package cn.starlight.wtf;

public final class Logger {
    private static final String name = "WTF";

    public static void info(String msg){
        System.out.printf("[%s - INFO] %s%n", name , msg);
    }

    public static void warn(String msg){
        System.out.printf("[%s - WARN] %s%n", name , msg);
    }

    public static void error(String msg){
        System.out.printf("[%s - ERR] %s%n", name , msg);
    }

}
