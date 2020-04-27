# ArrayList、LinkedList
## ArrayList
### 初始化
- 支持快速访问
    ```java
        public class ArrayList<E> extends AbstractList<E>
                       implements List<E>, RandomAccess, Cloneable, java.io.Serializable
    ```
  RandomAccess接口表示该类支持快速随机访问
- 默认容量
    ```java
        /**
        * Default initial capacity.
        */
       private static final int DEFAULT_CAPACITY = 10;
    ```
    初始化大小10
### 增加元素(add())
- add(E e)
    
    ```java
        public boolean add(E e) {
           ensureCapacityInternal(size + 1);  // Increments modCount!!
           elementData[size++] = e;
           return true;
       }
    ```
    1. 先通过ensureCapacityInternal()方法检查是否需要扩容
    2. 在结尾插入元素
    <br/>
    检查是否扩容
  ```java
      private void ensureCapacityInternal(int minCapacity) {
          ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
      }
      private void ensureExplicitCapacity(int minCapacity) {
          modCount++;
          // overflow-conscious code
          if (minCapacity - elementData.length > 0)
          grow(minCapacity);
      }
  ```
    1. 使用calculateCapacity()计算所需最小容量
    2. 如果所需最小容量 > 数组长度; 调用grow()方法扩容<br/>
    ```java
      private void grow(int minCapacity) {
          int oldCapacity = elementData.length;
          int newCapacity = oldCapacity + (oldCapacity >> 1);
          if (newCapacity - minCapacity < 0)
              newCapacity = minCapacity;
          if (newCapacity - MAX_ARRAY_SIZE > 0)
              newCapacity = hugeCapacity(minCapacity);
          // minCapacity is usually close to size, so this is a win:
             elementData = Arrays.copyOf(elementData, newCapacity); 
      }
    ```
    1. 扩容1.5倍
    2. 调用Arrays.copyOf()方法 
    ```java
          /**
         * 复制指定的数组，截断或填充空值（如果需要），使副本具有指定的长度。
         * 对于在原始数组和副本中都有效的所有索引，这两个数组将包含相同的值。
         *  对于在副本中有效但不是原始索引的任何索引，副本将包含<tt>null</tt>。
         *  当且仅当指定的长度大于原始数组的长度时，才存在此类索引。
         *  生成的数组属于新类型。
         *
         * @param <U> 原始数组中对象的类
         * @param <T> 返回数组中对象的类
         * @param original 原始要复制的数组
         * @param newLength 要返回的副本的长度
         * @param newType 要返回的副本的类
         * @return a copy of the original array, truncated or padded with nulls
         *     to obtain the specified length
         * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
         * @throws NullPointerException if <tt>original</tt> is null
         * @throws ArrayStoreException if an element copied from
         *     <tt>original</tt> is not of a runtime type that can be stored in
         *     an array of class <tt>newType</tt>
         * @since 1.6
         */
        public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
            @SuppressWarnings("unchecked")
            T[] copy = ((Object)newType == (Object)Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
            System.arraycopy(original, 0, copy, 0,
                             Math.min(original.length, newLength));
            return copy;
        }
    ```
### 获取元素(get())
### 更新元素(set())
### 删除元素(remove())
### Vector与ArrayList区别

---
## LinkedList
> 底层是双向链表
### 初始化
- 实现了Deque接口
    ```java
    public class LinkedList<E>
        extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Cloneable, java.io.Serializable{
          ...
      }
    ```
  Deque接口方法:
  <br/>
  插入元素
  1. addFirst() 
  2. addLast()
  3. offerFirst() //如果插入元素为空, 不返回NullPointException,返回false
  4. offerLast() //如果插入元素为空, 不返回NullPointException,返回false
  <br/>
  删除元素
  5. removeFirst()
  6. removeLast()
  7. pollFist() //如果队列无元素,不返回NoSuchElementException异常,返回null
  8. pollLast() //如果队列无元素,不返回NoSuchElementException异常,返回null
  <br/>
  获取元素
  9. getFirst()
  10. getLast()
  11. peekFirst() //如果队列无元素,不返回NoSuchElementException异常,返回null
  12. peekLast() //如果队列无元素,不返回NoSuchElementException异常,返回null
  <br/>
  栈操作
  13. pop() 相当于`removeFirst();`
  14. push() 相当于`addFirst(e);`
### 增加元素(add())
- add(E e)
    ```java
    public boolean add(E e) {
            linkLast(e);
            return true;
        }
    void linkLast(E e) {
            final Node<E> l = last;
            final Node<E> newNode = new Node<>(l, e, null);
            last = newNode;
            if (l == null)
                first = newNode;
            else
                l.next = newNode;
            size++;
            modCount++;
        }
    ```
  1. new 一个新Node节点
  2. 设置LinkedList实例的last节点为新节点
  3. 判断是否为空后插入LinkedList
  
### 获取元素(get())
- get(int index)
    ```java
    public E get(int index) {
            checkElementIndex(index);
            return node(index).item;
        }
      private void checkElementIndex(int index) {
          if (!isElementIndex(index))
              throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
      }
    ```
  1. 首先判断是否越界,如果越界抛出IndexOutBoundException异常
  2. 通过node(index).item返回index处的元素
    ```java
    Node<E> node(int index) {
            // assert isElementIndex(index);
    
            if (index < (size >> 1)) {
                Node<E> x = first;
                for (int i = 0; i < index; i++)
                    x = x.next;
                return x;
            } else {
                Node<E> x = last;
                for (int i = size - 1; i > index; i--)
                    x = x.prev;
                return x;
            }
        }
    ```
  1. 判断index位于LinkedList的前半/后半(size>>1 : size/2)
  2. 从first/last节点遍历
  > 为什么是 node(index).item?
    ```java
     private static class Node<E> {
             E item;
             Node<E> next;
             Node<E> prev;
     
             Node(Node<E> prev, E element, Node<E> next) {
                 this.item = element;
                 this.next = next;
                 this.prev = prev;
             }
         }
    ```
### 更新元素(set())
- set(int index,E element)
    ```java
    public E set(int index, E element) {
            checkElementIndex(index);
            Node<E> x = node(index);
            E oldVal = x.item;
            x.item = element;
            return oldVal;
        }
    ```
  与get方法类似
### 删除元素(remove())
- remove(Object o)
    ```java
    public boolean remove(Object o) {
            if (o == null) {
                for (Node<E> x = first; x != null; x = x.next) {
                    if (x.item == null) {
                        unlink(x);
                        return true;
                    }
                }
            } else {
                for (Node<E> x = first; x != null; x = x.next) {
                    if (o.equals(x.item)) {
                        unlink(x);
                        return true;
                    }
                }
            }
            return false;
        }
  E unlink(Node<E> x) {
          // assert x != null;
          final E element = x.item;
          final Node<E> next = x.next;
          final Node<E> prev = x.prev;
  
          if (prev == null) {
              first = next;
          } else {
              prev.next = next;
              x.prev = null;
          }
  
          if (next == null) {
              last = prev;
          } else {
              next.prev = prev;
              x.next = null;
          }
  
          x.item = null;
          size--;
          modCount++;
          return element;
      }
    ```
  1. 遍历Node节点
  2. 使用unlink(),将node.pre 与 node.next连接
### Vector与ArrayList区别
- Vector 线程安全 ;ArrayList 线程不安全
> ArrayList可以使用`Collections.synchronizedList(new ArrayList())`实现线程安全
- ArrayList 扩容 0.5倍 ; Vector 扩容1倍

## 面试题

  
  
  

