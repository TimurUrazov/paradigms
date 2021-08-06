package search;
import java.util.Arrays;

public class BinarySearchMax {
    // Pred: a.length > 0 && exists k: 0 <= k < a.length: a = [a[-1]..a[k]) && [a[k]..a[a.length]]
    // (just a[-1] = -infinity && a[a.length] = infinity)
    // i < j < k -> a[i] < a[j] && k <= i < j -> a[i] > a[j]
    // a[l] <= a[k] && a[r] < a[k], a[k] - max element in a
    // Post: R == a[k]
    public static int search(final int... a) {
        // Pred: exists k: 0 <= k < a.length: a = [a[-1]..a[k]) && [a[k]..a[a.length]]
        // i < j < k -> a[i] < a[j] && k <= i < j -> a[i] > a[j]
        int l = -1;
        // l' == -1 && a[l'] <= a[k] && a[r'] < a[k]
        int r = a.length;
        // l' == -1 && r' == a.length && a[l'] <= a[k] && a[r'] < a[k]
        // Inv: a[l] <= a[k] && a[r] < a[k] && l <= l' && r' <= r
        while (l < r - 1) {
            // l' + 1 < r' && a[l'] <= a[k] && a[r'] < a[k]
            int m = (l + r) / 2;
            // l' < m < r' && a[l'] <= a[k] && a[r'] < a[k]
            if (m > 0 && a[m] < a[m - 1]) {
                // m > 0 && a[m] < a[m - 1] -> k < m < r' -> a[m] < a[k]
                r = m;
                // l == l' && k < m == r < r' -> a[l] <= a[k] && a[r] < a[k]
            } else {
                // m == 0 || a[m] > a[m - 1] && l' < m -> l' < m <= k -> a[k] <= a[m]
                l = m;
                // r == r' && l' < l == m -> a[m] <= a[l] && a[r] < a[k]
            }
            // a[l] <= a[k] && a[r] < a[k] && (l' < l && r <= r' || l' <= l && r < r') -> Inv
        }
        // r == r' && l == l' && r - 1 == l && a[l + 1] < a[k] && a[l] <= a[k] -> a[k] == a[l] -> R == a[l]
        return a[l];
    }

    // Pred: a.length > 0 && exists k: 0 <= k < a.length: a = [a[-1]..a[k]) && [a[k]..a[a.length]]
    // (just a[-1] = -infinity && a[a.length] = infinity)
    // i < j < k -> a[i] < a[j] && k <= i < j -> a[i] > a[j]
    // -1 <= l && r <= a.length && a[l] <= a[k] && a[r] < a[k], a[k] - max element in a
    // Post: R == a[k]
    public static int search(final int l, final int r, final int... a) {
        // Pred: exists k: 0 <= k < a.length: a = [a[-1]..a[k]) && [a[k]..a[a.length]]
        // i < j < k -> a[i] < a[j] && k <= i < j -> a[i] > a[j]
        // -1 <= l' && r' <= a.length && a[l'] <= a[k] && a[r'] < a[k]
        if (r > l + 1) {
            // l' + 1 < r' && a[l'] <= a[k] && a[r'] < a[k]
            final int m = (r + l) / 2;
            // l' < m && m < r' && a[l'] <= a[k] && a[r'] < a[k]
            if (m > 0 && a[m] < a[m - 1]) {
                // m > 0 && a[m] < a[m - 1] -> k < m < r' -> a[m] < a[k]
                return search(l, m, a);
                // R = search(l, m, a): l == l' && k < m == r < r' -> a[l] <= a[k] && a[r] < a[k]
            } else {
                // m == 0 || a[m] > a[m - 1] && l' < m -> l' < m <= k -> a[k] <= a[m]
                return search(m, r, a);
                // R = search(m, r, a): r == r' && l' < l == m -> a[m] <= a[l] && a[r] < a[k]
            }
        } else {
            // r == r' && l == l' && r - 1 == l && a[l + 1] < a[k] && a[l] <= a[k] -> a[k] == a[l] -> R == a[l]
            return a[l];
        }
    }

    // Pred: args.length > 0 && for each i: 0 <= i < args.length args[i] is Integer
    // exists k: 0 <= k < args.length: args = [args[-1]..args[k]) && [args[k]..args[args.length]]
    // (just a[-1] = -infinity && a[a.length] = infinity)
    // i < j < k -> args[i] < args[j] && k <= i < j -> args[i] > args[j]
    // Post: R == args[i], i - index of max element in the array
    public static void main(String[] args) {
        final int[] a = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
//        final int index = leftSearch(a);
        final int index = search(-1, a.length, a);
        System.out.println(index);
    }
}

