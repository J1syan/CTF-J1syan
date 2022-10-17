## go语言基础快速入门
### 前言
最近在学java安全，还要刷题准备比赛（该死，比赛一直跳票...）。想到大三还要补好多基础，心好累。还好课程不多，希望大三能保研成功，那样大四和未来可能会轻松一些。</br>
未来主语言是java，所以go的学习不会很深入，基础学完，再碰到go的题目再继续学习。


### 变量
变量声明的几种方式
```go
第一种:
var a int

第二种:
a :=1

同一行声明多个同类型变量:
var a,b int=1,2
```

### 基本数据类型
```go
Go语言的基本类型有：
    bool
    string
    int、int8、int16、int32、int64
    uint、uint8、uint16、uint32、uint64、uintptr
    byte // uint8 的别名
    rune // int32 的别名 代表一个 Unicode 码
    float32、float64
    complex64、complex128

分类:
数字、字符串、布尔
数字类型:int、int8、int16、int32、int64等
布尔类型:bool
复数类型:complex64、complex128

简单案例:
var a int = 1
var flag boolen = true
var b string = "Hello,World"
x := cmplx.Sqrt(-5+12i)
```

<p>匿名变量的特点是一个下画线“_”，“_”本身就是一个特殊的标识符，被称为空白标识符。它可以像其他标识符那样用于变量的声明或赋值（任何类型都可以赋值给它），但任何赋给这个标识符的值都将被抛弃，因此这些值不能在后续的代码中使用，也不可以使用这个标识符作为变量对其它变量进行赋值或运算。使用匿名变量时，只需要在变量声明的地方使用下画线替换即可</p>
<font color="red">匿名变量不占用内存空间，不会分配内存。匿名变量与匿名变量之间也不会因为多次声明而无法使用。</font>

```go
案例:
func GetData() (int, int) {
    return 100, 200
}
func main(){
    a, _ := GetData()
    _, b := GetData()
    fmt.Println(a, b)
}
```

### 数组
```go
数组的声明:
var a [5]int

多维数组:
var b [5][5]int

数组遍历:
var team [3]string
team[0] = "hammer"
team[1] = "soldier"
team[2] = "mum"

for k, v := range team {
    fmt.Println(k, v)
}

输出结果:
0 hammer
1 soldier
2 mum 
```

### 切片
```go
var highRiseBuilding [30]int

for i := 0; i < 30; i++ {
        highRiseBuilding[i] = i + 1
}

// 区间
fmt.Println(highRiseBuilding[10:15])

// 中间到尾部的所有元素
fmt.Println(highRiseBuilding[20:])

// 开头到中间指定位置的所有元素
fmt.Println(highRiseBuilding[:2])

结果:
[11 12 13 14 15]
[21 22 23 24 25 26 27 28 29 30]
[1 2]

使用 make() 函数构造切片:
make( []Type, size, cap )
其中 Type 是指切片的元素类型，size 指的是为这个类型分配多少个元素，cap 为预分配的元素数量，这个值设定后不影响 size，只是能提前分配空间，降低多次分配空间造成的性能问题。

案例:
a := make([]int, 2)
b := make([]int, 2, 10)

fmt.Println(a, b)
fmt.Println(len(a), len(b))

结果:
[0 0] [0 0]
2 2
```
### Map
```go
map 是 go 的一种 Key-Value 类型的数据结构，我们可以通过下面的命令声明一个 map ：
m := make(map[string]int)

m是 一个 Key类型为 string、Value 类型为 int 的 map 类型的变量。我们可以很容易地添加键值对到 map 中：
// adding key/value
m["clearity"] = 2
m["simplicity"] = 3
// printing the values
fmt.Println(m["clearity"]) // -> 2
fmt.Println(m["simplicity"]) // -> 3
```
### 函数
```go
func add(a int, b int) int {
3
c := a + b
return c
}
func main() {
fmt.Println(add(2, 1))
}
//=> 3
```

### 结构体
结构体定义:
```go
type person struct {
name string
age int
gender string
}
```
使用多个值的列表初始化结构体:
```go
ins := 结构体类型名{
    字段1的值,
    字段2的值,
    …
}

案例:
type Address struct {
    Province    string
    City        string
    ZipCode     int
    PhoneNumber string
}
addr := Address{
    "四川",
    "成都",
    610000,
    "0",
}
fmt.Println(addr)
输出:
{四川 成都 610000 0}
```

初始化匿名结构体
```go
ins := struct {
    // 匿名结构体字段定义
    字段1 字段类型1
    字段2 字段类型2
    …
}{
    // 字段值初始化
    初始化字段1: 字段1的值,
    初始化字段2: 字段2的值,
    …
}

案例:
package main
import (
    "fmt"
)
// 打印消息类型, 传入匿名结构体
func printMsgType(msg *struct {
    id   int
    data string
}) {
    // 使用动词%T打印msg的类型
    fmt.Printf("%T\n", msg)
}
func main() {
    // 实例化一个匿名结构体
    msg := &struct {  // 定义部分
        id   int
        data string
    }{  // 值初始化部分
        1024,
        "hello",
    }
    printMsgType(msg)
}

输出:
*struct { id int; data string }
```
### 方法
方法是一个特殊类型的带有返回值的函数。返回值既可以是值，也可以是指针。让我们创建一个名为 describe 的方法，它具有我们在上面的例子中创建的 person结构体类型
的返回值：
```go
import "fmt"
//定义结构体
type person struct {
name string
age int
gender string
}
// 方法定义
func (p *person) describe() {
fmt.Printf("%v is %v years old.", p.name, p.age)
}
func (p *person) setAge(age int) {
p.age = age
}
func (p person) setName(name string) {
p.name = name
}
func main() {
pp := &person{name: "Bob", age: 42, gender: "Male"}
pp.describe()
// => Bob is 42 years old
pp.setAge(45)
fmt.Println(pp.age)
//=> 45
pp.setName("Hari")
4
fmt.Println(pp.name)
//=> Bob
}
```
### Go语言垃圾回收和SetFinalizer
```go
func SetFinalizer(x, f interface{})
参数说明如下：
参数 x 必须是一个指向通过 new 申请的对象的指针，或者通过对复合字面值取址得到的指针。
参数 f 必须是一个函数，它接受单个可以直接用 x 类型值赋值的参数，也可以有任意个被忽略的返回值。
```

### 接口
Go 的接口是一系列方法的集合。接口有助于将类型的属性组合在一起。
```go
接口 animal:
type animal interface {
    description() string
}


package main
import "fmt"
type animal interface {
    description() string
}
type cat struct {
    Type string
    Sound string
}
type snake struct {
    Type string
    Poisonous bool
}
func (s snake) description() string {
    return fmt.Sprintf("Poisonous: %v", s.Poisonous)
}
func (c cat) description() string {
    return fmt.Sprintf("Sound: %v", c.Sound)
}
func main() {
    var a animal
    a = snake{Poisonous: true}
    fmt.Println(a.description())
    a = cat{Sound: "Meow!!!"}
    fmt.Println(a.description())
}
//=> Poisonous: true
//=> Sound: Meow!!!
```






















#### 参考链接:
http://c.biancheng.net/view/6170.html</br>
https://www.runoob.com/go/go-map.html</br>
https://github.com/coderit666/GoGuide</br>
《Go语言核心编程》