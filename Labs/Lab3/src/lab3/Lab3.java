/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lab3;

import java.util.Scanner;

/**
 *
 * @author chaudhary_k
 */
public class Lab3 {

    /**
     * @param args the command line arguments
     */
   public static void main( String[] args ) {
 number n = new number();
 Scanner in = new Scanner(System.in);

 new HexNumber( n );
 new BinNumber( n );
while (true) {
System.out.print( "\nEnter a number: " );
n.setValue( in.nextInt() );
}
 } 
}
