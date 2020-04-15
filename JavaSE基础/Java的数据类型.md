## Java的数据类型

### Java基本数据类型有哪些
|     |         |     |                             |
|-----|---------|-----|-----------------------------|
| 四类  | 八种      | 字节数 | 数据表示范围                      |
| 整型  | byte    | 1   | \-128～127                   |
| 整型  | short   | 2   | \-32768～32767               |
| 整型  | int     | 4   | \-2147483648～2147483647     |
| 整型  | long    | 8   | \-2<sup>63</sup>～2<sup>63</sup>\-1                |
| 浮点型 | float   | 4   | \-3\.403E38~3\.403E38       |
| 浮点型 | double  | 8   | \-1\.798E308~1\.798E308     |
| 字符型 | char    | 2   | 表示一个字符，如\('a'，'A'，'0'，'家'\) |
| 布尔型 | boolean | 1   | 只有两个值 true 与 false          |

### String是基本数据类型吗?
String 是引用类型，底层用 char 数组实现的。

### short s1 = 1; s1 = s1 + 1; 有错吗?short s1 = 1; s1 += 1;有错吗?
前者不正确，后者正确。对于 `shorts1=1;s1=s1+1;`由于1是 int 类型，因此 s1+1 运算结果也是 int 型， 
需要强制转换类型才能赋值给 short 型。而 `short s1 = 1; s1 += 1;`可以正确编译，因为 s1+= 1;
相当于 `s1 = (short)(s1 + 1);`其中有**隐含的强制类型转换** 。

### int 和 Integer 有什么区别?
Java 是一个近乎纯洁的面向对象编程语言，但是为了编程的方便还是引入了基本数据类型，
为了能够将这些基本数据类型当成对象操作，Java 为每一个基本数据类型都引入了对应的包装类型(wrapper class)，
int 的包装类就是 Integer，从 Java 5 开始引入了自动装箱/拆箱机制，使得二者可以相互转换。
```java
class AutoUnboxingTest{
    public static void main(String[] args){
        Integer a = new Integer(3);
        Integer b = 3; //将3自动装箱成Integer类型
        int c = 3;
        System.out.println(a==b); //false 两个引用没有引用同一对象
        System.out.println(a==c); //true a自动拆箱成int类型再和c比较
    }
}
```
> 如果整型字面量的值在-128 到 127 之间，那么不会 new 新的 Integer 对象，而是直接引用常量池 中的 Integer 对象

### 数据类型之间的转换
- 字符串如何转基本数据类型?
调用基本数据类型对应的包装类中的方法 parseXXX(String)或 valueOf(String)即可返回相应基本类型。
- 基本数据类型如何转字符串?
一种方法是将基本数据类型与空字符串(“”)连接(+)即可获得其所对应的字符串;另一种方法是调用 String类中的 valueOf()方法返回相应字符串。