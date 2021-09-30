package com.littlejenny;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageUtils {

    public static List<String> jarDoScan(String basePackage) throws IOException {
        return new JarPackageScanner().doScan(basePackage);
    }
    public static List<String> jarDoScan(String basePackage,ClassLoader cl) throws IOException {
        return new JarPackageScanner().doScan(basePackage,cl);
    }
}

interface PackageScanner {
    /**
     * @return 返回指定包下所有的全類名
     */
    List<String> doScan(String basePackage, ClassLoader cl) throws IOException;
    List<String> doScan(String basePackage) throws IOException;

}

@Slf4j
class JarPackageScanner implements PackageScanner {
    public JarPackageScanner() {

    }
    @Override
    public List<String> doScan(String basePackage, ClassLoader cl) throws IOException {
        log.debug("正在掃描 {}",basePackage);
        String slashPath = StringUtils.dotToSlash(basePackage);
        log.debug("正斜線類名 {}",slashPath);
        URL url = cl.getResource(slashPath);
        log.debug("classLoader取得該包在 {} ",url);
        String filePath = StringUtils.getJarRootPath(url);
        log.debug("包的實際路徑{}",filePath);

        return readFromJarFile(filePath,slashPath);
    }
    @Override
    public List<String> doScan(String basePackage) throws IOException {
        return doScan(basePackage,PackageUtils.class.getClassLoader());
    }
    /**
     *
     * @param jarPath JAR包實際路徑
     * @param slashPackageName 查詢的包路徑
     * @return 符合包路徑下的所有Class
     * @throws IOException
     */
    private List<String> readFromJarFile(String jarPath, String slashPackageName) throws IOException {
        log.debug("從{}中讀取",jarPath);
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry;
        List<String> nameList = new ArrayList<>();
        while ((entry = jarIn.getNextJarEntry()) != null) {
            String name = entry.getName();
            log.debug("文件 {}",name);
            if (StringUtils.isClassFile(name) && name.contains(slashPackageName)) {
                name = StringUtils.trimExtension(name);
                name = name.substring(name.indexOf(slashPackageName));
                name = name.replaceAll("/",".");
                log.debug("符合條件的Class {}",name);
                nameList.add(name);
            }
        }
        return nameList;
    }
}
