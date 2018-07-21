# Dagger 간단히 알아보기

> Dagger는 자바와 안드로이드를 위한 완전히 Static한 컴파일 타임 의존성 주입 프레임 워크입니다. Square에서 작성한 초기 버전의 개정으로 현재 Google에서 관리하고 있습니다.     -  google.gihub.io/dagger

프로그래머 사이에 DI의 중요성이 강조되고 있습니다. 그러면서 도입되고 Dagger는 필수 라이브러리로 주목받고 있습니다. Dagger 사용법을 간단히 공유하겠습니다.



# Dagger 란?

Dagger은 DI을 도와주는 FrameWork을 의미합니다. Dagger는 다음 5가지의 필수 개념이 있습니다.

1. Inject
2. Component
3. Subcomponent
4. Module
5. Scope

각 개념들에 대해서 하나씩 알아보겠습니다.

#### Inject

의존성 주입을 요청합니다. Inject 으로 주입을 요청하면 연결된 Component가 Module로부터 객체를 생성하여 넘겨줍니다. Inject 어노테이션을 DI 진행할 멤버변수와 생성자에 달아줌으로서 DI 대상을 확인할 수 있습니다.

#### Component

연결된 Module을 이용하여 의존성 객체를 생성하고, Inject로 요청받은 인스턴스에 생성한 객체를 전달(주입)합니다. 의존성을 요청받고 전달(주입)하는 Dagger의 주된 역할을 수행합니다. Component 생성 시 interface나 abstract class에 @Subcomponent 를 달아주면 생성 할 수 있습니다.

#### Subcomponent

Component는 계층관계를 만들 수 있습니다. Subcomponent는 Inner Class 방식의 하위계층 Component 입니다. 연속된 Sub의 Sub도 가능합니다. Subcomponent는 Dagger의 중요한 컨셉인 그래프를 형성합니다. Inject로 주입을 요청받으면 Subcomponent에서 먼저 의존성을 검색하고, 없으면 부모로 올라가면서 검색합니다. Subcomponent는 component와 달리 코드 생성은 부모 component에서 이루어 집니다. subcomponent는 component와 마찬가지로 interface나 abstract class에 @Subcomponent 를 달아주면 생성 할 수 있습니다.

#### Module

Component에 연결되어 의존성 객체를 생성합니다. 생성 후 Scope에 따라 관리도 합니다.

#### Scope

생성된 객체의 Lifecycle 범위입니다. 안드로이드에서는 주로 PerActivity, PerFragment 등으로 화면의 생명주기와 맞추어 사용합니다. Module에서 Scope을 보고 객체를 관리합니다.

위 5가지 개념을 따라 Dagger가 의존성을 주입하는 Flow는 다음과 같습니다.



# 간단한 예제를 활용한 Dagger

제가 [참고한 블로그 ](https://cmcmcmcm.blog/2017/07/27/didependency-injection-%EC%99%80-dagger2/)의 예제가 가장 간편히 잘되어 있어서 그 예제를 기반으로 공유하려고합니다. [DI란 무엇인가?](https://github.com/FaithDeveloper/TIL/blob/master/Android/DI%20%EB%9E%80%20%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80%3F.md) 포스트의 예제에 덧붙여서 Dagger을 작성하였습니다. 

### Module

------

의존성 관계를 설정하는 클래스인 `Module`을 정의 하겠습니다. 모듈을 사용할 곳에서 `annotation`  으로 `Module` 이라는 것을 정의하면 정의가 완료됩니다. 

여기서 `Provides` 라는 `annotation` 을 사용한 것을 확인할 수 있습니다. `interface` 로 구성된 `heater` 와 `pump` 을 주입하기 위해서 `Provides` 로 구성하였습니다.

```java
@Module
public class CoffeeMakerModule {
    @Provides
    Heater provideHeater(){
        return new A_Heater();
    }

    @Provides
    Pump providePump(Heater heater){
        return new A_Pump(heater);
    }
}
```

<br/>

### Component

------

Component 에는 abstract 메소드가 있어야 합니다. 이 메소드를 통하여 의존성 주입이 가능합니다. 

```
1. injection 시킬 객체 리턴 (Provision Return)
2. 맴버 파리미터로 의존성 주입 시킬 객체 받음 (Member-injection Methods)
```

CoffeeComponet 라는 클래스로 표현하면 다음과 같습니다.

```
@Component(modules = CoffeeMakerModule.class)
public interface CoffeeComponent {
    //provision method
    CoffeeMaker make();

    //member-injection method
    void inject(CoffeeMaker coffeeMaker);
}
```

`CoffeeComponent` 라는 `interface` 는  `provision method` 와 `member-injection method` 을 갖고 있습니다. 

`Component` 이라는 `annotation` 을 갖고 있습니다. `Component` 에는 의존성 주입을 구현할 모듈을 선언하면 `provides` 을 통하여 의존성 주입 할 코드를 생성합니다.

<br/>

### inject

------

`inject` 을 통하여 의존성 주입을 합니다. `생성자` 또는 `맴버 변수` 에 `annotation` 을 선언하여 사용할 수 있습니다.

```java
@Inject
public CoffeeMaker(Heater heater, Pump pump){
    this.heater = heater;
    this.pump = pump;
}
```

`component` 에서 선언한  `Member-injection Methods` 인 inject 을 사용하려면 추가로 `Inject` `annotation`을 선언해야 합니다. 

**CoffeeMaker.class**

```java
// 맴버 변수 @Inject 추가
@Inject Heater heater;
@Inject Pump pump;

@Inject
public Coffeemaker(Heater heater, Pump pump){
    this.heater = heater;
    this.pump = pump;
}
// 생성자 추가
public Coffeemaker(){

}
```

CoffeeMaker 의 A_Pump 는 Heater 을 매개변수로 받게 됩니다. 따라서 이 부분도 의존성 주입이 필요합니다.

**A_Pump.class**

```java
@Inject
public A_Pump(Heater heater) {
    this.heater = heater;
}
```

<br/>

### Dagger Create() 또는 Build() 하기

------

Dagger에서는 `Component` `interface`을 `Dagger[YourComponent이름]` 형태로 구현합니다. 위 예제에서는 **CoffeeComponent**로 `componet` 을 설정 했으니 **DaggerCoffeeComponent**로 구현되어 있습니다. 

> 만약, `Dagger[YourComponent이름]` 이 없을 경우
>
> 1. Project Build 을 해줍니다.
> 2. 1번을 하여도 마찬가지인 경우 `dagger-compiler` 라이브러리를 포함하고 있는지 확인합니다.

**Android Studio 로 Java Class 구현 할 경우 `Build.gradle`**

```groovy
dependencies {
    implementation 'com.google.dagger:dagger:2.16'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.16'
}
```

`Component` 는 create() 또는 build() 함수로 만들수 있습니다.

**MainDaggerExample.class**

```java
DaggerCoffeeComponent.create().make().brew();
```

CoffeeComponet의 **void inject(Coffeemaker coffeemaker)** 사용 시 CoffeeMaker 객체를 생성해서 inject 함수로 넘겨 의존성 주입하면 됩니다. 

이미 DI 을 해야할 부분에 `Inject` 로 선언하였기에 가능합니다.

```
CoffeeMaker coffeeMaker = new CoffeeMaker();
DaggerCoffeeComponent.create().inject(coffeeMaker);
coffeeMaker.brew();
```

간단히 `Module`, `Component`, `Inject`을 사용하는 예제를 구현했습니다.

예제 소스코드는 [GitHub](https://github.com/FaithDeveloper/SampleDagger)에서 확인 할 수 있습니다.

<br/>

다음은 `Subcomponent`, `Scope`, `Binds`, `Multibinding` 을 할용하는 예제로 사용법을 익혀가겠습니다. 

이 예제 또한  [참고한 블로그 ](https://cmcmcmcm.blog/2017/08/02/didependency-injection-와-dagger2-2) 에 있는 예제를 바탕으로 작성하였습니다.

이번 예제는 새로운 프로젝트를 생성하여 진행하겠습니다. 

CafeApp 을 구성하는데 CafeInfo 와 CoffeeMaker, CoffeeBean 부터 생성해줍니다.  
(제가 참고한 블로그 기준으로 소스를 구성하였습니다.)

**CafeInfo.class**

```java
public class CafeInfo {
    private String name;

    public CafeInfo(){}
    public CafeInfo(String name){ this.name = name; }

    public void welcome(){
        System.out.println("Welcomename == null? "":name );
    }
}
```

**CoffeeMaker.class**

```java
public class CoffeeMaker{
    private Heater heater;

    @Inject
    public CoffeeMaker(Heater heater){
        this.heater = heater;
    }

    public void brew(CoffeeBean coffeeBean){
        System.out.println("CoffeeBeen("+coffeeBean.toString coffee!");
    }
}
```

**CoffeeBean**

```java
public class CoffeeBean {

    public void name(){
        System.out.println("CoffeeBean")
    }
}
```

`DI` 예제소스로 경험했듯이 `Component` 와 `Module` 을 작성합니다.

**CafeComponent.class**

```java
@Component(modules = {
        CafeModule.class
})
public interface CafeComponent {
    CafeInfo cafeInfo();
    CoffeeMaker coffeeMaker();
}
```

**CafeModule.class** 

```java
@Module
public class CafeModule {
    @Provides
    CafeInfo provideCafeInfo(){
        return new CafeInfo();
    }

    @Provides
    CoffeeMaker provideCoffeeMaker(Heater heater){
        return new CoffeeMaker(heater);
    }

    @Provides
    Heater provideHeater(){
        return new Heater();
    }
}
```

참고한 블로그에 나온 시나리오로 동일하게 CafeApp 을 구성하면 다음과 같습니다.

```
1. CafeInfo 는 하나뿐이며 누가 보던지 같은 결과를 보여줘야합니다.
2. Cafe 가 없어지면 CafeMaker도 없어집니다.
   단, Cafe가 유지된다면 CafeMaker를 새로 만들거나 교체할 수 있습니다.
3. CafeMaker 생성 될 때 항상 같은 Heater가 들어갑니다.
```

</br>

### Scope

------

조건에서 'CafeInfo 는 하나뿐이다.'라고 하였습니다.  `Component` 에 `Scope` `annotation` 을 사용하면 해당 `Component` 에 Binding 되는 객체들은 Component와 같은 LifeCycle 을 갖게 됩니다.

**CafeComponent.class**

```java
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
```

'**Builder cafeModule(CafeModule coffeeModule)** '을 통하여 모듈을 미리 `Bind` 할 수 있습니다.

**CafeModule.class**

```java
@Module(subcomponents = CoffeeComponent.class)
public class CafeModule {
    @Singleton
    @Provides
    CafeInfo provideCafeInfo(){
        return new CafeInfo();
    }
}
```

<br/>

### 커스텀 Scope

------

Dagger 에서는 개발자가 Scope을 만들 수 있습니다. 

```java
@Scope
public @interface [커스텀 Scope Name] {
}
```

여기서 주의할 점은 `@interface` 로 구성된다는 것입니다. 

`CoffeeComponent` 에 `Scope` 을 활용하여 CoffeeMaker와 Heater의 `LifeCycle`을 설정하겠습니다. 

비교를 위해서 CoffeeBean도 얻도록 설정을 하면서 Scope 설정을 하지 않았습니다.

**CoffeeModule.class**

```java
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

    @Provides
    CoffeeBean provideCoffeeBean(){
        return new CoffeeBean();
    }
}
```

### Subcomponent

------

 Subcomponent는 부모 Component을 갖고 있는 Component 입니다. `@SubComponent` 에서 `Builder` `interface` 을 정의 되어야만 `Component` 에서 코드가 생성됩니다. 

> `Dagger` 는 `component` 생성 시 `builder` 을 사용합니다. `@Subcomponent` 는 `@Component` 클래스 안에서 코드가 생성될 때 `@Subcomponent.Builder` 가 붙은 `interface` 가 없으면 ` builder` 가 자동으로 생성되지 않습니다. 그러므로 @Subcomponent.Builder 가 꼭 필요합니다.  

**CoffeeComponent.class**

```java
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
```

<br/>

어떻게 `Component` 하고 `SubComponent` 을 연결하는 걸까요? 그 해답은 `Module` 을 확인하면 됩니다.

CafeApp 에서 `Component`는 `CafeModule` 이었습니다. `@Module` 에 `subcomponents = [SubComponent Name]'을 입력하면 연결이 됩니다.

```java
@Module(subcomponents = CoffeeComponent.class)
public class CafeModule {
    
    private String name;
    
    public CafeModule(){
        
    }
    
    public CafeModule(String name){
        this.name = name;
    }
    
    @Singleton
    @Provides
    CafeInfo provideCafeInfo(){
     	if(name == null || name.isEmpty())  return new CafeInfo();
        else    return new CafeInfo(name);
    }
}
```

> **@Module(subcomponents = CoffeeComponent.class) 을 시도 시 subcomponents  가 없다고 나오는 경우**
>
>  [Dagger 버전](https://github.com/google/dagger) 으로 설정합니다.  **(Dagger 버전 : 2.7 이상)**

<br/>

Scope 가 정상으로 동작하였는지 확인하겠습니다.

```java
public class CaffeeApp {
    public static void main(String[] args) {
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
}
```

**결과**

```
Singletone CafeInfo is equal : true
// CafeInfo 는 Scope 로 Singleton 을 구현하였기에 동일하다는 판단을 합니다.

Makere / same component coffeeMaker is equal : true
// coffeeComponent1을 coffeemaker 로 2개 호출합니다. 동일한 coffeeScope 을 사용하기에 동일하다고 나옵니다.

Makere / different component coffeeMaker is equal : false
// coffeeScope 로 인하여 CoffeeComponent 은 1:1 매칭 하게 됩니다. coffeeComponent1 와 coffeeComponent2 은 다르게 됩니다.

Non-scopedeebean is equal : false
// scope 설정하지 않았기에 CoffeeBean 생성 할 때마다 새로 생성하게 됩니다.
```

<br/>

CafeComponent 생성 시 모듈 먼저 생성 시 다음과 같이 할 수 있습니다.

```java
CafeComponent cafeComponent = DaggerCafeComponent.builder()
        .cafeModule(new CafeModule("example cafe"))
        .build();
cafeComponent.cafeInfo().welcome();
```

이렇게 가능한 이유는 CafeCompoent의 Builder을 선언했기 때문입니다.

```java
@Component.Builder
interface Builder{
     Builder cafeModule(CafeModule cafeModule);
     CafeComponent build();
}
```

<br/>

### Binds

------

`Binds` 추상메서드를 이용해서 간략하게 `Binding` 할 때 사용합니다. 

```java
public class EthiopiaBean extends CoffeeBean{

    public void name(){
        System.out.println("EthiopiaBean   }
}
```

CoffeeBean 을 상속받은 EthiopiaBean 이 있을 시 `Binds` 을 통하여 abstract(추상)메서드로 정의하는 것으로 CoffeeBean 객체를 받을 수 있습니다. 단 EthiopiaBean 가 provider 메서드를 제공하고 있거나 @Inject 로 선언이 되어 있어야 합니다. 구체적은 구현은 `MultiBinding` 을 보시면 이해할 수 있을 것입니다.

**CoffeeBeanModule.class**

```
@Binds
abstract CoffeeBean provideCoffeeBean(EthiopiaBean ethiopiaBean);
```

<br/>

### MultiBinding

------

`Dagger` 는 한 객체가 여러가지 형태로 Binding 할 때 `set` 이나 `Map` 을 이용해서 `MultiBinding` 을 할 수 있습니다.

만약 GuatemalaBean 이 있을 경우 (이전에 만든 EthipiaBean 도 같이 있습니다.) 

```java
public class GuatemalaBean extends CoffeeBean {
    public void name(){
        System.out.println("GuatemalaBean   }
}
```

CoffeeBean으로 Binding 시도 시 EthiopiaBeand와 GuatemalaBean 중 어디에서 CoffeeBean Binding 시도할 것인지 혼란이 오게됩니다. 이럴때 `MultiBinding` 을 사용합니다.

**CoffeeBeanModule.class**

```java
@Module
public abstract class CoffeeBeanModule {
    // @Binds 사용하기 위해서는 @Provide 또는 @Inject 설정해야 합니다.

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
```

`@IntoMap` 과 `@StringKey` 를 이용하여 `Map` 형태(Map<key,object>)로 MultiBinding 한 코드입니다.  

`@Binds` 을 사용하려면 abstract 으로 선언된 추상 메소드 선언과 함께 provider 메서드를 제공하고 있거나 @Inject 로 선언이 되어 있어야 한다고 하였습니다. 

**CoffeeModule.class**

```java
@Module
public class CoffeeModule {
   ...
    // EthiopiaBean @Provides
    // 타입에 맞춰 생성될 객체가 명확할 때는 @Inject 형태로도 가능.
    // 그렇지 않으면 @Provide method 를 만들어주어야 함.
    @Provides
    EthiopiaBean provideEthiopiaBean(){
    return new EthiopiaBean();
	}
}
```

**GuatemalaBean.class**

```java
public class GuatemalaBean extends CoffeeBean {
    //@Inject annotation 구현으로 통한 @Binds 사용할 수 있도록 선언.
    @Inject
    public GuatemalaBean(){
        super();
    }

    public void name(){
        System.out.println("GuatemalaBean");
    }
}

```

**테스트**

```java
CoffeeComponent coffeeComponent = DaggerCafeComponent.create().coffeeComponent().build();
coffeeComponent.coffeeBeanMap().get("guatemala").name();
```

**결과**

```
GuatemalaBean

```

<br/>

# 정리

`DI` , `Dagger` 에 대해서 간단히 개념적인 부분을 살펴봤습니다. 친절하게 설명한 블로그에 있는 소스 및 설명 기준으로 내용을 재구성하였습니다. Dagger 에서 사용한 다양한 `annotation` 과 활용법을 확인하였는데요. 준비과정이 필요하지만 사용할 때는 간편히 사용하는 것을 확인하였습니다. 여전히 `Dagger` 의 진입장벽이 크다는 것을 경험하였습니다. 하지만 [예제소스 - SampleDaggerPart2](https://github.com/FaithDeveloper/SampleDaggerPart2)을 작성하면서 `Dagger` 에 대해서 익숙해시면서 느낌점은  `DI`구성을 할 경우 사용할 때는 더이상 신경 안써도 되는 장점을 발견했습니다.  `Dagger` 을 활용하여 `DI` 구조가 필요한 프로젝트에 사용하면 충분히 퍼모먼스를 낼 수 있을 것으로 예상됩니다.

<br/>

# 참고

[Multi Module 과 Dagger2](https://medium.com/@jsuch2362/multi-module-%EA%B3%BC-dagger2-8472492eaba3)

[DI, Dagger2 란?](http://duzi077.tistory.com/168)

[DI(Dependency Injection) 와 Dagger2](https://cmcmcmcm.blog/2017/08/02/didependency-injection-%EC%99%80-dagger2-2/)

[DI 기본개념부터 사용법까지, Dagger2 시작하기](https://medium.com/@rfrost77/di-%EA%B8%B0%EB%B3%B8%EA%B0%9C%EB%85%90%EB%B6%80%ED%84%B0-%EC%82%AC%EC%9A%A9%EB%B2%95%EA%B9%8C%EC%A7%80-dagger2-%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0-3332bb93b4b9)

[Dagger2 학습에 필요한 참고자료](https://www.androidhuman.com/life/2016/06/06/dagger2_resources/)

[Dagger Open Source](https://github.com/google/dagger)

