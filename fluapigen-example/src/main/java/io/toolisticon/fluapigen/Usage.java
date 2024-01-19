package io.toolisticon.fluapigen;

public class Usage {

    public static void main (String[] args) {
        System.out.println("1st := " + FirstApiStarter.setName("1st").getName());
        System.out.println("2nd := " + SecondApiStarter.setName("2nd").getName());
    }


}
