package com.kcs.sampledaggerdetail;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(subcomponents = CoffeeComponent.class)
public class CafeModule {
    @Singleton
    @Provides
    CafeInfo provideCafeInfo(){
        return new CafeInfo();
    }

}
