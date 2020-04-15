## Java面向对象

### 面向对象特性
#### 1.继承
继承是从已有类得到继承信息创建新类的过程。提供继承信息的类被称为父类(超类、基类);
得到继承信息的类被称为子类(派生类)。继承让变化中的软件系统有了一定的延续性，同时继承也是封装程序中可变因素的 重要手段。
#### 2.封装
通常认为封装是把数据和操作数据的方法绑定起来，对数据的访问只能通过已定义的接口。
面向对象 的本质就是将现实世界描绘成一系列完全自治、封闭的对象。我们在类中编写的方法就是对实现细节的一种封装;
我们编写一个类就是对数据和数据操作的封装。可以说，封装就是隐藏一切可隐藏的东西，只向外界提供最简单的编程接口。
#### 3.多态性
多态性是指允许不同子类型的对象对同一消息作出不同的响应。
简单的说就是用同样的对象引用调用同样的方法但是做了不同的事情。多态性分为编译时的多态性和运行时的多态性。
如果将对象的方法视为对象向外 界提供的服务，那么运行时的多态性可以解释为:当A系统访问B系统提供的服务时，B系统有多种提供服务的方式，但一切对A系统来说都是透明的。
方法重载(overload)实现的是编译时的多态性(也称为前绑定)，而方法重写 (override)实现的是运行时的多态性(也称为后绑定)。运行时的多态是面向对象最精髓的东西，要实现多态需要做 两件事:1. 方法重写(子类继承父类并重写父类中已有的或抽象的方法);2. 对象造型(用父类型引用引用子类型对 象，这样同样的引用调用同样的方法就会根据子类对象的不同而表现出不同的行为)。
#### 4.抽象
抽象是将一类对象的共同特征总结出来构造类的过程，包括数据抽象和行为抽象两方面。抽象只关注对 象有哪些属性和行为，并不关注这些行为的细节是什么。
### 如何理解clone
> 有时可能需要一个和A完全相同的新对象B 并且此后对B的任何改动都不会影响到A中的值。
#### 1.new和clone的过程区别
new 操作符本意是分配内存。程序执行到new 操作符时，首先去看new操作符后的类型，因为知道了类型才知道分配多大的内存空间。
分配完之后，调用构造函数，填充对象的各个域，这一步叫对象的初始化。构造方法返回后，一个对象创建完成，可以把他的引用（地址）发布到外部，在外部就可以使用这个引用操纵这个对象。
<br/>
clone第一步与new相似，都是分配内存，调用clone方法时，分配的内存和原对象相同，然后使用原对象的域填充新对象的域，
填充完成之后，clone方法返回，一个新对象被创建。
#### 2.clone对象的使用
```java
Person p = new Person(23,"zhang");
Person p1 = (Person) p.clone();
```
#### 3.深拷贝与浅拷贝
对于上述代码中int类型，由于int是基本数据类型，所以直接将4字节的整数值拷贝过来就行。
但是String类型只是一个引用，那么对它的拷贝有两种方式：直接将原对象引用值拷贝给新对象，或是根据原对象指向的字符串对象创建一个相同的字符串对象，将新字符串对象引用赋给新对象
>上述代码为浅拷贝

如果想要深拷贝一个对象，这个对象要实现Cloneable接口，实现clone方法，并在clone方法内部，把该对象引用的其他对象也要 clone 一份，这就要求这个被引用的对象必须也要实现 Cloneable 接口并且实现 clone 方法。
```java
    static class Body implements Cloneable{
        public Head head;
        public Body(){};
        public Body(Head head){this.head = head;}
        @Override
        protected Object clone() throws CloneNotSupportedException{
            Body newBody = (Body) super.clone();
            newBody.head = (Head) head.clone();
            return newBody;
        }
    }
    static class Head implements Cloneable{ 
        public Face face;
        public Head() {}
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
       }
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        Body body = new Body(new Head(new Face()));
        Body body1 = (Body) body.clone();
        System.out.println("body == body1 : " + (body == body1) );
        System.out.println("body.head == body1.head : " + (body.head == body1.head));
    }
```
