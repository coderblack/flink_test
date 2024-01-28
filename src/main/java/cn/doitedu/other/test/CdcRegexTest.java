package cn.doitedu.other.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CdcRegexTest {
    public static void main(String[] args) {

        Pattern compile = Pattern.compile(".*",2);
        Matcher matcher = compile.matcher("flinktest.flink_score");
        System.out.println(matcher.find());
    }
}
