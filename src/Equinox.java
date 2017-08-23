/**
 *
 * @author Julian Craske
 */

import Client.*;

public class Equinox {
    public static void main(String[] args) {
        System.out.print("Arguments provided: ");
        for(String s : args) {
            System.out.print(s + " ");
        }
        if(args.length == 0) System.out.print("none");
        System.out.println();

        if(args.length > 0 && args[0].equalsIgnoreCase("dedicated")) {
            Dedicated.main(args);
        } else {
            ClientController.main(args);
        }
    }
}
