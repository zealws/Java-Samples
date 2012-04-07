class Main {

    public static void main(String[] args) {
        IncrementalHashtable<String> hash = new IncrementalHashtable<String>(10,100);

        hash.set(0,"Ackbar");
        hash.set(101,"Leia");
        hash.set(1,"Han");
        hash.set(3,"Lando");     // Collision
        hash.set(2,"Chewie");    // Collision
        hash.set(5,"Han");
        hash.set(4,"Vader");     // Collision
        hash.set(6,"Yoda");      // Collision
        hash.set(7,"Palpatine"); // Collision
        hash.set(8,"Luke");      // Collision
        hash.set(9,"Windu");     // Collision
        hash.set(10,"Anakin");   // Collision

        hash.printContents();
    }

}