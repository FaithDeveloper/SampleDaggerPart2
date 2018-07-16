package com.kcs.sampledaggerdetail;


public class CafeInfo {
    private String name;
    public CafeInfo(){

    }
    public CafeInfo(String name){
        this.name = name;
    }
    public void welcome(){
        System.out.printf("Wellcome " +  name == null ?  "" : name);
    }
}
