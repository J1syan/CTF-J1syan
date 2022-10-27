import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ListString {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<String> test = new ArrayList<>();

        test.add("xie");
        test.add("zi");
        test.add("J1syan");
        test.add("li");
//        test.add(100);
        for(int i = 0;i<test.size();i++){
            System.out.println(test.get(i));
        }
        System.out.println("=============================");

        Class clazz = test.getClass();
        Method m = clazz.getDeclaredMethod("add", Object.class);
        m.invoke(test,100);
        for(Object obj:test){
            System.out.println(obj);

        }
    }
}
