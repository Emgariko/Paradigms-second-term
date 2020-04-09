package search;

public class BinarySearchSpan {

    public static void main(String[] args) {
        int a[];
        int x = Integer.valueOf(args[0]);
        a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.valueOf(args[i + 1]);
        }
        int y = BinarySearch.search1(x, a);
        //int y = BinarySearch.search(x, a , -1, a.length);
        if (x == Integer.MIN_VALUE) {
            System.out.println(Integer.toString(y) + " " + Integer.toString(a.length - y));
        } else {
            int z = BinarySearch.search(x - 1, a, -1, a.length);
            //int z = BinarySearch.search1(x - 1, a);
            if (y == z) {
                System.out.println(Integer.toString(y) + " " + Integer.toString(0));
            } else {
                System.out.println(Integer.toString(y) + " " + Integer.toString(z - y));
            }
        }
    }
}
