class HighOrderFunctions {

    // Wrapper for function class.
    private static interface Function {
        public int perform(int x, int y);
    }

    // Creates a function that adds two integers
    private static Function add() {
        return new Function() {
                public int perform(int x, int y) {
                    return x+y;
                }
            };
    }

    // Creates a function that subtracts two integers
    private static Function subtract() {
        return new Function() {
                public int perform(int x, int y) {
                    return x-y;
                }
            };
    }

    // Creates a function that divides two integers
    private static Function divide() {
        return new Function() {
                public int perform(int x, int y) {
                    return x/y;
                }
            };
    }

    // Creates a function that multiplies two integers
    private static Function multiply() {
        return new Function() {
                public int perform(int x, int y) {
                    return x*y;
                }
            };
    }

    // Applies a function to two integer parameters
    public static int apply(int x, int y, Function g) {
        return g.perform(x, y);
    }

    // Main function
    public static void main(String[] args) {
        // Test the fuctions:

        // addition
        System.out.print("10+3: ");
        System.out.println(apply(10, 3, add()));

        // division
        System.out.print("10/3: ");
        System.out.println(apply(10, 3, divide()));

        // subtraction
        System.out.print("10-3: ");
        System.out.println(apply(10, 3, subtract()));

        // multiplication
        System.out.print("10*3: ");
        System.out.println(apply(10, 3, multiply()));
    }

}