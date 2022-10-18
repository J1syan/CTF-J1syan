### 不同包中变量、函数、方法、类型公私有问题
<li> 在Go语言中通过首字母大小写来控制变量、函数、方法、类型的公私有</li>
<li> 如果首字母小写, 那么代表私有, 仅在当前包中可以使用</li>
<li> 如果首字母大写, 那么代表共有, 其它包中也可以使用</li>

```go
package demo

import "fmt"

var num1 int = 123 // 当前包可用
var Num1 int = 123 // 其它包也可用

type person struct { // 当前包可用
	name string // 当前包可用
	age int // 当前包可用
}

type Student struct { // 其它包也可用
	Name string // 其它包也可用
	Age int // 其它包也可用
}

func test1()  { // 当前包可用
	fmt.Println("test1")
}
func Test2()  { // 其它包也可用
	fmt.Println("Test2")
}

func (p person)say()  { // 当前包可用
	fmt.Println(p.name, p.age)
}
func (s Student)Say()  { // 其它包也可用
	fmt.Println(s.Name, s.Age)
}
```
### 异常处理
方式一: 通过fmt包中的Errorf函数创建错误信息, 然后打印
```go
package main
import "fmt"
func main() {
	// 1.创建错误信息
	var err error = fmt.Errorf("这里是错误信息")
	// 2.打印错误信息
	fmt.Println(err) // 这里是错误信息
}
```
方式二: 通过errors包中的New函数创建错误信息,然后打印
```go
package main
import "fmt"
func main() {
	// 1.创建错误信息
	var err error = errors.New("这里是错误信息")
	// 2.打印错误信息
	fmt.Println(err) // 这里是错误信息
}
```

### 文件的打开和关闭
```go
type file struct {
	pfd     poll.FD
	name    string
	dirinfo *dirInfo 
}
type File struct {
	*file // os specific
}
```
Open函数
    <li>func Open(name string) (file *File, err error)</li>
    <li>Open打开一个文件用于读取</li>
Close函数
    <li>func (f *File) Close() error</li>
    <li>Close关闭文件f</li>

```go
package main
import (
	"fmt"
	"os"
)

func main() {
	// 1.打开一个文件
	// 注意: 文件不存在不会创建, 会报错
	// 注意: 通过Open打开只能读取, 不能写入
	fp, err := os.Open("d:/lnj.txt")
	if err != nil{
		fmt.Println(err)
	}else{
		fmt.Println(fp)
	}

	// 2.关闭一个文件
	defer func() {
		err = fp.Close()
		if err != nil {
			fmt.Println(err)
		}
	}()
}
```
### 文件读取
Read函数(不带缓冲区去读)
<li>func (f *File) Read(b []byte) (n int, err error)</li>
<li>Read方法从f中读取最多len(b)字节数据并写入</li>

```go
package main

import (
	"fmt"
	"io"
	"os"
)

func main() {
	// 1.打开一个文件
	// 注意: 文件不存在不会创建, 会报错
	// 注意: 通过Open打开只能读取, 不能写入
	fp, err := os.Open("d:/lnj.txt")
	if err != nil{
		fmt.Println(err)
	}else{
		fmt.Println(fp)
	}

	// 2.关闭一个文件
	defer func() {
		err = fp.Close()
		if err != nil {
			fmt.Println(err)
		}
	}()

	// 3.读取指定指定字节个数据
	// 注意点: \n也会被读取进来
	//buf := make([]byte, 50)
	//count, err := fp.Read(buf)
	//if err != nil {
	//	fmt.Println(err)
	//}else{
	//	fmt.Println(count)
	//	fmt.Println(string(buf))
	//}

	// 4.读取文件中所有内容, 直到文件末尾为止
	buf := make([]byte, 10)
	for{
		count, err := fp.Read(buf)
		// 注意: 这行代码要放到判断EOF之前, 否则会出现少读一行情况
		fmt.Print(string(buf[:count]))
		if err == io.EOF {
			break
		}
	}
}
```

