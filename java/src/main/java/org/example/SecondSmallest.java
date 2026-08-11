package org.example;

public class SecondSmallest {

    public static int findSecondSmallest(int[] arr) {
        if (arr == null || arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements");
        }

        int smallest = Integer.MAX_VALUE;
        int second   = Integer.MAX_VALUE;

/////////////////////////////////////////////////
//        Single pass (above)O(n)O(1)
//         First iteration, num = 5:
//        if (5 < Integer.MAX_VALUE)  // ✅ always true → smallest = 5

//        int smallest = 0;
// Array = {3, 1, 4}
// 3 < 0? ❌ No → smallest stays 0 (WRONG!)
//


//        2️⃣ Why else if (num < second && num != smallest) ?
//        This condition has two guards:
//        Guard 1: num < second
//                java// Only update 2nd smallest if num is a better (smaller) candidate
//// e.g: second=5, num=3 → 3 is a better 2nd smallest ✅
//// e.g: second=5, num=8 → 8 is worse, ignore ❌
//        Guard 2: num != smallest
//        java// Ensures 2nd smallest is DISTINCT from smallest
//// Array = {1, 1, 1}  → smallest=1, second should NOT be 1
//// Without this guard: second would also become 1 (wrong for distinct requirement)

//
//        3️⃣ Why if (second == Integer.MAX_VALUE) ?
//        javaif (second == Integer.MAX_VALUE) {
//            throw new IllegalArgumentException("No distinct second smallest element");
//        }
//        After the loop, if second is still MAX_VALUE, it means it was never updated — i.e., no valid 2nd smallest was ever found.
//                When does this happen?
//                java// Case 1: All elements are identical
//        int[] arr = {5, 5, 5};
//// smallest=5, second never updates (num==smallest always) → still MAX_VALUE
//
//// Case 2: Only one element (caught earlier, but defensive check)
//        int[] arr = {7};
//        This check prevents returning a garbage value (Integer.MAX_VALUE) as if it were a real answer.
////////////////////////////////////////////

        for (int num : arr) {
            if (num < smallest) {
                second   = smallest;   // old smallest becomes 2nd
                smallest = num;
            } else if (num < second && num != smallest) {
                second = num;          // better 2nd candidate
            }
        }

        if (second == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No distinct second smallest element");
        }
        return second;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        System.out.println(findSecondSmallest(new int[]{5, 3, 1, 4, 2}));     // 2
        System.out.println(findSecondSmallest(new int[]{10, 20, 30}));         // 20
        System.out.println(findSecondSmallest(new int[]{5, 5, 5, 3, 3}));     // 5
        System.out.println(findSecondSmallest(new int[]{-3, -1, -7, 0}));     // -3
    }
}