## Java的集合 · 上
### HashMap排序题
已知一个 HashMap<Integer，User>集合， User 有 name(String)和 age(int)属性。
请写一个方法实现对 HashMap 的排序功能，该方法接收 HashMap<Integer，User>为形参，返回类型为 HashMap<Integer，User>， 
要求对 HashMap 中的 User 的 age 倒序进行排序。排序时 key=value 键值对不得拆散。
```java
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
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

### 请问 ArrayList、HashSet、HashMap 是线程安全的吗?如果不是我想要线程安全的集合怎么办?
我们都看过上面那些集合的源码(如果没有那就看看吧)，每个方法都没有加锁，显然都是线程不安全的。话又说 过来如果他们安全了也就没第二问了。
<br/>
集合中Vector和HashTable是线程安全的，实质上是核心方法添加上了synchronized关键字。
<br/>
Collections工具类提供了相关API，可以让上面3个不安全的集合变为安全的。
```java
Collection.synchronizedCollection(c)
Collection.synchronizedList(list)
Collection.synchronizedMap(map)
Collection.synchronizedSet(set)
```

### ArrayList内部用什么实现的
> ArrayList内部是用Object[]实现的

- 构造函数
    - 空参构造
    ```java
    /**
    * Constructs a new {@code ArrayList} instance with zero initial capacity. 
    */
    public ArrayList() {
    array = EmptyArray.OBJECT;
    }
    ```
    array 是一个 Object[]类型。当我们 new 一个空参构造时系统调用了EmptyArray.OBJECT 属性，EmptyArray 仅 仅是一个系统的类库，该类源码如下:
    ```java
    public final class EmptyArray {
      private EmptyArray() {}
      public static final boolean[] BOOLEAN = new boolean[0]; public static final byte[] BYTE = new byte[0];
      public static final char[] CHAR = new char[0];
      public static final double[] DOUBLE = new double[0]; public static final int[] INT = new int[0];
      public static final Class<?>[] CLASS = new Class[0];
      public static final Object[] OBJECT = new Object[0];
      public static final String[] STRING = new String[0];
      public static final Throwable[] THROWABLE = new Throwable[0];
      public static final StackTraceElement[] STACK_TRACE_ELEMENT = new StackTraceElement[0];
    }
    ```
  当new一个空参ArrayList时，系统内部使用了一个new Object[0]数组
  - 带参构造1
  ```java
    /**
    * Constructs a new instance of {@code ArrayList} with the specified * initial capacity.
    *
    * @param capacity
    * the initial capacity of this {@code ArrayList}.
     */  
  public ArrayList(int capacity) {
       if (capacity < 0) {
           throw new IllegalArgumentException("capacity < 0: " + capacity);
       }
      array = (capacity == 0 ? EmptyArray.OBJECT : new Object[capacity]); 
  }
  ```
  该构造函数传入一个 int 值，该值作为数组的长度值。如果该值小于 0，则抛出一个运行时异常。
  如果等于 0，则 使用一个空数组，如果大于 0，则创建一个长度为该值的新数组。
  - 带参构造2
  ```java
    /**
        * Constructs a new instance of {@code ArrayList} containing the elements of
    * the specified collection.
    *
    * @param collection
    * the collection of elements to add. */
    public ArrayList(Collection<? extends E> collection) { 
          if (collection == null) {
              throw new NullPointerException("collection == null"); 
          }
              Object[] a = collection.toArray(); 
          if (a.getClass() != Object[].class) {
              Object[] newArray = new Object[a.length]; 
              System.arraycopy(a, 0, newArray, 0, a.length); 
              a = newArray;
          }
          array = a;
          size = a.length;
    }
  ```
  如果调用构造函数的时候传入了一个 Collection 的子类，那么先判断该集合是否为 null，为 null 则抛出空指针异常。
  如果不是则将该集合转换为数组 a，然后将该数组赋值为成员变量 array，将该数组的长度作为成员变量 size。
  这里面它先判断 a.getClass 是否等于 Object[].class，其实一般都是相等的，我也暂时没想明白为什么多加了这个判断， 
  toArray 方法是 Collection 接口定义的，因此其所有的子类都有这样的方法，list 集合的 toArray 和 Set 集合的 toArray 返回的都是 Object[]数组。 

- add方法
```java
    /**
    * Adds the specified object at the end of this {@code ArrayList}. *
    * @param object
    * the object to add.
    * @return always true
    */
    @Override public boolean add(E object) { Object[] a = array;
    int s = size;
    if (s == a.length) {
    Object[] newArray = new Object[s +
    (s < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : s >> 1)];
    System.arraycopy(a, 0, newArray, 0, s);
    array = a = newArray; }
    a[s] = object; size = s + 1; modCount++; return true;
    }
```
1. 首先将成员变量 array 赋值给局部变量 a，将成员变量 size 赋值给局部变量 s。
2. 判断集合的长度 s 是否等于数组的长度(如果集合的长度已经等于数组的长度了，说明数组已经满了，该重新 分配新数组了)，
重新分配数组的时候需要计算新分配内存的空间大小，如果当前的长度小于 MIN_CAPACITY_INCREMENT/2(这个常量值是 12，除以 2 就是 6，
也就是如果当前集合长度小于 6)则分配 12 个长度，如果集合长度大于 6 则分配当前长度 s 的一半长度。这里面用到了三元运算符和位运算，s >> 1，
意思就是将 s 往右移 1 位，相当于 s=s/2，只不过位运算是效率最高的运算。
3. 将新添加的 object 对象作为数组的 a[s]个元素。
4. 修改集合长度 size 为 s+1
5. modCotun++,该变量是父类中声明的，用于记录集合修改的次数，记录集合修改的次数是为了防止在用迭代器迭代集合时避免并发修改异常，或者说用于判断是否出现并发修改异常的。
6. return true，这个返回值意义不大，因为一直返回 true，除非报了一个运行时异常。

- remove 方法
```java
/**
* Removes the object at the specified location from this list. *
* @param index
* the index of the object to remove.
* @return the removed object.
* @throws IndexOutOfBoundsException
* when {@code location < 0 || location >= size()} */
@Override public E remove(int index) { 
    Object[] a = array;
    int s = size;
    if (index >= s) {
       throwIndexOutOfBoundsException(index, s);
    }
    @SuppressWarnings("unchecked")
    E result = (E) a[index];
    System.arraycopy(a, index + 1, a, index, --s - index);
     a[s] = null; // Prevent memory leak
    size = s;
    modCount++;
    return result;
}
```
1. 先将成员变量 array 和 size 赋值给局部变量 a 和 s。
2. 判断形参 index 是否大于等于集合的长度，如果成了则抛出运行时异常
3. 获取数组中脚标为 index 的对象 result，该对象作为方法的返回值
4. 调用 System 的 arraycopy 函数(将数组中index对象删除后，index后对象前移)
5. 接下来就是很重要的一个工作，因为删除了一个元素，而且集合整体向前移动了一位，因此需要将集合最后一 个元素设置为 null，否则就可能内存泄露。
6. 重新给成员变量 array 和 size 赋值
7. 记录修改次数
8. 返回删除的元素

- clear 方法
```java
/**
* Removes all elements from this {@code ArrayList}, leaving it empty. *
* @see #isEmpty
* @see #size
 */
@Override public void clear() {
    if (size != 0) {
    Arrays.fill(array, 0, size, null); 
    size = 0;
    modCount++;
    }
}
```
如果集合长度不等于 0，则将所有数组的值都设置为 null，然后将成员变量 size 设置为 0 即可，最后让修改记录 加 1。







