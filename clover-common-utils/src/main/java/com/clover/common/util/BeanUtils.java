package com.clover.common.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * bean utils
 *
 * @author dingpengfei
 * @date 2018/8/13 下午2:51
 */
public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 检查对象属性是否全部是空值
     *
     * @param bean 对象
     * @return true/false
     */
    public static boolean checkNull(Object bean) {
        if (bean == null) {
            return true;
        }
        Class<?> cls = bean.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldGetName = schemaGetSetName(field.getName(), true);
                if (!checkGetMet(methods, fieldGetName)) {
                    throw new Exception("没有getter方法");
                }
                Class[] arr = {};
                Method fieldGetMet = cls.getMethod(fieldGetName, arr);
                Object fieldVal = fieldGetMet.invoke(bean, arr);
                if (fieldVal != null) {
                    if (fieldVal instanceof String && !StringUtils.isEmpty(fieldVal.toString())) {
                        return false;
                    } else if (fieldVal.getClass() == int.class || fieldVal.getClass() == Integer.class) {
                        if (Integer.valueOf(fieldVal.toString()) != 0) {
                            return false;
                        }
                    } else if (fieldVal.getClass() == long.class || fieldVal.getClass() == Long.class) {
                        if (Long.valueOf(fieldVal.toString()) != 0L) {
                            return false;
                        }
                    } else if (fieldVal.getClass() == double.class || fieldVal.getClass() == Double.class) {
                        if (Double.valueOf(fieldVal.toString()) != 0.0) {
                            return false;
                        }
                    } else if (fieldVal.getClass() == float.class || fieldVal.getClass() == Float.class) {
                        if (Float.valueOf(fieldVal.toString()) != 0.0) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static Object queryId(Object bean, String methodName) throws Exception {
        if (bean == null || StringUtils.isEmpty(methodName)) {
            throw new Exception("BeanUtils queryId 参数 不可以为空");
        }
        Object id = null;
        Class<?> cls = bean.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!field.getName().equalsIgnoreCase(methodName)) {
                    continue;
                }
                String fieldGetName = schemaGetSetName(field.getName(), true);
                if (!checkGetMet(methods, fieldGetName)) {
                    throw new Exception("没有getter方法");
                }
                Class[] arr = {};
                Method fieldGetMet = cls.getMethod(fieldGetName, arr);
                return fieldGetMet.invoke(bean, arr);


            } catch (Exception e) {
                throw new Exception("BeanUtils queryId 参数 异常");
            }
        }
        return id;
    }


    /**
     * 拼接某属性的 get/set方法
     * <p>
     * param fieldName
     *
     * @return String
     */
    private static String schemaGetSetName(String fieldName, Boolean isGet) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        char split = '_';
        if (fieldName.charAt(0) == split) {
            startIndex = 1;
        }

        String prefix = isGet ? "get" : "set";
        return prefix + fieldName.substring(startIndex, startIndex + 1).toUpperCase() + fieldName.substring(startIndex + 1);
    }

    /**
     * 判断是否存在某属性的 get方法
     * <p>
     * param methods
     * param fieldGetMet
     *
     * @return boolean
     */
    private static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化对象 避免繁琐的对象初始化赋值操作 按需调用
     *
     * @param clazz class
     * @return 对象
     */
    public static Object initObject(Class<?> clazz) {
        Object object;
        try {
            object = clazz.newInstance();
            for (Field field : getFields(object)) {
                if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                String name = field.getName();
                String methodStr = "set" + name.toUpperCase().substring(0, 1) + name.substring(1);
                Method method = clazz.getMethod(methodStr, field.getType());
                if (field.getType().getCanonicalName().equals(String.class.getCanonicalName())) {
                    method.invoke(object, "");
                } else if (field.getType().getCanonicalName().equals(Integer.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(int.class.getCanonicalName())) {
                    method.invoke(object, 0);
                } else if (field.getType().getCanonicalName().equals(Long.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(long.class.getCanonicalName())) {
                    method.invoke(object, 0L);
                } else if (field.getType().getCanonicalName().equals(Boolean.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(boolean.class.getCanonicalName())) {
                    method.invoke(object, false);
                } else if (field.getType().getCanonicalName().equals(Double.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(Float.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(double.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(float.class.getCanonicalName())
                        ) {
                    method.invoke(object, 0.000);
                } else if (field.getType().getCanonicalName().equals(List.class.getCanonicalName())) {
                    method.invoke(object, new ArrayList<>());
                } else if (field.getType().getCanonicalName().equals(Map.class.getCanonicalName())) {
                    method.invoke(object, new HashMap<>(5));
                } else {
                    method.invoke(object, field.getType().newInstance());
                }
            }

        } catch (Exception e) {
            return null;
        }

        return object;
    }

    /**
     * 获取数据
     *
     * @param object 对象
     * @return 属性数组
     */
    private static Field[] getFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        Field[] superFields = object.getClass().getSuperclass().getDeclaredFields();
        return ArrayUtils.addAll(fields, superFields);
    }

    /**
     * 测试数据生成方法 正式代码中请勿使用
     *
     * @param object 对象
     * @return 测试数据
     */
    public static <T> T schemaTestData(T object) {
        try {
            Random random = new Random();
            for (Field field : getFields(object)) {
                if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                String name = field.getName();
                String methodStr = "set" + name.toUpperCase().substring(0, 1) + name.substring(1);
                Method method = object.getClass().getMethod(methodStr, field.getType());
                if (field.getType().getCanonicalName().equals(String.class.getCanonicalName())) {
                    if (containsFiledName(field, "id","password")) {
                        method.invoke(object, UUID.randomUUID().toString().substring(0,16));
                    }
//                    else if (containsFiledName(field, "name")) {
//                        method.invoke(object, RandomValue.getChineseName());
//                    } else if (containsFiledName(field, "mobile","phone","contact")) {
//                        method.invoke(object, RandomValue.getTel());
//                    }else if (containsFiledName(field, "icon")) {
//                        method.invoke(object, UUID.randomUUID().toString().substring(0,16)+".png");
//                    }else if (containsFiledName(field, "grade","type")) {
//                        method.invoke(object, new Random().nextInt(5)+"");
//                    } else if (field.getName().equalsIgnoreCase("parent")) {
//                        method.invoke(object, RandomValue.getChineseName());
//                    }else if (containsFiledName(field, "city","province")) {
//                        method.invoke(object, RandomValue.getRoad());
//                    }
                    else{
                        method.invoke(object, getRandomString(3));
                    }

                } else if (field.getType().getCanonicalName().equals(Integer.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(int.class.getCanonicalName())) {
                    method.invoke(object, random.nextInt(10));
                } else if (field.getType().getCanonicalName().equals(Long.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(long.class.getCanonicalName())) {
                    if (containsFiledName(field, "time", "Time")) {
                        method.invoke(object, System.currentTimeMillis());
                    } else {
                        method.invoke(object, random.nextLong());
                    }

                } else if (field.getType().getCanonicalName().equals(Date.class.getCanonicalName())) {
                    method.invoke(object, new Date());

                } else if (field.getType().getCanonicalName().equals(Boolean.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(boolean.class.getCanonicalName())) {
                    method.invoke(object, true);
                } else if (field.getType().getCanonicalName().equals(Double.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(Float.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(double.class.getCanonicalName())
                        || field.getType().getCanonicalName().equals(float.class.getCanonicalName())
                        ) {
                    method.invoke(object, random.nextDouble());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return object;
    }

    private static boolean containsFiledName(Field field, String... names) {
        if(names==null||names.length==0) {
            return false;
        }
        boolean flag=false;
        for(String name:names){
            if(StringUtils.isEmpty(name)){
                continue;
            }
            if(field.getName().contains(name)){
                flag=true;
                break;
            }else {
                name=name.substring(0,1).toUpperCase()+name.substring(1);
                if(field.getName().contains(name)){
                    flag=true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Auth: dingpengfei
     * Date: 2017/10/12 14:19
     * Title: 比較兩個對象的各个值，返回差异项
     * Param:oldObject老数据
     * Param:newObject新数据
     * Param:compares 比较字段 如果为空则对比所有的字段，反之比较compares中指定的字段
     * Return:jsonObject
     **/
    public static <T> JSONObject compareBean(T oldObject, T newObject, List<String> compares) throws Exception {
        Boolean changed = false;
        JSONObject jsonObject = new JSONObject();
        Class oldClass = oldObject.getClass();
        Class newClass = newObject.getClass();
        Field[] fields = getFields(oldObject);
        for (Field field : fields) {
            try {
                if (!CollectionUtils.isEmpty(compares) && !compares.contains(field.getName())) {
                    continue;
                }
                Field oldField = oldClass.getDeclaredField(field.getName());
                Field newField = newClass.getDeclaredField(field.getName());
                oldField.setAccessible(true);
                newField.setAccessible(true);
                Object oldValue = oldField.get(oldObject);
                Object newValue = oldField.get(newObject);

                String oldStringValue = oldValue == null ? "" : (oldValue instanceof Date) ? DateUtils.formatDate((Date) oldValue, "yyyy-MM-dd HH:mm:ss") : oldValue.toString();
                String newStringValue = newValue == null ? "" : (newValue instanceof Date) ? DateUtils.formatDate((Date) newValue, "yyyy-MM-dd HH:mm:ss") : newValue.toString();

                if (!oldStringValue.equalsIgnoreCase(newStringValue)) {
                    changed = true;
                    logger.info(field.getName() + ": " + oldStringValue + "==>" + newStringValue);
                    jsonObject.put(field.getName(), oldStringValue + "==>" + newStringValue);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new Exception(e.getMessage());
            }
        }

        return changed ? jsonObject : null;
    }

    /**
     * Auth: dingpengfei
     * Date: 2017/9/29 11:50
     * Desc:对象属性的copy 注意：copy之后 source对象如果还会修改 请谨慎使用 （如果对象的属性不是基本类型 long double int boolean AtomicLong）
     *
     * @param source 源数据
     * @param target 新数据
     * @throws SecurityException        异常
     * @throws NoSuchFieldException     异常
     * @throws IllegalArgumentException 异常
     * @throws IllegalAccessException   异常
     **/

    public static void copyProperty(Object source, Object target) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class oldClass = source.getClass();
        Class newClass = target.getClass();
        //该类所有的属性
        Field[] newFields = oldClass.getDeclaredFields();
        Field newField;
        Field oldField;
        for (Field f : newFields) {
            //类中的属性名称
            String fieldName = f.getName();
            //通过属性名称获取属性
            newField = newClass.getDeclaredField(fieldName);
            oldField = oldClass.getDeclaredField(fieldName);
            //获取属性的值时需要设置为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
            //值为 false 则指示反射的对象应该实施 Java 语言访问检查。
            oldField.setAccessible(true);
            newField.setAccessible(true);
            //根据属性获取对象上的值
            Object oldObject = oldField.get(source);
            Object newObject = newField.get(target);
            //过滤空的属性或者一些默认值
            if (oldObject == null || newObject!=null|| !oldField.getType().getCanonicalName().equalsIgnoreCase(newField.getType().getCanonicalName())) {
                continue;
            }
            if (oldField.getType().getCanonicalName().equals(String.class.getCanonicalName())) {
                if(StringUtils.isEmpty(oldObject.toString())){
                    continue;
                }
            }
            newField.setAccessible(true);
            if (oldField.getType().getCanonicalName().equals(String.class.getCanonicalName())) {

                newField.set(target, oldObject.toString());
            } else if (oldField.getType().getCanonicalName().equals(Integer.class.getCanonicalName())
                    || oldField.getType().getCanonicalName().equals(int.class.getCanonicalName())) {
                newField.set(target, Integer.valueOf(oldObject.toString()));
            } else if (oldField.getType().getCanonicalName().equals(Long.class.getCanonicalName())
                    || oldField.getType().getCanonicalName().equals(long.class.getCanonicalName())) {
                newField.set(target, Long.valueOf(oldObject.toString()));
            } else if (oldField.getType().getCanonicalName().equals(Boolean.class.getCanonicalName())
                    || oldField.getType().getCanonicalName().equals(boolean.class.getCanonicalName())) {
                newField.set(target, Boolean.valueOf(oldObject.toString()));
            } else if (oldField.getType().getCanonicalName().equals(Double.class.getCanonicalName())
                    || oldField.getType().getCanonicalName().equals(double.class.getCanonicalName())
                    ) {
                newField.set(target, Double.valueOf(oldObject.toString()));
            } else if (oldField.getType().getCanonicalName().equals(Float.class.getCanonicalName())
                    || oldField.getType().getCanonicalName().equals(float.class.getCanonicalName())
                    ) {
                newField.set(target, Float.valueOf(oldObject.toString()));
            } else if (oldField.getType().getCanonicalName().equals(List.class.getCanonicalName())) {
                newField.set(target, oldObject);
            } else if (oldField.getType().getCanonicalName().equals(Map.class.getCanonicalName())) {
                newField.set(target, oldObject);
            } else if (oldField.getType().getCanonicalName().equals(AtomicLong.class.getCanonicalName())) {
                newField.set(target, new AtomicLong(((AtomicLong) oldObject).longValue()));
            } else {
                newField.set(target, oldObject);
            }
        }
    }

    public static void setField(Object obj,String fieldName,Object value) throws NoSuchFieldException, IllegalAccessException {
        Class clazz=obj.getClass();
        System.out.println(clazz.getName());
        Field[] fields=clazz.getDeclaredFields();
        for(Field f:fields){
            System.out.println(f.getName());
        }
        Field field=clazz.getDeclaredField(fieldName);
        if(field!=null){
            field.setAccessible(true);
            field.set(obj, value);
        }
    }
    public static void setAnnotationField(Object obj,String fieldName,Object value) throws NoSuchFieldException, IllegalAccessException {
        InvocationHandler h = Proxy.getInvocationHandler(obj);
        // 获取 AnnotationInvocationHandler 的 memberValues 字段
        Field hField = h.getClass().getDeclaredField("memberValues");
        // 因为这个字段事 private final 修饰，所以要打开权限
        hField.setAccessible(true);
        // 获取 memberValues
        Map memberValues = (Map) hField.get(h);
        // 修改 value 属性值
        memberValues.put(fieldName, value);
    }
    public static String getRandomString(Integer len) {
        String result = "";
        for (int i = 0; i < len; i++) {
            result += getRandomChar();
        }
        return result;
    }

    private static String getRandomChar() {
        String str = "";
        int highPos; //
        int lowPos;


        Random random = new Random();

        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return new String(((Character) str.charAt(0)).toString().getBytes(), StandardCharsets.UTF_8);
    }


}
