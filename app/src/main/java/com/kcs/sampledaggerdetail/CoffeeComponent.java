package com.kcs.sampledaggerdetail;

import java.util.Map;

import dagger.Subcomponent;

@CoffeeScope
@Subcomponent(modules = {
        CoffeeModule.class
        , CoffeeBeanModule.class
})
public interface CoffeeComponent {
    //provision method
    CoffeeMaker coffeeMaker();
    CoffeeBean coffeeBean();
    Map<String, CoffeeBean> coffeeBeanMap();

    @Subcomponent.Builder
    interface Builder{
        Builder cafeModule(CoffeeModule coffeeModule);
        CoffeeComponent build();
    }
}
