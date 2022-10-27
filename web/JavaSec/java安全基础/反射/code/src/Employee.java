import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Employee {

    public Employee() {
    }

    public String name;

    protected double money;

    String address;

    private long number;


    protected void getDayMoney(String name) {
        System.out.println("我是Employee受保护的获取日薪的方法,有一个参数为:" + name);
    }

    public void getweekMoney(String name, double money) {
        System.out.println("我是Employee公有的获取周薪的方法,没有参数..");
    }

    void getMonthMoney() {
        System.out.println("我是Employee默认的获取月薪的方法,没有参数..");
    }

    private void getYearMoney(int age) {
        System.out.println("我是Employee私有的的获年薪月薪的方法,有一个参数为:" + age);
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", money=" + money + ", address=" + address + ", number=" + number + "]";
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Employee中的main()方法执行了...");
        System.out.println(Arrays.toString(args));

        Class<?> emplClass = Class.forName("Employee");
        System.out.println("==========================================");
        System.out.println("1.获取所有的公共的方法  （包含父类的方法）");
        Method[] methods = emplClass.getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }
        System.out.println("==========================================");
        System.out.println("2.获取该类中的所有方法，以数组形式返回");
        Method[] methods1 = emplClass.getDeclaredMethods();
        for (Method method : methods1) {
            System.out.println(method);
        }
        System.out.println("==========================================");
        System.out.println("3.获取特定的公有的方法：  只能是公共的");
        //3.获取特定的公有的方法：  只能是公共的
//参数一:要获取的方法的名称,参数二:方法对应的形参列表的类型
        Method method = emplClass.getMethod("getweekMoney",new Class[]{String.class,double.class});
        System.out.println(method);     //输出：public void Employee.getweekMoney(java.lang.String,double)
        System.out.println("==========================================");
        System.out.println("//4.获取特定的方法：\t可以是私有的");
//4.获取特定的方法：	可以是私有的
        Method method2 = emplClass.getDeclaredMethod("getYearMoney", int.class);
        System.out.println(method2);	//输出：private void Employee.getYearMoney(int)
        System.out.println("==========================================");
        //emplClass对应的类的对象
        Object obj = emplClass.getConstructor().newInstance();
//可以通过Employee类对象去直接调用非私有方法，我这里将测试类和Employee类放在了同一个包下，所有可以访问默认方法（包限定方法）
        Employee employee = (Employee)obj;
        employee.getMonthMoney();

//参数一:要调用的对象 参数二:方法具体要求传递的值   返回值:为调用该方法后的返回值所对应的对象,如果没有返回值（void）,则对象为null
        method2.setAccessible(true);
        Object result = method2.invoke(obj, 20);   //执行method2方法对应的代码
        System.out.println(result);

//输出：
/*
我是Employee默认的获取月薪的方法,没有参数..
我是Employee私有的的获年薪月薪的方法,有一个参数为:20
null
*/

    }

}
