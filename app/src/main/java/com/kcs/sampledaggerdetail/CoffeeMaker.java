package com.kcs.sampledaggerdetail;

import javax.inject.Inject;

public class CoffeeMaker {
   private Heater heater;

    @Inject
    public CoffeeMaker(Heater heater){
        this.heater = heater;
    }

    public CoffeeMaker(){

    }

    public void brew(CoffeeBean coffeeBean){
        System.out.println("CoffeeBeen("+coffeeBean.toString() + "coffee!");
    }

}
