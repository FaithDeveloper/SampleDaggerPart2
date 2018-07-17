package com.kcs.sampledaggerdetail;

import dagger.Module;
import dagger.Provides;

@Module
public class CoffeeModule {
    @CoffeeScope
    @Provides
    CoffeeMaker provideCoffeemaker(Heater heater) {
        return new CoffeeMaker(heater);
    }

    @CoffeeScope
    @Provides
    Heater provideHeater() {
        return new Heater();
    }

    // 타입에 맞춰 생성될 객체가 명확할 때는 GuatemalaBean 처럼 @Inject 형태로도 가능. (GuatemalaBean.class 참고)
    // 그렇지 않으면 @Provide method 를 만들어주어야 함.
    @Provides
    EthiopiaBean provideEthiopiaBean(){
    return new EthiopiaBean();
}

    // Binds 쓰기 전
//    @Provides
//    CoffeeBean provideCoffeeBean(){
//        return new CoffeeBean();
//    }
}
