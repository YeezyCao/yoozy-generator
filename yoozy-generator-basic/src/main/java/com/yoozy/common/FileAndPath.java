package com.yoozy.common;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static sun.invoke.util.Wrapper.isPrimitiveType;

public class FileAndPath {

    private static final YoozyLogger LOGGER = YoozyLogger.getLogger(FileAndPath.class.getName());

    static final Class<FileAndPath> CLAZZ = FileAndPath.class;

    static final AtomicInteger MAIN_COUNT = new AtomicInteger(0);

    static final AtomicInteger LAYER = new AtomicInteger(0);

    public static void main(String[] args) {
        print("args", Arrays.toString(args));
        /* printProperties(); */

        printMethods(CLAZZ);

        URL url = CLAZZ.getResource("");
        print("true", getPath(CLAZZ, true));
        print("false", getPath(CLAZZ, false));
        printMethods(url);

    }


    static <T> String getPath(Class<T> clazz, boolean pack) {
        URL resource;
        if (pack) {
            resource = clazz.getResource("");   //  /C:/../yoozy-generator/out/production/demo-projects/com/yoozy/
        } else {
            resource = clazz.getResource("/");  //  /C:/../yoozy-generator/out/production/demo-projects/
        }
        return resource.toString();
    }

    /**
     * 打印系统属性信息。
     * 它将系统所有的属性名称和对应的值收集起来，并以"名称 = 值"的形式打印出来。
     */
    static void printProperties() {
        Properties properties = System.getProperties();
        Set<String> names = properties.stringPropertyNames();
        ArrayList<String> list = new ArrayList<>();
        for (String name : names) {
            String property = properties.getProperty(name);
            list.add(name + " = " + property);
        }
        print("properties", list.toArray());
    }


    static <T> void print(T obj) {
        print("", obj);
    }

    static <T> void print(String message, Collection<T> obj) {
        print(message, obj.toArray());
    }

    static void print(String message, Number[] obj) {
        print(message, Arrays.toString(obj));
    }

    static String strformat(String str, int length) {
        return strformat(str, length, ' ', false);
    }

    static String strformat(String str, int length, char replace) {
        return strformat(str, length, replace, false);
    }

    static String strformat(String str, int length, char replace, boolean sign) {
        StringBuilder builder = new StringBuilder("%");
        if (!sign) {
            builder.append("-");
        }
        builder.append(length);
        builder.append("s");
        String format = builder.toString();
        return String.format(format, str).replace(' ', replace);
    }

    static <T> void error(String message, T... objs) {
        String level = strformat(">>>ERROR<<<", 13, ' ');
        print(level + message, objs);
    }

    static <T> void info(String message, T... objs) {
        String level = strformat(">>>INFO<<<", 13, ' ');
        print(level + message, objs);
    }

    @SafeVarargs
    static <T> void print(String message, T... objs) {
        StringBuilder args;
        if (objs.length <= 1) {
            args = new StringBuilder("%s%n");
        } else {
            args = new StringBuilder(strformat("", 100, '↓'));
            for (int i = 0; i < objs.length; i++) {
                args.append("%n%s");
            }
            args.append("%n");
            args.append(strformat("", 100, '↑'));
            args.append("%n%n");
        }
        // 获取方法调用次数
        String number = String.format("%-5s", String.format("[%s]", MAIN_COUNT.getAndAdd(1)));
        String str = number + message + " >>> " + args;
        String log = String.format(str, objs);
        // System.out.println(log);
        LOGGER.info(log);
    }


    /* 调用对象方法,打印方法名以及返回值。方法：无参; 返回值类型为String或基本数据类型 */
    public static <T> void printMethods(T obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        methods = Arrays.stream(methods).sorted(Comparator.comparing(Method::getName)).toArray(Method[]::new);
        for (Method method : methods) {
            // 判断方法参数是否为空
            if (method.getParameterCount() != 0) continue;
            if (method.getReturnType() == String.class          // 返回值类型是否为String
                    || isPrimitiveType(method.getReturnType())  // 返回值类型是否为基本数据类型
            ) {
                String methodName = method.getName();
                try {
                    Object result = method.invoke(obj);
                    info(methodName, result);
                } catch (Exception e) {
                    error(methodName, e.toString());
                }
            }
        }
    }

}
