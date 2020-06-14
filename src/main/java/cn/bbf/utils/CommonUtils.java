/*
 * $Id: CommonUtils.java 12456 2020-03-05 05:27:46Z xinxudi $
 * 版权所有 2016 冠新软件。
 * 保留所有权利。
 */
package cn.bbf.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bbf.domain.standard.CodeSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 通用工具类
 */
public class CommonUtils extends StringUtils {

    /**
     * @author HS
     * @since 2016年6月18日 下午3:59:06 判断对象是否为空或空字符串
     */
    public static boolean isBlankOrEmpty(Object object) {
        if (null == object) {
            return true;
        }
        if (object.toString().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * @author HS
     * @since 2016年6月24日 下午4:44:27 判断集合是否为空
     */
    public static boolean isCollectionBlankOrEmpty(Collection<?> object) {
        if (null == object) {
            return true;
        }
        if (object.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * @author hn
     * @since 2017年1月20日 下午7:03:03 根据出生日期获取年龄
     */
    public static Integer getAge(Date birthday, Integer isElder) {
        if (birthday == null) {
            return 0;
        }
        Calendar now = Calendar.getInstance();
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthday);

        // int monthGap = now.get(Calendar.MONTH) -
        // birthCal.get(Calendar.MONTH);
        // int yearGap = (now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR))
        // * 12;
        // int gapMonth = monthGap + yearGap;
        // return gapMonth / 12;

        // 取出系统当前时间的年、月、日部分
        int yearNow = now.get(Calendar.YEAR);
        int monthNow = now.get(Calendar.MONTH);
        int dayOfMonthNow = now.get(Calendar.DAY_OF_MONTH);
        // 取出出生日期的年、月、日部分
        int yearBirth = birthCal.get(Calendar.YEAR);
        int monthBirth = birthCal.get(Calendar.MONTH);
        int dayOfMonthBirth = birthCal.get(Calendar.DAY_OF_MONTH);
        // 当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        // 当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            // 如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周搜索岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * @param persistent 数据库中查询出来的集合
     * @param detached   前台传过来的集合
     * @param primaryKey 主键
     * @param updateArrs 如果主键相等，集合中需要更新的值 是个字符串数组
     * @author HS
     * @since 2016年6月23日 下午1:23:01 一对多关联，把前台传过来的多的一方变化的copy的到后台查询出来的里面
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void copyListForUpdate(List persistent, List detached, String primaryKey, String[] updateArrs) {
        // 存储新增的数据
        List addList = new ArrayList();
        // 存储删除的数据
        List removeList = new ArrayList();

        // 如果原值是空的
        if (CommonUtils.isCollectionBlankOrEmpty(persistent)) {
            if (!CommonUtils.isCollectionBlankOrEmpty(detached)) {
                persistent.addAll(detached);
            }
            return;
        }

        // 如果传过来的是空的
        if (CommonUtils.isCollectionBlankOrEmpty(detached)) {
            if (!CommonUtils.isCollectionBlankOrEmpty(persistent)) {
                persistent.clear();
            }
            return;
        }

        // 删除的
        for (Object object : persistent) {
            if (!detached.contains(object)) {
                removeList.add(object);
            }
        }

        // 新增的
        for (Object object : detached) {
            if (!persistent.contains(object)) {
                addList.add(object);
            }
        }

        // 更新的
        for (Object detachedObject : detached) {
            for (Object persistentObject : persistent) {
                // 主键相等
                if (Reflections.getFieldValue(persistentObject, primaryKey)
                        .equals(Reflections.getFieldValue(detachedObject, primaryKey))) {
                    // 更新具体的值
                    for (String updateFieldName : updateArrs) {
                        Reflections.setFieldValue(persistentObject, updateFieldName,
                                Reflections.getFieldValue(detachedObject, updateFieldName));
                    }
                    break;
                }
            }
        }
        persistent.removeAll(removeList);
        persistent.addAll(addList);
    }

    /**
     * @author HS
     * @since 2016年7月18日 下午5:06:54 根据类型和代码获取代码的中文值
     */
    public static String getCodeCommonValue(String type, String code) {
        if (code == null) {
            return null;
        }
        String codeValue = null;
        for (CodeSet codeSet : CommonAtrr.CODESETS) {
            if (codeSet.getType().getType().equals(type)) {
                codeValue = codeSet.getValue(code);
            }
        }
        return codeValue;
    }



    private static void copyMapValue(Map<String, Object> destMap, Map<String, Object> sourceMap) {
        for (String sourceKey : sourceMap.keySet()) {
            destMap.put(sourceKey, sourceMap.get(sourceKey));
        }
    }

    /*
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLenthString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    /**
     * @author计算obj合计

     */
    public static void caculateTotalItem(Object adItem, Object totalItem) {
        Field[] toiFields = totalItem.getClass().getDeclaredFields();
        Field[] itemFields = adItem.getClass().getDeclaredFields();
        for (Field toiField : toiFields) {
            for (Field itemField : itemFields) {
                if (toiField.getName().equals(itemField.getName())
                        && Reflections.getFieldValue(adItem, toiField.getName()) != null
                        && ("class java.lang.Integer".equals(toiField.getType().toString())
                        || "class java.math.BigDecimal".equals(toiField.getType().toString()))) {
                    if (CommonUtils.isBlankOrEmpty(Reflections.getFieldValue(totalItem, toiField.getName()))) {
                        Reflections.setFieldValue(totalItem, toiField.getName(),
                                Reflections.getFieldValue(adItem, toiField.getName()));
                    } else {
                        if ("notSignCount".equals(itemField.getName())
                                || "notSignZDCount".equals(itemField.getName())) {
                            break;
                        }
                        if ("organizationLevel".equals(itemField.getName())
                                || "organizationLevel".equals(itemField.getName())) {
                            break;
                        }
                        if ("class java.lang.Integer".equals(toiField.getType().toString())) {
                            Reflections.setFieldValue(totalItem, toiField.getName(),
                                    (Integer) Reflections.getFieldValue(totalItem, toiField.getName())
                                            + (Integer) Reflections.getFieldValue(adItem, toiField.getName()));
                        }
                        if ("class java.math.BigDecimal".equals(toiField.getType().toString())) {
                            Reflections.setFieldValue(totalItem, toiField.getName(),
                                    ((BigDecimal) Reflections.getFieldValue(totalItem, toiField.getName()))
                                            .add(((BigDecimal) Reflections.getFieldValue(adItem, toiField.getName()))));
                        }
                    }
                    break;
                }
            }
        }
    }


    public static void caculateMap(Map<String, Object> adItem, Map<String, Object> totalItem) {
        for (String key : adItem.keySet()) {
            if (totalItem.get(key) == null) {
                totalItem.put(key, adItem.get(key));
            } else {
                for (String toiKey : totalItem.keySet()) {
                	if("orgLevel".equals(toiKey)) {
                		continue;
                	}
                    if (toiKey.equals(key) && StringUtils
                            .isNumeric(totalItem.get(toiKey).toString())) {
                        totalItem.put(toiKey,
                                Integer.valueOf(
                                        totalItem.get(toiKey).toString())
                                        + Integer.valueOf(
                                        adItem.get(key).toString()));
                        break;
                    }
                }
            }
        }
    }

    /**
     * 计算map合计
     *
     * @since 2017年7月25日
     */
    public static Map<String, Object> createTotalItem(List<Map<String, Object>> adList) {
        Map<String, Object> totalItem = new HashMap<>();
        for (Map<String, Object> item : adList) {
            for (String key : item.keySet()) {
                if (totalItem.get(key) == null) {
                    totalItem.put(key, item.get(key));
                } else {
                    for (String toiKey : totalItem.keySet()) {
                        if (toiKey.equals(key) && StringUtils.isNumeric(totalItem.get(toiKey).toString()) 
                        		&& StringUtils.isNumeric(item.get(key).toString())) {
                            totalItem.put(toiKey, Integer.valueOf(totalItem.get(toiKey).toString())
                                    + Integer.valueOf(item.get(key).toString()));
                            break;
                        }
                    }
                }
            }
        }
        return totalItem;
    }

    /**
     * @author  base64转图片
     */
    public static boolean generateImage(String base64Str, String destUrl) {
        if (base64Str == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 生成png图片
            OutputStream out = new FileOutputStream(destUrl);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    
    public static long generateImageSize(String base64Str) {
        long size = 0;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b;
        try {
            b = decoder.decodeBuffer(base64Str);
            size = b.length;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;

    }

    /**
     * 获取拼音首字母
     *
     * @param chinese
     * @return
     */
    public static String cn2FirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (_t != null) {
                        pybf.append(_t[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 数组按照从小到大排序
     *
     * @param s
     * @return

     */
    public static String[] stringSort(String[] s) {
        List<String> list = new ArrayList<String>(s.length);
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }
        Collections.sort(list);
        return list.toArray(s);
    }

    /**
     * 加密为base64文本
     *
     * @param cotent   文本内容
     * @param textCode 编码格式
     * @return
     */
    public static String generateBase64FromStr(String cotent, String textCode) {
        final BASE64Encoder encoder = new BASE64Encoder();
        byte[] textByte;
        try {
            textByte = cotent.getBytes(textCode);
            // 编码
            final String encodedText = encoder.encode(textByte);
            return encodedText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }



    /**
     * 校验签名
     * @param signedValue 前台传过来的签名
     * @param value 后台要签名的字符串
     * @return
     */
    public static boolean md5(String signedValue, String value) {
        String md5Value = DigestUtils.md5Hex(DigestUtils.md5Hex(value));
        if (md5Value.equals(signedValue)) {
            return true;
        }
        return false;
    }

    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个Map分别存oldObject,newObject此属性名的值
     * @param obj1 进行属性比较的对象1
     * @param obj2 进行属性比较的对象2
     * @param ignoreList 忽略比较的字段
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, List<String> ignoreList) {
        try{
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = obj1.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(obj1);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(obj2);
                    if(o1 instanceof Date){
                        o1 = DateUtil.format(new Date(((Date) o1).getTime()), DateUtil.YEAR_MONTH_DAY_PATTERN);
                    }
                    if(o2 instanceof Date){
                        o2 = DateUtil.format(new Date(((Date) o2).getTime()), DateUtil.YEAR_MONTH_DAY_PATTERN);
                    }
                    if(o1 == null && o2 == null){
                        continue;
                    }else if(o1 == null && o2 != null){
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                        continue;
                    }
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                    }
                }
            }
            return map;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
}
