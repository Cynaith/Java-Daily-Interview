/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	User.java 模块说明： 修改历史： 2022/3/7 - liuyang - 创建。
 */
package com.ly.interview.spring;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author liuyang
 */


public class User implements BeanNameAware, InitializingBean {

  private String beanName;

  public String getBeanName() {
    return beanName;
  }

  @Override
  public void setBeanName(String s) {
    this.beanName = s;
    System.out.println("s = " + s);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("User.afterPropertiesSet");
  }
}