package cn.bbf;

import cn.bbf.utils.CommonUtils;
import cn.bbf.utils.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: WJ
 * @date 2020/3/10 17:59
 * @description: TODO
 */
public class Test {

    /**
     * https://www.cnblogs.com/jxd283465/p/11726678.html 为什么要用static final修饰
     */
    public static final Logger logger  = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.setAge("12");
        testClass.setName("sdaf");
        testClass.setTestClassId("1224234");
        TestClass newClass = new TestClass();
        testclass(testClass,newClass);
        System.out.println(newClass.toString());
//        String format = String.format("%02d",19);
//        logger.info("测试结果----{}",format);
//        String str = "upload/1000000001234034120";
//        String newStr = str.replaceAll("^(0+)", "");
//        System.out.println(newStr);
//        System.out.println(Test.substr("650000"));
//        System.out.println(str.substring(7));
//        Map<String,Object> map = new HashMap<>();
//        map.put("wangjie","王杰");
//        System.out.println(!map.containsKey("wang"));
//        String testString = "wangabc#wefw#";
//        System.out.println(testString.replace("#","%23"));

//        String str="河北机构注册卫生院";
//        String regEx="(卫生室) |(卫生院) "; //表示a或f
//        Pattern p=Pattern.compile(regEx);
//        boolean result=p.matcher(str).find();
//        System.out.println(result);
//        String address="中华人民共和国吉林省长春市二道区临河街万兴小区4栋2门";
//        Test.testRegex(address);
    }

    public static String substr(String xzqh) {
        if (StringUtils.isNotEmpty(xzqh)) {
            if (xzqh.length() > 6 && xzqh.length() <= 12) {
                String suffix = xzqh.substring(xzqh.length() -3, xzqh.length());
                if (suffix.equals("000")) {
                    xzqh = substr(xzqh.substring(0, xzqh.length() - 3));
                }
            } else {
                String suffix = xzqh.substring(xzqh.length() -2, xzqh.length());
                if (suffix.equals("00")) {
                    xzqh = substr(xzqh.substring(0, xzqh.length() - 2));
                }
            }
            return xzqh;
        }
        return null;
    }
    public static void testRegex(String address){
        String regex = "(.*国)|(.*省)|(.*市)|(..?区)|(.*?街)|(.*)" ;
        Pattern compile = Pattern.compile(regex);
        Matcher matcher =compile.matcher(address);
        while(matcher.find()){
            String group = matcher.group();
            System.out.println(group);
        }
    }
    public static void testclass(Object o1,Object newObject){
        Field[] declaredFields = o1.getClass().getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Reflections.setFieldValue(newObject,declaredField.getName(),Reflections.getFieldValue(o1,declaredField.getName()));
                System.out.println(declaredField.getType() +" : "+declaredField.getName() +" : "+declaredField.get(o1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
