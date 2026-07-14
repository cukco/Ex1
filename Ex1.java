class Animal{
    protected String name;
    public void makeSound(){
        System.out.println("Some animal sound");
    }
}
class Dog extends Animal{
    public Dog(String name){
        this.name=name;
    }
    @Override
    public void makeSound(){
        System.out.println("Gâu Gâu");
    }
}
public class Ex101 {
    public static void main (String[] args){
        Dog d=new Dog("Pate");
        d.makeSound();
    }
}
