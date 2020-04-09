package search;

public class BinarySearch {
    // Pre : a[-1] = +INF, a[N] = -INF, \forall i \in [0, N] a[i - 1] >= a[i], x \ in Z, l + 1 <= r
    // Post : a[R] <= x && R = min(\forall R' :  a[R'] <= x)
    // I : a[l] > x >= a[r], l < r
    public static int search(int x, int[] a, int l, int r) {
        // a[-1] = +INF, a[N] = -INF, \forall i \in [0, N] a[i - 1] >= a[i], x \in Z, a[l] > x >= a[r], l < r
        if (l + 1 == r) {
            // (a[l] > x >= a[r], l + 1 == r) => r = min(\forall R' :  a[R'] <= x)
            return r;
        } else {
            // a[l] > x >= a[r], l + 1 < r
            int m = (l + r) / 2;
            // m = mid_([l, r]), a[l] > x >= a[r], l + 1 < r
            if (a[m] <= x) {
                // m = mid_([l, r]), a[l] > x >= a[r], l + 1 < r, a[m] <= x
                return search(x, a, l, m);
            } else {
                // m = mid_([l, r]), a[l] > x >= a[r], l + 1 < r, a[m] > x
                return search(x, a, m, r);
            }
        }
    }

    // Pre : a[-1] = +INF, a[N] = -INF, \forall i \in [0, N] a[i - 1] >= a[i], x \in Z
    // Post : a[R] <= x && R = min(\forall R' :  a[R'] <= x)
    public static int search1(int x, int a[]) {
        // a[-1] = +INF, a[N] = -INF, \forall i \in [0, N] a[i - 1] >= a[i]
        int l = -1, r = a.length, m;
        // I : a[l] > x >= a[r]
        while (r > l + 1) {
            // a[l] > x >= a[r], r > l + 1
            m = (l + r) / 2;
            // m = mid_([l, r]), a[l] > x >= a[r], r > l + 1
            if (a[m] <= x) {
                // m = mid_([l, r]), a[l] > x >= a[r], r > l + 1, x >= a[m]
                r = m;
                // r := m, m > l => (l < r)  => (a[l] > x >= a[r])
                // a[l] > x >= a[r], l < r
            } else {
                // m = mid_([l, r]), a[l] > x >= a[r], r > l + 1, a[m] > x
                l = m;
                // l := m, r > m => (l < r) => (a[l] > x >= a[r])
                // a[l] > x >= a[r], l < r
            }
            // a[l] > x >= a[r], l < r
        }
        // a[l] > x >= a[r],  (l < r && r <= l + 1) => (l = r + 1)
        // (a[l] > x >=a[r]) && (l = r + 1) => r = min(\forall R' :  a[R'] <= x)
        return r;
    }

    public static void main(String[] args) {
        int a[];
        int x = Integer.valueOf(args[0]);
        a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.valueOf(args[i + 1]);
        }
        int y = search1(x, a);
        //int y = search(x, a , -1, a.length);
        System.out.println(y);
    }
}
