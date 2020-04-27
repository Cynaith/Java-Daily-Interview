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
### 增加元素
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
    检查是否扩容:
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
    2. 如果所需最小容量 > 数组长度; 调用grow()方法扩容
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
  
  
  

