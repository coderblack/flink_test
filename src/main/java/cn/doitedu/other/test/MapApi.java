package cn.doitedu.other.test;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapApi {
    public static void main(String[] args) {


        HashMap<Integer, String> mp = new HashMap<Integer, String>();
        mp.put(2, "2");
        mp.put(3, "3");

        mp.compute(3,
                (k, s) -> {
                    System.out.println(k);
                    return k + "_abc";
                });

        mp.computeIfAbsent(5, k -> k+"_555");

        mp.computeIfAbsent(3, k -> k+"_33333");

        mp.computeIfPresent(3, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer k, String v) {
                return v+"--------------";
            }
        });

        System.out.println(mp);


    }

}
