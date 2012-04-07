class Main {

    public static void main(String[] args) {
        DynamicHashTable<String> hash = new DynamicHashTable<String>(2,10);

        hash.set(0,"Ackbar");
        hash.set(11,"Leia");
        hash.set(1,"Han Solo");
        hash.set(3,"Lando");
        hash.set(2,"Chewie");
        hash.set(5,"Han");
        hash.set(4,"Vader");
        hash.set(6,"Anakin");
        hash.set(7,"Palpatine");
        hash.set(8,"Luke");
        hash.set(9,"Windu");

        hash.print();
    }

}