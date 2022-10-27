import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class Teacher {

    public Teacher() {
    }

    public String name;

    protected int age;

    boolean sex;

    private String address;

    @Override
    public String toString() {
        return "Teacher [name=" + name + ", age=" + age + ", sex=" + sex + ", address=" + address + "]";
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class teaClass = Class.forName("Teacher");	//创建Class对象（第三种方式）

//获取Teacher类中的所有的public字段
        Field[] f = teaClass.getFields();
        for (Field field : f) {
            System.out.println(field);
        }

//获取Teacher类中的所有的字段(包含各种访问修饰符)
        System.out.println("===========所有的字段=======================>");
        Field[] df = teaClass.getDeclaredFields();
        for (Field field : df) {
            System.out.println(field);
        }
        System.out.println("===========公共的特定的字段=======================>");
// 获取公共的特定的字段
        Field field = teaClass.getField("name");
        System.out.println(field);
        System.out.println("===========特定的字段=======================>");
// 获取特定的字段
        Field field2 = teaClass.getDeclaredField("age");
        System.out.println(field2);

/**
 * 为字段设置具体的值
 * 参数一:该类的对象
 * 参数二:为特定的属性赋值
 */
        Object object = teaClass.getConstructor().newInstance();//相当于Object object = new Teacher();
        field.set(object, "张三丰");

        System.out.println("名称为:"+((Teacher)object).name);

    }

}





