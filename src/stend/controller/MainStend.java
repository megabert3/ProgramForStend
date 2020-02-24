package stend.controller;


public class MainStend {

    public static void main(String[] args) {
        MainStend mn = new MainStend();
        System.out.println(mn.getTime(3661000));
    }

    private String getTime(long s){
        s = s /1000;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
