import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.AnnotatedWildcardType;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.RunnableFuture;
import java.util.stream.IntStream;
import java.util.zip.Adler32;

import javax.print.attribute.standard.MediaSize.ISO;
import javax.swing.RepaintManager;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;


public class Township{
    static Random generator;
    static ArrayList<String> names = new ArrayList<String>();

    static <T> T pickRandom(ArrayList<T> array){
        int rnd = generator.nextInt(array.size());
        return array.get(rnd);
    }

    static class World{
        public int est = 0;
        public String name = "example";
        public int value = 0;
        public int index;

        public ArrayList<Creature> creatures;
        Creature creatureAdd = new Creature();
        int startingNumber = 15;
        public int food = (generator.nextInt(startingNumber) + 1);

        public World(){
            creatures = addRandomCreature(startingNumber);
            print();
        }
        public void NewCreature(){
            creatures.add(creatureAdd);
        }
        public void Die(){
            creatures.remove(index);
        }


        public void print(){
            int creatureNumber = 0;
            food = food+(generator.nextInt(startingNumber) + 1);
            ArrayList <Integer> toDie = new ArrayList<>();
            int addCreatureAmount = 0;
            System.out.println("NAME\t\tAGE\t\tPREDATOR\tHUNGER\tBREEDABLE\tWILLING\t\t\tFOOD: "+ food);
            for(Creature c: this.creatures){
                
                System.out.print(c.name);
                if (c.name.length()>=8){
                    System.out.println("\t "+c.age+"\t\t "+c.predator+"\t\t  "+c.hunger+"\t  "+c.breedable+"\t\t   "+c.willing);
                } else {
                    System.out.println("\t\t "+c.age+"\t\t "+c.predator+"\t\t  "+c.hunger+"\t  "+c.breedable+"\t\t   "+c.willing);
                }
            }
            for(Creature c: this.creatures){
                c.reproduce(pickRandom(this.creatures));
                if (c.spawnCreature){
                    addCreatureAmount++;
                    c.spawnCreature = false;
                }
            }
            for(int i = 0; i < addCreatureAmount; i++){
                NewCreature();
                System.out.println("New Baby: "+names.get(1));
            }
            for(Creature c: this.creatures){
                c.die(pickRandom(this.creatures));
                if (c.remove){
                    toDie.add(creatureNumber);
                    creatureNumber--;
                }
                creatureNumber++;
            }
            if (toDie.size()>0){
                for(int i = 0; i <= (toDie.size()-1); i++){
                    index = toDie.get(i);
                    Die();
                }
            }
            for(Creature c: this.creatures){
                c.age++;
                c.hunger++;
                c.willing++;

                int goHungry = generator.nextInt(3);
                if (goHungry==0&&!c.predator&&food>0) {
                    c.hunger = 0;
                    food--;
                System.out.println(c.name+" found food");
                }
            }
            System.out.println("Current Populace: "+(creatures.size()));
        }

        public ArrayList<Creature> addRandomCreature(int iStartingNumber){
            ArrayList<Creature> temp = new ArrayList<Creature>();
            for(int i = 0; i< iStartingNumber; i++){
                temp.add(new Creature());
            }
            return temp;
        }

    }
    
    static class Creature{
        String name = "exampleAnimal";
        int age = 0;
        int hunger = 0;
        Boolean spawnCreature = false;
        Boolean breedable = false;
        int willing = 0;
        Boolean predator = false;
        Boolean remove = false;

        public Creature(){
            age = generator.nextInt(9) + 1;
            hunger = generator.nextInt(2);
            name = pickRandom(names);
            int predgen = generator.nextInt(3);
            if (predgen == 0) {
                predator = true;
            }
            if (age>=2) {
                breedable = true;
                if (hunger>=60) {
                    willing = 0;
                }
            }

        }
    
        public void reproduce(Creature interactee) {
            if ((this.name!=interactee.name)&&
                (this.breedable&&this.willing>3)&&
                (interactee.breedable&&interactee.willing>3)){

                int hasSex = generator.nextInt(2);
                if (hasSex == 0) {
                    System.out.println(this.name + " mated with " + interactee.name);
                    this.willing = 0;
                    interactee.willing = 0;
                    spawnCreature = true;
                }
            }
        }

        public void die(Creature interactee){
            int escapes = generator.nextInt(3);
            if(this.name!=interactee.name){
                if(this.hunger>5){
                    System.out.println(this.name + " starved to death.");
                    remove=true;
                }
                if(this.age>=15){
                    System.out.println(this.name + " died of old age.");
                    remove=true;
                }
                if (!this.predator&&interactee.predator&&escapes==0){
                    System.out.println(this.name + " was killed by " + interactee.name + "!");
                    remove=true;
                    this.hunger=0;
                }
            } 
        } 
    }

    public static void main(String[] args) throws IOException{
        generator = new Random();

        Scanner s = new Scanner(System.in);

        try {
            s = new Scanner(new BufferedReader(new FileReader("./src/animals.txt")));
            s.useLocale(Locale.US);
            s.useDelimiter(",\\s*");

            while(s.hasNext()){
                names.add(s.next());
            }
        }
        finally {
            s.close();
        }

        World world = new World();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean proceed = true;
        while(proceed&&world.creatures.size()>0){
            String input = reader.readLine();
            switch (input){
                case "step":
                    System.out.println();
                    world.print();
                break;
                case "quit":
                    proceed = false;
                break;
                case "reroll":
                    System.out.println();
                    new World();
                break;
            }
        } System.out.println("All creatures are dead.");
    }
}




