## JavaSE常用API
### Math
Math.round(11.5) 返回 12，Math.round(-11.5) 返回 -11
### switch
Java5以前switch(expr)中，expr只能是 byte、short、char、int。
<br/>
从 Java 5 开始，Java 中引入了枚举类型， expr 也可以是 enum 类型。
<br/>
从 Java 7 开始，expr 还可以是字符串(String)，但是长整型(long)在目前所有的版本中都是不可以的。

### 数组有没有 length() 方法?String 有没有 length() 方法?
数组没有 length()方法，而是有 length 的属性。
<br/>
String 有 length()方法。
> JavaScript 中，获得字符串的长度是 通过 length 属性得到的，这一点容易和 Java 混淆。

### String StringBuilder StringBuffer的区别?
- String是只读字符串
```java
String str = “abc”;
 str = “bcd”;
```
str仅仅是一个引用对象，它指向一个字符串对象“abc”。
第二行代码的含义是让 str 重新指向了一个新的字符串“bcd”对象，而“abc”对象并没有任何改变，只不过该对象已经成为一个不可及对象罢了。
- StringBuffer/StringBuilder 表示的字符串对象可以直接进行修改。
> StringBuilder 是 Java5 中引入的，它和 StringBuffer 的方法完全相同，
> 区别在于它是在单线程环境下使用的， 因为它的所有方法都没有被 synchronized 修饰，
> 因此它的效率理论上也比 StringBuffer 要高。

### 什么情况下用“+”运算符进行字符串连接比调用 StringBuffer/StringBuilder 对象的 append 方法连接字符串性能更好?
> 华为面试题

在 Java 中无论使用何种方式进行字符串连接，实际上都使用的是 StringBuilder。
- 从运行结果来看是相同的
- 从运行效率和资源消耗方面看，有很大区别
    - 对于循环中执行字符串连接时，每执行一次循环，就会创建一个 StringBuilder 对象。
    > 在使用 StringBuilder 时要注意，尽量不要"+"和 StringBuilder 混着用，否则会创建更多的 StringBuilder 对 象
    
### 请说出下面程序的输出
```java
class StringEqualTest {
  public static void main(String[] args) {
        String s1 = "Programming";
        String s2 = new String("Programming"); String s3 = "Program";
        String s4 = "ming";
        String s5 = "Program" + "ming";
        String s6 = s3 + s4;  
        System.out.println(s1 == s2);   //false
        System.out.println(s1 == s5);   //true
        System.out.println(s1 == s6);   //false 
        System.out.println(s1 == s6.intern());  //true
        System.out.println(s2 == s2.intern());  //false
    }
}
```
1. String 对象的 intern()方法会得到字符串对象在常量池中对应的版本的引用(如果常量池中有一个字符串与 String 对象的 equals 结果是 true)，
如果常量池中没有对应的字符串，则该字符串将被添加到常量池中，然后返回常量池中字符串的引用。
2. 字符串的+操作其本质是创建了 StringBuilder 对象进行 append 操作，然后将拼接后的 StringBuilder 对 象用 toString 方法处理成 String 对象。

### 如何取得年月日、小时分钟秒?
```java
public class DateTimeTest {
    public static void main(String[] args) {
    Calendar cal = Calendar.getInstance();
    System.out.println(cal.get(Calendar.YEAR));
    System.out.println(cal.get(Calendar.MONTH)); // 0 - 11
    System.out.println(cal.get(Calendar.DATE));
    System.out.println(cal.get(Calendar.HOUR_OF_DAY));
    System.out.println(cal.get(Calendar.MINUTE));
    System.out.println(cal.get(Calendar.SECOND));
    // Java 8
    LocalDateTime dt = LocalDateTime.now();
    System.out.println(dt.getYear());
    System.out.println(dt.getMonthValue()); // 1 - 12
    System.out.println(dt.getDayOfMonth());
    System.out.println(dt.getHour());
    System.out.println(dt.getMinute());
    System.out.println(dt.getSecond()); 
    }
}
```

### 如何取得从 1970 年 1 月 1 日 0 时 0 分 0 秒到现在的毫秒数?
1. `Calendar.getInstance().getTimeInMillis();`
2. `System.currentTimeMillis();`
3. `Clock.systemDefaultZone().millis(); //java8`

### 如何取得某月的最后一天?
```java
    //Java 8
    LocalDate today = LocalDate.now();
    //本月的第一天
    LocalDate firstday = LocalDate.of(today.getYear(),today.getMonth(),1);
    //本月的最后一天
    LocalDate lastDay =today.with(TemporalAdjusters.lastDayOfMonth());
    System.out.println("本月的第一天"+firstday); 
    System.out.println("本月的最后一天"+lastDay);
```

### 如何格式化日期?
- Java.text.DataFormat 的子类(如 SimpleDateFormat 类)中的 format(Date)方法可将日期格式化
```java
    SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy/MM/dd"); 
    Date date1 = new Date();
    System.out.println(oldFormatter.format(date1));
```
- Java 8 中可以用 java.time.format.DateTimeFormatter 来格式化时间日期
```java
    // Java 8
    DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); 
    LocalDate date2 = LocalDate.now(); 
    System.out.println(date2.format(newFormatter));
```
> Java 的时间日期 API 一直以来都是被诟病的东西，为了解决这一问题，Java 8 中引入了新的时间日期 API，
>其中包括 LocalDate、LocalTime、LocalDateTime、Clock、Instant 等类，这些的类的设计都使用了不变模式，因 此是线程安全的设计。

### Java8日期/时间特性
> Java 8 日期/时间 API 是 JSR-310 的实现，它的实现目标是克服旧的日期时间实现中所有的缺陷

设计原则为
- **不变性:** 新的日期/时间 API 中，所有的类都是不可变的，这对多线程环境有好处。
- **关注点分离:** 新的API将人可读的日期时间和机器时间(unixtimestamp)明确分离，它为日期(Date)、时间
  (Time)、日期时间(DateTime)、时间戳(unix timestamp)以及时区定义了不同的类。
- **清晰:** 在所有的类中，方法都被明确定义用以完成相同的行为。举个例子，要拿到当前实例我们可以使用now()方 法，在所有的类中都定义了 format()和 parse()方法，而不是像以前那样专门有一个独立的类。为了更好的处理问
  题，所有的类都使用了工厂模式和策略模式，一旦你使用了其中某个类的方法，与其他类协同工作并不困难。
- **实用操作:** 所有新的日期/时间API类都实现了一系列方法用以完成通用的任务，如:加、减、格式化、解析、从
  日期/时间中提取单独部分，等等。
- **可扩展性:** 新的日期/时间API是工作在ISO-8601日历系统上的，但我们也可以将其应用在非ISO的日历上。