## ArrayList
### 初始化
- `public class ArrayList<E> extends AbstractList<E>
                       implements List<E>, RandomAccess, Cloneable, java.io.Serializable`
    - RandomAccess接口表示该类支持快速随机访问
- `/**
        * Default initial capacity.
        */
       private static final int DEFAULT_CAPACITY = 10;`
    - 初始化大小10
### 增加元素
- `public boolean add(E e) {
           ensureCapacityInternal(size + 1);  // Increments modCount!!
           elementData[size++] = e;
           return true;
       }`

