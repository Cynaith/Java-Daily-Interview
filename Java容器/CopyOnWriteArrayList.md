## CopyOnWriteArrayList
1. ArrayList不是线程安全的
2. Vector在方法体上加synchronized关键字
3. Collections.synchronizedList(new ArrayList()) 在方法内部添加synchronized关键字

### 使用
- 底层使用复制数组方式实现
- 遍历时不会抛出ConcurrentModificationException异常，不用加锁
- 元素可以为null
- 可重入锁对象ReentrantLock

### add
```java
public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            //复制数组
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            //添加至新数组尾部
            newElements[len] = e;
            //替换数组
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }
```

### set
```java
public E set(int index, E element) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                //检查是否越界
                rangeCheck(index);
                checkForComodification();
                E x = l.set(index+offset, element);
                expectedArray = l.getArray();
                return x;
            } finally {
                lock.unlock();
            }
        }
```
### 迭代器
为什么容器遍历不抛出异常？
<br/>
操作的是原数组
### 缺点
- 内存占用
    - 每一次add()、set()、remove()操作会重新创建数组
- 数据一致性
    - 只保证最终一致、不保证过程一致