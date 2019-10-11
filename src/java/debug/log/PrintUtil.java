package debug.log;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class PrintUtil {
    public static void printhex(byte[] arr) {
        printhex(arr, arr.length);
    }

    public static void printhex(byte[] arr, int length) {
        for(int i=0; i<length; i++) {
            System.out.printf("%2x ", arr[i]);
            if((i-15)%16 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
