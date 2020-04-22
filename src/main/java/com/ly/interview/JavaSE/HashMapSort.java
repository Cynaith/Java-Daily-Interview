package com.ly.interview.JavaSE;

import java.util.*;

/**
 * @USER: lynn
 * @DATE: 2020/4/18
 **/
public class HashMapSort {
    public static void main(String[] args) {
        HashMap<Integer,User> users = new HashMap<>();
        users.put(1,new User("张三",24));
        users.put(2,new User("李四",25));
        users.put(3,new User("王五",26));
        users.put(4,new User("李六",27));
        System.out.println(users.toString());
        HashMap<Integer,User> usersSorted = sort(users);
        System.out.println(usersSorted);
    }

    public static HashMap<Integer,User> sort(HashMap<Integer,User> userHashMap){
        Set<Map.Entry<Integer,User>> entrySet = userHashMap.entrySet();
        List<Map.Entry<Integer,User>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Map.Entry<Integer, User>>() {
            @Override
            public int compare(Map.Entry<Integer, User> o1, Map.Entry<Integer, User> o2) {
                System.out.println("o1 :"+o1.toString()+";o2 :"+o2.toString());
                return o2.getValue().age-o1.getValue().age;
            }
        });
        LinkedHashMap<Integer,User> linkedHashMap = new LinkedHashMap<Integer, User>();
        for (Map.Entry<Integer,User> entry:list){
            linkedHashMap.put(entry.getKey(),entry.getValue());
        }
        return linkedHashMap;
    }
}

class User{
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }



    String name;
    int age;

    @Override
    public String toString() {
        return "User{" +
                "name=" + name +
                ", age=" + age +
                '}';
    }
}
