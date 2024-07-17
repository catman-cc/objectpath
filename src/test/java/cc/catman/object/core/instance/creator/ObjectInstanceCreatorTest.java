package cc.catman.object.core.instance.creator;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;


public class ObjectInstanceCreatorTest {
    @Test
    public void testCreateObjectWithNoArgsConstructor(){
        ObjectInstanceCreator creator = new ObjectInstanceCreator();
        Person person = creator.create(Person.class);
        Assert.assertNotNull(person);
    }

    @Test
    public void testCreateObjectWithUnpackedConstructor(){
        ObjectInstanceCreator creator = new ObjectInstanceCreator();
        Animal animal = creator.create(Animal.class, "dog",1);
        Assert.assertNotNull(animal);
    }

    @Test
    public void testCreateObjectWithInheritedArgsConstructor(){
        ObjectInstanceCreator creator = new ObjectInstanceCreator();
        Zoo zoo = creator.create(Zoo.class, new Dog("dog", 1));
        Assert.assertNotNull(zoo);
    }

    @Data
    @NoArgsConstructor
    public static class Person{
        private String name;
        private int age;
    }

    @Data
    public static class Animal {
        private String name;
        private int age;

        public Animal(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static class Dog extends Animal {
        @Getter
        private String breed;

        public Dog(String name, int age) {
            super(name, age);
        }
    }

    public static class Cat extends Animal {
        @Getter
        private String color;

        public Cat(String name, int age) {
            super(name, age);
        }
    }

    public static class Zoo {
        @Getter
        private Animal animal;

        public Zoo(Animal animal) {
            this.animal = animal;
        }
    }
}