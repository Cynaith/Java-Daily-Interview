/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	BeanFactoryProcessorImpl.java 模块说明： 修改历史： 2022/3/7 - liuyang -
 * 创建。
 */
package com.ly.interview.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 */

@Component
public class BeanFactoryProcessorImpl implements BeanFactoryPostProcessor {
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    System.out.println("configurableListableBeanFactory = " + configurableListableBeanFactory.toString());
  }
}