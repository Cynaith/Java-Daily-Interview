/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	FinalTest.java 模块说明： 修改历史： 2022/3/4 - liuyang - 创建。
 */
package com.ly.interview.JavaSE;

import jdk.internal.util.xml.impl.Pair;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author liuyang
 */

public class FinalTest {
  public static void main(String[] args) {

    BigDecimal zero = new BigDecimal(0);

    AtomicStampedReference<BigDecimal> atomicStampedReference = new AtomicStampedReference<BigDecimal>(zero, 0);

    atomicStampedReference.compareAndSet(zero, new BigDecimal(1), 0, 1);

    System.out.println(atomicStampedReference.getReference());
  }
}
