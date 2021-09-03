package com.manman.rpc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Title: PackageScanUtils
 * @Author manman
 * @Description 扫描路径下所有的文件名的工具类
 * @Date 2021/8/26
 */

@Slf4j
public class PackageScanUtils {
    // 获取 main 方法中
    public static String getStackTrace() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();    // 获取方法栈的栈顶信息：RPCServer 的 main 方法
    }

    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        boolean recursive = true;      // 是否递归查找文件
        String packageDirName = packageName.replace('.', '/');  // 将包路径格式转为目录路径格式 com/manman/rpc
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件，并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // jar 包文件，定义一个 Jarfile
                    JarFile jar;
                    try {
                        // 获取 jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此 jar 中得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的循环
                        while (entries.hasMoreElements()) {
                            // 获取 jar 里的一个实体，可以是目录，和一些 jar 包里的其他文件，如 META-INF 等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是 / 开头
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以“/”结尾 是一个包
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '/');
                                }
                                // 如果可以迭代下去，并且是一个包
                                if ((idx != 1) || recursive) {
                                    // 如果是一个 .class 文件，而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的 ".class" 获取真正的类目
                                        String className = name.substring(packageDirName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到 classes
                                            classes.add(Class.forName(packageName + "." + className));
                                        } catch (ClassNotFoundException e) {
                                            log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String filePath, boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录，建立一个 File
        File dir = new File(filePath);
        // 如果不存在获取也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件，包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则，如果可以循环（包含子目录）或则是以 .class 结尾的文件（编译好的 java 类文件）
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory() || (file.getName().endsWith(".class")));
            }
        });
        // 循环所有文件
        for (File file : dirFiles) {
            // 如果是目录，则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是 java 类文件，去掉后面的 .class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                }
            }
        }
    }
}
