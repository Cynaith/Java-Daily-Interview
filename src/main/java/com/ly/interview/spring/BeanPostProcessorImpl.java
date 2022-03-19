/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	BeanPostProcessor.java 模块说明： 修改历史： 2022/3/7 - liuyang - 创建。
 */
package com.ly.interview.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 */

@Component
public class BeanPostProcessorImpl implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("bean = " + bean);
    return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("bean = " + bean);
    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }

  public static void main(String[] args) {
    int i = 0;
    while (true) {
      i++;
    }
  }
}