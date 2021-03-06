package com.kcs.sampledaggerdetail;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public abstract class CoffeeBeanModule {
    // @Binds 사용하기 위해서는 @Provide 또는 @Inject 설정해야 합니다.

    // CoffeeComponent 의  CoffeeBean coffeeBean() 의 값을 넣기 위해서 사용.
    @Binds
    abstract CoffeeBean provideCoffeeBean(EthiopiaBean ethiopiaBean);

    @Binds
    @IntoMap
    @StringKey("ethiopia")
    abstract CoffeeBean provideEthiopiaBean(EthiopiaBean ethiopiaBean);
    // EthiopiaBean  @Provide method 제공

    @Binds
    @IntoMap
    @StringKey("guatemala")
    abstract CoffeeBean provideGuatmalaBean(GuatemalaBean guatemalaBean);
    // EthiopiaBean 처럼 @Provide method 를 제공하지 않아도 @Inject annotation을 이용해 Injectable 한 객체로 정의해도 bind 가능.
}
