package net.kk.orm;

import org.junit.Test;

public class Test2 {
    static class A{

    }
    static class B extends A{

    }
    static void a(Object o){
        System.out.print(o.getClass());
    }
    @Test
    public void testclass(){
        a((A)(new B()));
    }
}
