<a href="https://blog.csdn.net/qq_37561898/article/details/122744290">wp1</a></br>
<a href="https://blog.csdn.net/miuzzx/article/details/122998220">羽师傅wp</a>
### web680
1. 题目提示参数是code，用code进行get传参
2. code=phpinfo() 发现禁用很多函数
3. 使用 code=var_dump(scandir(".")); 查看当前目录下文件，发现secret_you_never_know
4. 直接访问该文件 或者show_source("secret_you_never_know");





### web681
1. dirseach扫描后台发现有www.zip 文件下载 发现源代码
2. 查看源代码经过审计 主要语句:
3. select count(*) from ctfshow_users where username = '' or nickname = ''
4. 主要过滤了单引号和空格，空格可以使用\**\绕过 
5. 单引号则是使用\进行转义拼凑
6. payload: ||1#\


### web685
```php
<?php
function is_php($data){
    return preg_match('/<\?.*[(`;?>].*/is', $data);
}

if(empty($_FILES)) {
    die(show_source(__FILE__));
}

$user_dir = './data/';
$data = file_get_contents($_FILES['file']['tmp_name']);
if (is_php($data)) {
    echo "bad request";
} else {
    @mkdir($user_dir, 0755);
    $path = $user_dir . '/' . random_int(0, 10) . '.php';
    move_uploaded_file($_FILES['file']['tmp_name'], $path);

    header("Location: $path", true, 303);
}
```
考点:</br>
preg_match() 函数匹配超过100w次就会返回false，而不是返回非1和0





