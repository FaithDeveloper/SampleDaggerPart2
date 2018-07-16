package com.kcs.sampledaggerdetail;

import dagger.Subcomponent;

@CoffeeScope
@Subcomponent(modules = {
        CoffeeModule.class
})
public interface CoffeeComponent {
    //provision method
    CoffeeMaker coffeeMaker();
    CoffeeBean coffeeBean();

    @Subcomponent.Builder
    interface Builder{
        Builder cafeModule(CoffeeModule coffeeModule);
        CoffeeComponent build();
    }
}
