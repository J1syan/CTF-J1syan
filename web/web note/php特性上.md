### php特性
<a href="https://blog.csdn.net/qq_49480008/article/details/113753951">ctfshow php特性</a>


<a href="https://blog.csdn.net/weixin_45696568/article/details/113631173">ctf show-web入门 php特性篇部分题解</a>
<a href="https://www.php.net/manual/zh/class.reflectionclass.php">php反射类</a>
<br/>
<a href="https://blog.csdn.net/weixin_45669205/article/details/113753555">CTFshow——PHP特性(上)</a>


<a href="https://www.jianshu.com/p/c9089fd5b1ba?u_atoken=55f06165-8dc7-43a0-acb3-309d0e0317f9&u_asession=01Ydg4338TWEiMLDpgKvF2q4f6Dfqa4o9YasDOnSNJhEBektMeYc13GAY1yTKfeRH7X0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K9tlc1S1VDIzYaFpthpLz1pp5RU6UlCZRD0CDVEtyq5OmBkFo3NEHBv0PZUm6pbxQU&u_asig=05e8UfzbqaZ0I2SYhoWmA47-j_G7Hw8vsxR15OwF8YpjPx9tz31315NoWMFmMTHPhx4LBC6NA6FTuQVWRwThQPdNIvHHElI7KdaPMZTheQyPv6ne_0HeiR91C41qCNcA7v7mwUu9YIQe74zsZ94prppDqZfxWYLvRWWlVaZLRXyzL9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzb8E_iikv3wlFL21jRAq0ydc4u7x92shjHxOdS8yhDolO21gpjNe9akLVIO763CEMe3h9VXwMyh6PgyDIVSG1W_8k7K1cPWI9GVxiP0T4REaIwNOrpX04BxodSt1A8Jz_VCjl2eFlqtj_8xRzSlA-jjW5clkFoonSpC1jx2m-vD0mWspDxyAEEo4kbsryBKb9Q&u_aref=iUhPtKjOUx2oyWZ%2Bgt6xnyOCkgI%3D">MD5 强类型绕过(true):</a>

preg_match()

此函数用于执行正则表达式匹配</br>
语法:

int preg_match ( string $pattern , string $subject [, array &$matches [, int $flags = 0 [, int $offset = 0 ]]] )

参数说明：

    $pattern: 要搜索的模式，字符串形式。

    $subject: 输入字符串。

    $matches: 如果提供了参数matches，它将被填充为搜索结果。 $matches[0]将包含完整模式匹配到的文本， $matches[1] 将包含第一个捕获子组匹配到的文本，以此类推。

    $flags：flags 可以被设置为以下标记值：
    PREG_OFFSET_CAPTURE: 如果传递了这个标记，对于每一个出现的匹配返回时会附加字符串偏移量(相对于目标字符串的)。 注意：这会改变填充到matches参数的数组，使其每个元素成为一个由 第0个元素是匹配到的字符串，第1个元素是该匹配字符串 在目标字符串subject中的偏移量。

    offset: 通常，搜索从目标字符串的开始位置开始。可选参数 offset 用于 指定从目标字符串的某个未知开始搜索(单位是字节)。

返回值(重点)：
返回 pattern 的匹配次数。 它的值将是 0 次（不匹配）或 1 次，因为 preg_match() 在第一次匹配后 将会停止搜索。preg_match_all() 不同于此，它会一直搜索subject 直到到达结尾。 如果发生错误preg_match()返回 FALSE。
```
绕过姿势:
如果不按规定传一个字符串，通常是传一个数组进去，这样就会报错，从而返回false，达到我们的目的。
```

## intval()

intval() 函数用于获取变量的整数值。

intval() 函数通过使用指定的进制 base 转换（默认是十进制），返回变量 var 的 integer 数值。 intval() 不能用于 object，否则会产生 E_NOTICE 错误并返回 1。

语法:
```
int intval ( mixed $var [, int $base = 10 ] )
```

参数说明:

    $var：要转换成 integer 的数量值。
    $base：转化所使用的进制。

进制：
```
0??:八进制
0x??:十六进制
0b??:二进制
```

当题目:
```php
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


可以通过这些方法绕过(从羽师傅那里学的):
```
intval('4476.0')===4476    小数点 #intval只识别整数部分
intval('+4476.0')===4476   正负号
intval('4476e0')===4476    科学计数法
intval('0x117c')===4476    16进制
intval('010574')===4476    8进制 #当题目过滤字母时，2和16进制都不好用
intval(' 010574')===4476   8进制+空格 #当要求存在0且0不是首位时
intval('0b1000101111100')===4476  2进制
```

#### web96(路径问题)
```php
<?php
highlight_file(__FILE__);

if(isset($_GET['u'])){
    if($_GET['u']=='flag.php'){
        die("no no no");
    }else{
        highlight_file($_GET['u']);
    }
}
```
payload
```
php://filter/read=convert.base64-encode/resource=flag.php #利用LFI来查看源码
/var/www/html/flag.php              绝对路径
./flag.php                          相对路径
php://filter/resource=flag.php      php伪协议 
```

### in_array()

in_array：(PHP 4, PHP 5, PHP 7)

功能：检查数组中是否存在某个值
```
定义： bool in_array ( mixed $needle , array $haystack [, bool $strict = FALSE ] )
```
```
在 $haystack中搜索 $needle，如果第三个参数 $strict的值为 TRUE，则 in_array()函数会进行强检查，检查 $needle的类型是否和 $haystack中的相同。如果找到 $haystack，则返回 TRUE，否则返回 FALSE。
````

#### web102、103——回调函数、经base64与bin2hex后全为数字
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
'''
