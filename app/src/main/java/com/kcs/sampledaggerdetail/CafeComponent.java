package com.kcs.sampledaggerdetail;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

@Singleton
@Component(modules = {
        CafeModule.class
})
public interface CafeComponent {
    CafeInfo cafeInfo();
    CoffeeComponent.Builder coffeeComponent();

    @Component.Builder
    interface Builder{
        Builder cafeModule(CafeModule coffeeModule);
        CafeComponent build();
    }
}
