package stend.controller;


public class MainStend {

    public static void main(String[] args) {
        MainStend mn = new MainStend();

        for(int i = 0; i < 20; i++) {

        }
        System.out.println(new MainStend().getTime(1000));
    }

    private String getTime(long s){
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
