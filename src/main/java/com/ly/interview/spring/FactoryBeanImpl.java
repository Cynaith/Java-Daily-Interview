/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	FactoryBeanImpl.java 模块说明： 修改历史： 2022/3/7 - liuyang - 创建。
 */
package com.ly.interview.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 */
@Component
public class FactoryBeanImpl implements FactoryBean<User> {

  @Override
  public boolean isSingleton() {
    return FactoryBean.super.isSingleton();
  }

  @Override
  public User getObject() throws Exception {
    return new User();
  }

  @Override
  public Class<?> getObjectType() {
    return User.class;
  }
}