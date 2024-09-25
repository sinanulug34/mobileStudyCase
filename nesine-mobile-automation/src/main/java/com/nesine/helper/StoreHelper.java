package com.nesine.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import com.nesine.model.ElementInfo;
import org.apache.log4j.Logger;

public enum StoreHelper {
    INSTANCE;
    Logger logger = org.apache.log4j.Logger.getLogger(getClass());
    private static final String DEFAULT_DIRECTORY_PATH = "element-values";
    ConcurrentMap<String, Object> elementMapList;

    StoreHelper() {
        initMap(getFileList());
    }

    private void initMap(File[] fileList) {
        elementMapList = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<ElementInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList = null;
        for (File file : fileList) {
            try {
                elementInfoList = gson
                        .fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream()
                        .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {
                logger.warn("{} not found", e);
            }
        }
    }

    private File[] getFileList() {
        File[] fileList = new File(
                this.getClass().getClassLoader().getResource(DEFAULT_DIRECTORY_PATH).getFile())
                .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}" +
                            DEFAULT_DIRECTORY_PATH);
            throw new NullPointerException();
        }
        return fileList;
    }

    public ElementInfo findElementInfoByKey(String key) {
        return (ElementInfo) elementMapList.get(key);
    }

}
