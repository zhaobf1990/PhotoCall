package com.zhaobf.phonecall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhaobf.phonecall.model.Person;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取和保存联系人信息
 * Created by zhaobf on 2018/2/14.
 */

public class PersonUtils {


    public static List<Person> getAllPerson() {
        File phoneFile = TgPictureUtil.getPhoneFile();
        StringBuilder stringBuilder = FileUtils.readFile(phoneFile.getPath(), "UTF-8");
        List<Person> persons;
        if (StringUtils.isEmpty(stringBuilder.toString())) {
            persons = new ArrayList<>();
        } else {
            persons = JSON.parseObject(stringBuilder.toString(), new TypeReference<List<Person>>() {
            });
        }
        Collections.sort(persons);
        return persons;
    }

    public static void saveAllPerson(List<Person> persons) {
        String json = JSON.toJSONString(persons);
        File phoneFIle = TgPictureUtil.getPhoneFile();
        FileUtils.writeFile(phoneFIle.getPath(), json);
    }
}
