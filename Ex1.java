class Student{
    int id;
    String name;
    int age;
    public Student(int id,String name,int age){
        this.id=id;
        this.name=name;
        this.age=age;
    }
}
public class Ex71 {
    public static void main(String[] args){
        Student me=new Student(10,"Quang",20);
        System.out.print("ID: "+me.id+",Name: "+me.name+",Age: "+me.age);
    }
}
