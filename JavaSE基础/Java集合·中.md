## Java集合·中
### List三个子类的特点
- ArrayList 底层结构是数组,底层查询快,增删慢。
- LinkedList 底层结构是链表型的,增删快,查询慢。
- Vector 底层结构是数组 线程安全的,增删慢,查询慢。
### List 、 Map、Set
- List 接口有三个实现类
    - **LinkedList:** 基于链表实现，链表内存是散乱的，每一个元素存储本身内存地址的同时还 存储下一个元素的地址。链表增删快，查找慢.
    - **ArrayList:** 基于数组实现，非线程安全的，效率高，便于索引，但不 便于插入删除.
    - **Vector:** 基于数组实现，线程安全的，效率低。

- Map 接口有三个实现类
    - **HashMap:** 基于 hash 表的 Map 接口实现，非线程安全，高效，支持 null 值和 null 键。
    - **HashTable:** 线程安全，低效，不支持 null 值和 null 键。
    - **LinkedHashMap:** 是 HashMap 的一个子类，保存了记录的插入顺序。
    - **SortMap 接口:** TreeMap，能够把它保存的记录根据键排序，默认是键值的升序排序。
- Set 接口有两个实现类
    - **HashSet:** 底层是由 HashMap 实现，不允许集合中有重复的值，使用该方式时需要重写 equals()和 hashCode()方法。
    - **LinkedHashSet:** 继承与 HashSet，同时又基于 LinkedHashMap 来进行实现，底层使用的是 LinkedHashMp。
###  ArrayList 和 Linkedlist 区别?
LinkedList 使用了循环双向链表数据结构。LinkedList 链表由一系列表项连接而成。一个表项总是包含 3 个部分:元素内容，前驱表和后驱表。
无论 LikedList 是否 为空，链表内部都有一个 header 表项，它既表示链表的开始，也表示链表的结尾。

### List a=new ArrayList()和 ArrayList a =new ArrayList()的区别?
List list = new ArrayList();这句创建了一个 ArrayList 的对象后把上溯到了 List。
此时它是一个 List 对象了，有些 ArrayList 有但是 List 没有的属性和方法，它就不能再用了。
而 ArrayList list=new ArrayList();创建一对象则保留了 ArrayList 的所有属性。 
所以需要用到 ArrayList 独有的方法的时候不能用前者。实例代码如下:
```java
List list = new ArrayList();
ArrayList arrayList = new ArrayList(); 
list.trimToSize(); //错误，没有该方法。
arrayList.trimToSize(); //ArrayList里有该方法。
```
### 要对集合更新操作时，ArrayList 和 LinkedList 哪个更适合?
1. 如果集合数据是对于集合随机访问 get 和 set，ArrayList 绝对优于 LinkedList，因为 LinkedList 要移动指针。
2. 如果集合数据是对于集合新增和删除操作 add 和 remove，LinedList 比较占优势，因为 ArrayList 要移动数
   据。
> ArrayList 的空间浪费主要体现在在 list 列表的结尾预留一定的容量空间，而 LinkedList 的空间花费则体现在
> 它的每一个元素都需要消耗相当的空间

当操作是在一列数据的后面添加数据而不是在前面或中间,并且需要随机地访问其中的元素时,使用 ArrayList 会提供比较好的性能;
当你的操作是在一列数据的前面或中间添加或删除数据,并且按照顺序访问其中的元素时,就应该使用 LinkedList 了。

### Map 中的 key 和 value 可以为 null 么?
- HashMap 对象的 key、value 值均可为 null。
- HahTable 对象的 key、value 值均不可为 null。