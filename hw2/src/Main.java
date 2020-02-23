public class Main {
    static int x;
    static int a[];
    public static void search(int l, int r) {

    }

    public static int search1() {
        int l = -1, r = a.length, m;
        while (r > l + 1) {
            m = (l + r) / 2;
            if (a[m] >= x) {
                r = m;
            } else {
                l = m;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        x = Integer.valueOf(args[0]);
        a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.valueOf(args[i + 1]);
        }
        int y = search1();
        System.out.println(y);
    }
}
