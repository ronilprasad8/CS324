/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

/**
 *
 * @author fehnker_a
 */
public class Banking {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Application started");

        Account savings = new Account("Pete", "Super Saver", 1000);
        Account checking = new Account("Pete", "Free Checking", 1000);

        System.out.println("\nBeginning of month");
        System.out.println(savings.accountType + ":\t" + savings.balance);
        System.out.println(checking.accountType + ":\t" + checking.balance);
        System.out.println("Total before \t" + (checking.balance + savings.balance));

        Transfer transfer = new Transfer(savings, checking, 100);
        Interest checkInterest = new Interest(checking, -10);
        Interest saveInterest = new Interest(savings, 10);

        checkInterest.start();
        saveInterest.start();
        saveInterest.join();
        checkInterest.join();

        transfer.start();

        System.out.println("\nEnd of month");
        System.out.println(savings.accountType + ":\t" + savings.balance);
        System.out.println(checking.accountType + ":\t" + checking.balance);
        System.out.println("Total  after \t" + (checking.balance + savings.balance));
        System.out.println("Main thread finished");
    }
}
