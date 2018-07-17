package com.kcs.sampledaggerdetail;

public class CaffeeApp {
    public static void main(String[] args) {
/*
        // === Subcomponent ====
        {
        // Scope
        CafeComponent cafeComponent = DaggerCafeComponent.create();
        CafeInfo cafeInfo1 = cafeComponent.cafeInfo(); // 동일한 singleton scope 이기 때문에 같은 객체가 리턴
        CafeInfo cafeInfo2 = cafeComponent.cafeInfo();
        System.out.println("Singletone CafeInfo is equal : " + cafeInfo1.equals(cafeInfo2));

        //CoffeeScope
        CoffeeComponent coffeeComponent1 = cafeComponent.coffeeComponent().build();
        CoffeeComponent coffeeComponent2 = cafeComponent.coffeeComponent().build();
        CoffeeMaker coffeeMaker1 = coffeeComponent1.coffeeMaker();
        CoffeeMaker coffeeMaker2 = coffeeComponent1.coffeeMaker();
        System.out.println("Makere / same component coffeeMaker is equal : " + coffeeMaker1.equals(coffeeMaker2));
        CoffeeMaker coffeeMaker3 = coffeeComponent2.coffeeMaker(); //MakerScopeMethod
        System.out.println("Makere / different component coffeeMaker is equal : " + coffeeMaker1.equals(coffeeMaker3));

        //Non-scope
        CoffeeBean coffeeBean1 = coffeeComponent1.coffeeBean();
        CoffeeBean coffeeBean2 = coffeeComponent1.coffeeBean();
        System.out.println("Non-scopedeebean is equal : " + coffeeBean1.equals(coffeeBean2));
        }
        */

    // Builder 활용
        /*{
            CafeComponent cafeComponent = DaggerCafeComponent.builder()
                    .cafeModule(new CafeModule("Example Cafe!"))
                    .build();
            cafeComponent.cafeInfo().welcome();
        }*/

    // IntoMap 활용
        {
            CoffeeComponent coffeeComponent = DaggerCafeComponent.create().coffeeComponent().build();
            coffeeComponent.coffeeBeanMap().get("guatemala").name();
        }
    }
}
