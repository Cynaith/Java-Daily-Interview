/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	CloneTest.java 模块说明： 修改历史： 2022/3/2 - liuyang - 创建。
 */
package com.ly.interview.JavaSE;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

/**
 * @author liuyang
 */

public class CloneTest {


  public static void main(String[] args) {
    Employee employee = new Employee("1","1","1","1","1","1");
    Employee beanUtils = new Employee();

    long time = System.currentTimeMillis();
    BeanUtils.copyProperties(employee, beanUtils);
    System.out.println(System.currentTimeMillis() - time);

    BeanCopier beanCopier = BeanCopier.create(Employee.class, Employee.class, true);


    Employee employeeBeanCopier = new Employee();
    time = System.currentTimeMillis();
    beanCopier.copy(employee, employeeBeanCopier, new EmployeeConverter());
    System.out.println(System.currentTimeMillis() - time);

  }


}

class EmployeeConverter implements Converter {

  @Override
  public Object convert(Object o, Class aClass, Object o1) {
    if (o instanceof String) {
      return o;
    }
    return null;
  }
}

class Employee {
  public Employee() {
  }

  public Employee(String a1, String a2, String a3, String a4, String a5, String a6) {
    this.a1 = a1;
    this.a2 = a2;
    this.a3 = a3;
    this.a4 = a4;
    this.a5 = a5;
    this.a6 = a6;
  }

  private String a1;
  private String a2;
  private String a3;
  private String a4;
  private String a5;
  private String a6;

  public String getA1() {
    return a1;
  }

  public void setA1(String a1) {
    this.a1 = a1;
  }

  public String getA2() {
    return a2;
  }

  public void setA2(String a2) {
    this.a2 = a2;
  }

  public String getA3() {
    return a3;
  }

  public void setA3(String a3) {
    this.a3 = a3;
  }

  public String getA4() {
    return a4;
  }

  public void setA4(String a4) {
    this.a4 = a4;
  }

  public String getA5() {
    return a5;
  }

  public void setA5(String a5) {
    this.a5 = a5;
  }

  public String getA6() {
    return a6;
  }

  public void setA6(String a6) {
    this.a6 = a6;
  }
}