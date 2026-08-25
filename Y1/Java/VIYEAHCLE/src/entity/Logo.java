package entity;
public class Logo 
{
    public void clearScreen()
    {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                //Clear screen for Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //Clear screen for MacOs
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void line(int amt)
    {
        System.out.print(" ");
        for(int i =0;i<amt;i++)
        {
            System.out.print("* ");
        }
        System.out.print("\n");
    }

    public void mainLogo()
    {
        System.out.println("██▒   █▓ ██▓▓██   ██▓▓█████ ▄▄▄       ██░ ██  ▄████▄   ██▓    ▓█████   ");
        System.out.println("▓██░   █▒▓██▒ ▒██  ██▒▓█   ▀▒████▄    ▓██░ ██▒▒██▀ ▀█  ▓██▒    ▓█   ▀  ");
        System.out.println(" ▓██  █▒░▒██▒  ▒██ ██░▒███  ▒██  ▀█▄  ▒██▀▀██░▒▓█    ▄ ▒██░    ▒███    ");
        System.out.println("  ▒██ █░░░██░  ░ ▐██▓░▒▓█  ▄░██▄▄▄▄██ ░▓█ ░██ ▒▓▓▄ ▄██▒▒██░    ▒▓█  ▄  ");
        System.out.println("   ▒▀█░  ░██░  ░ ██▒▓░░▒████▒▓█   ▓██▒░▓█▒░██▓▒ ▓███▀ ░░██████▒░▒████▒ ");
        System.out.println("   ░ ▐░  ░▓     ██▒▒▒ ░░ ▒░ ░▒▒   ▓▒█░ ▒ ░░▒░▒░ ░▒ ▒  ░░ ▒░▓  ░░░ ▒░ ░ ");
        System.out.println("   ░ ░░   ▒ ░ ▓██ ░▒░  ░ ░  ░ ▒   ▒▒ ░ ▒ ░▒░ ░  ░  ▒   ░ ░ ▒  ░ ░ ░  ░ ");
        System.out.println("     ░░   ▒ ░ ▒ ▒ ░░     ░    ░   ▒    ░  ░░ ░░          ░ ░      ░    ");
        System.out.println("      ░   ░   ░ ░        ░  ░     ░  ░ ░  ░  ░░ ░          ░  ░   ░  ░ ");
        System.out.println("     ░        ░ ░                             ░                        ");
        System.out.println("          ██▀███  ▓█████  ███▄    █ ▄▄▄█████▓ ▄▄▄       ██▓            ");
        System.out.println("         ▓██ ▒ ██▒▓█   ▀  ██ ▀█   █ ▓  ██▒ ▓▒▒████▄    ▓██▒            ");
        System.out.println("         ▓██ ░▄█ ▒▒███   ▓██  ▀█ ██▒▒ ▓██░ ▒░▒██  ▀█▄  ▒██░            ");
        System.out.println("         ▒██▀▀█▄  ▒▓█  ▄ ▓██▒  ▐▌██▒░ ▓██▓ ░ ░██▄▄▄▄██ ▒██░            ");
        System.out.println("         ░██▓ ▒██▒░▒████▒▒██░   ▓██░  ▒██▒ ░  ▓█   ▓██▒░██████▒        ");
        System.out.println("         ░ ▒▓ ░▒▓░░░ ▒░ ░░ ▒░   ▒ ▒   ▒ ░░    ▒▒   ▓▒█░░ ▒░▓  ░        ");
        System.out.println("           ░▒ ░ ▒░ ░ ░  ░░ ░░   ░ ▒░    ░      ▒   ▒▒ ░░ ░ ▒  ░        ");
        System.out.println("           ░░   ░    ░      ░   ░ ░   ░        ░   ▒     ░ ░           ");
        System.out.println("            ░        ░  ░         ░                ░  ░    ░  ░        ");                         
    }

    public void loginLogo()
    {
        System.out.println("██▓     ▒█████    ▄████  ██▓ ███▄    █   ");
        System.out.println("▓██▒    ▒██▒  ██▒ ██▒ ▀█▒▓██▒ ██ ▀█   █  ");
        System.out.println("▒██░    ▒██░  ██▒▒██░▄▄▄░▒██▒▓██  ▀█ ██▒ ");
        System.out.println("▒██░    ▒██   ██░░▓█  ██▓░██░▓██▒  ▐▌██▒ ");
        System.out.println("░██████▒░ ████▓▒░░▒▓███▀▒░██░▒██░   ▓██░ ");
        System.out.println("░ ▒░▓  ░░ ▒░▒░▒░  ░▒   ▒ ░▓  ░ ▒░   ▒ ▒  ");
        System.out.println("░ ░ ▒  ░  ░ ▒ ▒░   ░   ░  ▒ ░░ ░░   ░ ▒░ ");
        System.out.println("  ░ ░   ░ ░ ░ ▒  ░ ░   ░  ▒ ░   ░   ░ ░  ");
        System.out.println("    ░  ░    ░ ░        ░  ░           ░  ");
        System.out.println("                                         ");
        System.out.println("    ██▓███   ▄▄▄        ▄████ ▓█████     ");
        System.out.println("   ▓██░  ██▒▒████▄     ██▒ ▀█▒▓█   ▀     ");
        System.out.println("   ▓██░ ██▓▒▒██  ▀█▄  ▒██░▄▄▄░▒███       ");
        System.out.println("   ▒██▄█▓▒ ▒░██▄▄▄▄██ ░▓█  ██▓▒▓█  ▄     ");
        System.out.println("   ▒██▒ ░  ░ ▓█   ▓██▒░▒▓███▀▒░▒████▒    ");
        System.out.println("   ▒▓▒░ ░  ░ ▒▒   ▓▒█░ ░▒   ▒ ░░ ▒░ ░    ");
        System.out.println("   ░▒ ░       ▒   ▒▒ ░  ░   ░  ░ ░  ░    ");
        System.out.println("   ░░         ░   ▒   ░ ░   ░    ░       ");
        System.out.println("                  ░  ░      ░    ░  ░    ");                                                                                                             
    }

        //staff page logo
        public void staffLogo()
        {
            System.out.println("██████ ▄▄▄█████▓ ▄▄▄        █████▒ █████▒  ");
            System.out.println("▒██    ▒ ▓  ██▒ ▓▒▒████▄    ▓██   ▒▓██   ▒ ");
            System.out.println("░ ▓██▄   ▒ ▓██░ ▒░▒██  ▀█▄  ▒████ ░▒████ ░ ");
            System.out.println("  ▒   ██▒░ ▓██▓ ░ ░██▄▄▄▄██ ░▓█▒  ░░▓█▒  ░ ");
            System.out.println("▒██████▒▒  ▒██▒ ░  ▓█   ▓██▒░▒█░   ░▒█░    ");
            System.out.println("▒ ▒▓▒ ▒ ░  ▒ ░░    ▒▒   ▓▒█░ ▒ ░    ▒ ░    ");
            System.out.println("░ ░▒  ░ ░    ░      ▒   ▒▒ ░ ░      ░      ");
            System.out.println("░  ░  ░    ░        ░   ▒    ░ ░    ░ ░    ");
            System.out.println("      ░                 ░  ░               ");
            System.out.println("                                           ");
            System.out.println("    ██▓███   ▄▄▄        ▄████ ▓█████       ");
            System.out.println("   ▓██░  ██▒▒████▄     ██▒ ▀█▒▓█   ▀       ");
            System.out.println("   ▓██░ ██▓▒▒██  ▀█▄  ▒██░▄▄▄░▒███         ");
            System.out.println("   ▒██▄█▓▒ ▒░██▄▄▄▄██ ░▓█  ██▓▒▓█  ▄       ");
            System.out.println("   ▒██▒ ░  ░ ▓█   ▓██▒░▒▓███▀▒░▒████▒      ");
            System.out.println("   ▒▓▒░ ░  ░ ▒▒   ▓▒█░ ░▒   ▒ ░░ ▒░ ░      ");
            System.out.println("   ░▒ ░       ▒   ▒▒ ░  ░   ░  ░ ░  ░      ");
            System.out.println("   ░░         ░   ▒   ░ ░   ░    ░         ");
            System.out.println("                  ░  ░      ░    ░  ░      ");
        }
    
        //add car page's logo
        public void addLogo()
        {
            System.out.println("▄▄▄      ▓█████▄ ▓█████▄     ▄████▄   ▄▄▄       ██▀███    ");
            System.out.println("▒████▄    ▒██▀ ██▌▒██▀ ██▌   ▒██▀ ▀█  ▒████▄    ▓██ ▒ ██▒ ");
            System.out.println("▒██  ▀█▄  ░██   █▌░██   █▌   ▒▓█    ▄ ▒██  ▀█▄  ▓██ ░▄█ ▒ ");
            System.out.println("░██▄▄▄▄██ ░▓█▄   ▌░▓█▄   ▌   ▒▓▓▄ ▄██▒░██▄▄▄▄██ ▒██▀▀█▄   ");
            System.out.println(" ▓█   ▓██▒░▒████▓ ░▒████▓    ▒ ▓███▀ ░ ▓█   ▓██▒░██▓ ▒██▒ ");
            System.out.println(" ▒▒   ▓▒█░ ▒▒▓  ▒  ▒▒▓  ▒    ░ ░▒ ▒  ░ ▒▒   ▓▒█░░ ▒▓ ░▒▓░ ");
            System.out.println("  ▒   ▒▒ ░ ░ ▒  ▒  ░ ▒  ▒      ░  ▒     ▒   ▒▒ ░  ░▒ ░ ▒░ ");
            System.out.println("  ░   ▒    ░ ░  ░  ░ ░  ░    ░          ░   ▒     ░░   ░  ");
            System.out.println("      ░  ░   ░       ░       ░ ░            ░  ░   ░      ");
            System.out.println("          ░       ░         ░                             ");
        }
    
        //remove car page's logo
        public void removeLogo()
        {
            System.out.println("██▀███  ▓█████  ███▄ ▄███▓ ▒█████   ██▒   █▓▓█████    ");
            System.out.println("▓██ ▒ ██▒▓█   ▀ ▓██▒▀█▀ ██▒▒██▒  ██▒▓██░   █▒▓█   ▀   ");
            System.out.println("▓██ ░▄█ ▒▒███   ▓██    ▓██░▒██░  ██▒ ▓██  █▒░▒███     ");
            System.out.println("▒██▀▀█▄  ▒▓█  ▄ ▒██    ▒██ ▒██   ██░  ▒██ █░░▒▓█  ▄   ");
            System.out.println("░██▓ ▒██▒░▒████▒▒██▒   ░██▒░ ████▓▒░   ▒▀█░  ░▒████▒  ");
            System.out.println("░ ▒▓ ░▒▓░░░ ▒░ ░░ ▒░   ░  ░░ ▒░▒░▒░    ░ ▐░  ░░ ▒░ ░  ");
            System.out.println("  ░▒ ░ ▒░ ░ ░  ░░  ░      ░  ░ ▒ ▒░    ░ ░░   ░ ░  ░  ");
            System.out.println("  ░░   ░    ░   ░      ░   ░ ░ ░ ▒       ░░     ░     ");
            System.out.println("   ░        ░  ░       ░       ░ ░        ░     ░  ░  "); 
            System.out.println("                                         ░            ");
            System.out.println("             ▄████▄   ▄▄▄       ██▀███                ");
            System.out.println("            ▒██▀ ▀█  ▒████▄    ▓██ ▒ ██▒              ");
            System.out.println("            ▒▓█    ▄ ▒██  ▀█▄  ▓██ ░▄█ ▒              ");
            System.out.println("           ▒▓▓▄ ▄██▒░██▄▄▄▄██ ▒██▀▀█▄                 ");
            System.out.println("            ▒ ▓███▀ ░ ▓█   ▓██▒░██▓ ▒██▒              ");
            System.out.println("            ░ ░▒ ▒  ░ ▒▒   ▓▒█░░ ▒▓ ░▒▓░              ");
            System.out.println("              ░  ▒     ▒   ▒▒ ░  ░▒ ░ ▒░              ");
            System.out.println("            ░          ░   ▒     ░░   ░               ");
            System.out.println("            ░ ░            ░  ░   ░                   ");
            System.out.println("            ░                                         ");                                                          
        }

        public void emptyLogo()
        {
            System.out.println("\n");
            System.out.println("=====================");
            System.out.println("|  ╔═╗╔╦╗╔═╗╔╦╗╦ ╦  |");
            System.out.println("|  ║╣ ║║║╠═╝ ║ ╚╦╝  |");
            System.out.println("|  ╚═╝╩ ╩╩   ╩  ╩   |");
            System.out.println("=====================");
        }

    public void exitLogo()
    {
        this.clearScreen();
        System.out.println("▄▄▄█████▓ ██░ ██  ▄▄▄       ███▄    █  ██ ▄█▀ "); 
        System.out.println("▓  ██▒ ▓▒▓██░ ██▒▒████▄     ██ ▀█   █  ██▄█▒  "); 
        System.out.println("▒ ▓██░ ▒░▒██▀▀██░▒██  ▀█▄  ▓██  ▀█ ██▒▓███▄░  "); 
        System.out.println("░ ▓██▓ ░ ░▓█ ░██ ░██▄▄▄▄██ ▓██▒  ▐▌██▒▓██ █▄  "); 
        System.out.println("  ▒██▒ ░ ░▓█▒░██▓ ▓█   ▓██▒▒██░   ▓██░▒██▒ █▄ "); 
        System.out.println("  ▒ ░░    ▒ ░░▒░▒ ▒▒   ▓▒█░░ ▒░   ▒ ▒ ▒ ▒▒ ▓▒ "); 
        System.out.println("    ░     ▒ ░▒░ ░  ▒   ▒▒ ░░ ░░   ░ ▒░░ ░▒ ▒░ "); 
        System.out.println("  ░       ░  ░░ ░  ░   ▒      ░   ░ ░ ░ ░░ ░  "); 
        System.out.println("          ░  ░  ░      ░  ░         ░ ░  ░    "); 
        System.out.println("                                              "); 
        System.out.println("         ▓██   ██▓ ▒█████   █    ██           "); 
        System.out.println("          ▒██  ██▒▒██▒  ██▒ ██  ▓██▒          "); 
        System.out.println("           ▒██ ██░▒██░  ██▒▓██  ▒██░          "); 
        System.out.println("           ░ ▐██▓░▒██   ██░▓▓█  ░██░          "); 
        System.out.println("           ░ ██▒▓░░ ████▓▒░▒▒█████▓           "); 
        System.out.println("            ██▒▒▒ ░ ▒░▒░▒░ ░▒▓▒ ▒ ▒           "); 
        System.out.println("          ▓██ ░▒░   ░ ▒ ▒░ ░░▒░ ░ ░           "); 
        System.out.println("          ▒ ▒ ░░  ░ ░ ░ ▒   ░░░ ░ ░           "); 
        System.out.println("          ░ ░         ░ ░     ░               "); 
        System.out.println("          ░ ░                                 ");    
    }

    public void bookingLogo()
    {
        System.out.println("▄▄▄▄    ▒█████   ▒█████   ██ ▄█▀ ██▓ ███▄    █   ▄████    "); 
        System.out.println("▓█████▄ ▒██▒  ██▒▒██▒  ██▒ ██▄█▒ ▓██▒ ██ ▀█   █  ██▒ ▀█▒  "); 
        System.out.println("▒██▒ ▄██▒██░  ██▒▒██░  ██▒▓███▄░ ▒██▒▓██  ▀█ ██▒▒██░▄▄▄░  "); 
        System.out.println("▒██░█▀  ▒██   ██░▒██   ██░▓██ █▄ ░██░▓██▒  ▐▌██▒░▓█  ██▓  "); 
        System.out.println("░▓█  ▀█▓░ ████▓▒░░ ████▓▒░▒██▒ █▄░██░▒██░   ▓██░░▒▓███▀▒  "); 
        System.out.println("░▒▓███▀▒░ ▒░▒░▒░ ░ ▒░▒░▒░ ▒ ▒▒ ▓▒░▓  ░ ▒░   ▒ ▒  ░▒   ▒   "); 
        System.out.println("▒░▒   ░   ░ ▒ ▒░   ░ ▒ ▒░ ░ ░▒ ▒░ ▒ ░░ ░░   ░ ▒░  ░   ░   "); 
        System.out.println(" ░    ░ ░ ░ ░ ▒  ░ ░ ░ ▒  ░ ░░ ░  ▒ ░   ░   ░ ░ ░ ░   ░   "); 
        System.out.println(" ░          ░ ░      ░ ░  ░  ░    ░           ░       ░   "); 
        System.out.println("      ░                                                   "); 
        System.out.println("            ██▓███   ▄▄▄        ▄████ ▓█████              "); 
        System.out.println("           ▓██░  ██▒▒████▄     ██▒ ▀█▒▓█   ▀              "); 
        System.out.println("           ▓██░ ██▓▒▒██  ▀█▄  ▒██░▄▄▄░▒███                "); 
        System.out.println("           ▒██▄█▓▒ ▒░██▄▄▄▄██ ░▓█  ██▓▒▓█  ▄              "); 
        System.out.println("           ▒██▒ ░  ░ ▓█   ▓██▒░▒▓███▀▒░▒████              "); 
        System.out.println("           ▒▓▒░ ░  ░ ▒▒   ▓▒█░ ░▒   ▒ ░░ ▒░ ░             "); 
        System.out.println("           ░▒ ░       ▒   ▒▒ ░  ░   ░  ░ ░  ░             "); 
        System.out.println("           ░░         ░   ▒   ░ ░   ░    ░                "); 
        System.out.println("                          ░  ░      ░    ░  ░             ");  
    }

    public void ecoLogo()
    {
        System.out.println("▓█████  ▄████▄   ▒█████   ███▄    █  ▒█████   ███▄ ▄███▓▓██   ██▓ ");  
        System.out.println("▓█   ▀ ▒██▀ ▀█  ▒██▒  ██▒ ██ ▀█   █ ▒██▒  ██▒▓██▒▀█▀ ██▒ ▒██  ██▒ ");  
        System.out.println("▒███   ▒▓█    ▄ ▒██░  ██▒▓██  ▀█ ██▒▒██░  ██▒▓██    ▓██░  ▒██ ██░ ");  
        System.out.println("▒▓█  ▄ ▒▓▓▄ ▄██▒▒██   ██░▓██▒  ▐▌██▒▒██   ██░▒██    ▒██   ░ ▐██▓░ ");  
        System.out.println("░▒████▒▒ ▓███▀ ░░ ████▓▒░▒██░   ▓██░░ ████▓▒░▒██▒   ░██▒  ░ ██▒▓░ ");  
        System.out.println("░░ ▒░ ░░ ░▒ ▒  ░░ ▒░▒░▒░ ░ ▒░   ▒ ▒ ░ ▒░▒░▒░ ░ ▒░   ░  ░   ██▒▒▒  ");  
        System.out.println(" ░ ░  ░  ░  ▒     ░ ▒ ▒░ ░ ░░   ░ ▒░  ░ ▒ ▒░ ░  ░      ░ ▓██ ░▒░  ");  
        System.out.println("   ░   ░        ░ ░ ░ ▒     ░   ░ ░ ░ ░ ░ ▒  ░      ░    ▒ ▒ ░░   ");  
        System.out.println("   ░  ░░ ░          ░ ░           ░     ░ ░         ░    ░ ░      ");  
        System.out.println("       ░                                                 ░ ░      ");                            
    }

    public void inteLogo()
    {
        System.out.println("██▓ ███▄    █ ▄▄▄█████▓▓█████  ██▀███   ███▄ ▄███▓▓█████ ▓█████▄  ██▓ ▄▄▄     ▄▄▄█████▓▓█████  ");
        System.out.println("▓██▒ ██ ▀█   █ ▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒▓██▒▀█▀ ██▒▓█   ▀ ▒██▀ ██▌▓██▒▒████▄   ▓  ██▒ ▓▒▓█   ▀ ");
        System.out.println("▒██▒▓██  ▀█ ██▒▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒▓██    ▓██░▒███   ░██   █▌▒██▒▒██  ▀█▄ ▒ ▓██░ ▒░▒███   ");
        System.out.println("░██░▓██▒  ▐▌██▒░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄  ▒██    ▒██ ▒▓█  ▄ ░▓█▄   ▌░██░░██▄▄▄▄██░ ▓██▓ ░ ▒▓█  ▄ ");
        System.out.println("░██░▒██░   ▓██░  ▒██▒ ░ ░▒████▒░██▓ ▒██▒▒██▒   ░██▒░▒████▒░▒████▓ ░██░ ▓█   ▓██▒ ▒██▒ ░ ░▒████▒");
        System.out.println("░▓  ░ ▒░   ▒ ▒   ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░░ ▒░   ░  ░░░ ▒░ ░ ▒▒▓  ▒ ░▓   ▒▒   ▓▒█░ ▒ ░░   ░░ ▒░ ░");
        System.out.println(" ▒ ░░ ░░   ░ ▒░    ░     ░ ░  ░  ░▒ ░ ▒░░  ░      ░ ░ ░  ░ ░ ▒  ▒  ▒ ░  ▒   ▒▒ ░   ░     ░ ░  ░");
        System.out.println(" ▒ ░   ░   ░ ░   ░         ░     ░░   ░ ░      ░      ░    ░ ░  ░  ▒ ░  ░   ▒    ░         ░   ");
        System.out.println(" ░           ░             ░  ░   ░            ░      ░  ░   ░     ░        ░  ░           ░  ░");
        System.out.println("                                                           ░                                   ");  
    }

    public void luxLogo()
    {
        System.out.println("██▓     █    ██ ▒██   ██▒ █    ██  ██▀███ ▓██   ██▓   ");  
        System.out.println("▓██▒     ██  ▓██▒▒▒ █ █ ▒░ ██  ▓██▒▓██ ▒ ██▒▒██  ██▒  ");  
        System.out.println("▒██░    ▓██  ▒██░░░  █   ░▓██  ▒██░▓██ ░▄█ ▒ ▒██ ██░  ");  
        System.out.println("▒██░    ▓▓█  ░██░ ░ █ █ ▒ ▓▓█  ░██░▒██▀▀█▄   ░ ▐██▓░  ");  
        System.out.println("░██████▒▒▒█████▓ ▒██▒ ▒██▒▒▒█████▓ ░██▓ ▒██▒ ░ ██▒▓░  ");  
        System.out.println("░ ▒░▓  ░░▒▓▒ ▒ ▒ ▒▒ ░ ░▓ ░░▒▓▒ ▒ ▒ ░ ▒▓ ░▒▓░  ██▒▒▒   ");  
        System.out.println("░ ░ ▒  ░░░▒░ ░ ░ ░░   ░▒ ░░░▒░ ░ ░   ░▒ ░ ▒░▓██ ░▒░   ");  
        System.out.println("  ░ ░    ░░░ ░ ░  ░    ░   ░░░ ░ ░   ░░   ░ ▒ ▒ ░░    ");  
        System.out.println("    ░  ░   ░      ░    ░     ░        ░     ░ ░       ");  
        System.out.println("                                            ░ ░       ");  
    }

    public void checkLogo()
    {
        System.out.println("                 ▄████▄   ██░ ██ ▓█████  ▄████▄   ██ ▄█▀                "); 
        System.out.println("                 ▒██▀ ▀█  ▓██░ ██▒▓█   ▀ ▒██▀ ▀█   ██▄█▒                            ");
        System.out.println("                 ▒▓█    ▄ ▒██▀▀██░▒███   ▒▓█    ▄ ▓███▄░                            ");
        System.out.println("                 ▒▓▓▄ ▄██▒░▓█ ░██ ▒▓█  ▄ ▒▓▓▄ ▄██▒▓██ █▄                            ");
        System.out.println("                 ▒ ▓███▀ ░░▓█▒░██▓░▒████▒▒ ▓███▀ ░▒██▒ █▄                           ");
        System.out.println("                 ░ ░▒ ▒  ░ ▒ ░░▒░▒░░ ▒░ ░░ ░▒ ▒  ░▒ ▒▒ ▓▒                           ");
        System.out.println("                 ░  ▒    ▒ ░▒░ ░ ░ ░  ░  ░  ▒   ░ ░▒ ▒░                           ");
        System.out.println("                 ░         ░  ░░ ░   ░   ░        ░ ░░ ░                            ");
        System.out.println("                 ░ ░       ░  ░  ░   ░  ░░ ░      ░  ░                              ");
        System.out.println("                 ░                       ░                                          ");
        System.out.println("▄████▄   ▒█████   ███▄    █ ▓█████▄  ██▓▄▄▄█████▓ ██▓ ▒█████   ███▄    █   ");
        System.out.println("▒██▀ ▀█  ▒██▒  ██▒ ██ ▀█   █ ▒██▀ ██▌▓██▒▓  ██▒ ▓▒▓██▒▒██▒  ██▒ ██ ▀█   █  ");
        System.out.println("▒▓█    ▄ ▒██░  ██▒▓██  ▀█ ██▒░██   █▌▒██▒▒ ▓██░ ▒░▒██▒▒██░  ██▒▓██  ▀█ ██▒ ");
        System.out.println("▒▓▓▄ ▄██▒▒██   ██░▓██▒  ▐▌██▒░▓█▄   ▌░██░░ ▓██▓ ░ ░██░▒██   ██░▓██▒  ▐▌██▒ ");
        System.out.println("▒ ▓███▀ ░░ ████▓▒░▒██░   ▓██░░▒████▓ ░██░  ▒██▒ ░ ░██░░ ████▓▒░▒██░   ▓██░ ");
        System.out.println("░ ░▒ ▒  ░░ ▒░▒░▒░ ░ ▒░   ▒ ▒  ▒▒▓  ▒ ░▓    ▒ ░░   ░▓  ░ ▒░▒░▒░ ░ ▒░   ▒ ▒  ");
        System.out.println("░  ▒     ░ ▒ ▒░ ░ ░░   ░ ▒░ ░ ▒  ▒  ▒ ░    ░     ▒ ░  ░ ▒ ▒░ ░ ░░   ░ ▒░   ");
        System.out.println("░        ░ ░ ░ ▒     ░   ░ ░  ░ ░  ░  ▒ ░  ░       ▒ ░░ ░ ░ ▒     ░   ░ ░  ");
        System.out.println("░ ░          ░ ░           ░    ░     ░            ░      ░ ░           ░  ");
        System.out.println("░                             ░                                            ");
        System.out.println("\n");
    }

    public void goodBad()
    {
        System.out.println("\n");
        System.out.println("================================");
        System.out.println("|  ┌─┐┌─┐┌─┐┌┬┐  |  ┌┐ ┌─┐┌┬┐  |");
        System.out.println("|  │ ┬│ ││ │ ││  |  ├┴┐├─┤ ││  |");
        System.out.println("|  └─┘└─┘└─┘─┴┘  |  └─┘┴ ┴─┴┘  |");
        System.out.println("================================");
        System.out.println("\n");
    }

    public void paymentMethodLogo()
    {
        System.out.println("\n");
        System.out.println("============================================================================================");
        System.out.println("|  ╔╦╗╔╗╔╔═╗  |  ╔═╗╦═╗╔═╗╔╦╗╦╔╦╗  ╔═╗╔═╗╦═╗╔╦╗  |  ╔═╗╔╗╔╦  ╦╔╗╔╔═╗  ╔╗ ╔═╗╔╗╔╦╔═╦╔╗╔╔═╗  |"); 
        System.out.println("|   ║ ║║║║ ╦  |  ║  ╠╦╝║╣  ║║║ ║   ║  ╠═╣╠╦╝ ║║  |  ║ ║║║║║  ║║║║║╣   ╠╩╗╠═╣║║║╠╩╗║║║║║ ╦  |"); 
        System.out.println("|   ╩ ╝╚╝╚═╝  |  ╚═╝╩╚═╚═╝═╩╝╩ ╩   ╚═╝╩ ╩╩╚══╩╝  |  ╚═╝╝╚╝╩═╝╩╝╚╝╚═╝  ╚═╝╩ ╩╝╚╝╩ ╩╩╝╚╝╚═╝  |"); 
        System.out.println("============================================================================================");
        System.out.println("\n");
    }

    public void payHistoryLogo()
    {
        System.out.println("▄▄▄▄    ▒█████   ▒█████   ██ ▄█▀▓█████ ▓█████▄   ");
        System.out.println("▓█████▄ ▒██▒  ██▒▒██▒  ██▒ ██▄█▒ ▓█   ▀ ▒██▀ ██▌ ");
        System.out.println("▒██▒ ▄██▒██░  ██▒▒██░  ██▒▓███▄░ ▒███   ░██   █▌ ");
        System.out.println("▒██░█▀  ▒██   ██░▒██   ██░▓██ █▄ ▒▓█  ▄ ░▓█▄   ▌ ");
        System.out.println("░▓█  ▀█▓░ ████▓▒░░ ████▓▒░▒██▒ █▄░▒████▒░▒████▓  ");
        System.out.println("░▒▓███▀▒░ ▒░▒░▒░ ░ ▒░▒░▒░ ▒ ▒▒ ▓▒░░ ▒░ ░ ▒▒▓  ▒  ");
        System.out.println("▒░▒   ░   ░ ▒ ▒░   ░ ▒ ▒░ ░ ░▒ ▒░ ░ ░  ░ ░ ▒  ▒  ");
        System.out.println(" ░    ░ ░ ░ ░ ▒  ░ ░ ░ ▒  ░ ░░ ░    ░    ░ ░  ░  ");
        System.out.println(" ░          ░ ░      ░ ░  ░  ░      ░  ░   ░     ");
        System.out.println("      ░                                  ░       ");
        System.out.println("       ██▓███   ▄▄▄        ▄████ ▓█████          ");
        System.out.println("      ▓██░  ██▒▒████▄     ██▒ ▀█▒▓█   ▀          ");
        System.out.println("      ▓██░ ██▓▒▒██  ▀█▄  ▒██░▄▄▄░▒███            ");
        System.out.println("      ▒██▄█▓▒ ▒░██▄▄▄▄██ ░▓█  ██▓▒▓█  ▄          ");
        System.out.println("      ▒██▒ ░  ░ ▓█   ▓██▒░▒▓███▀▒░▒████▒         ");
        System.out.println("      ▒▓▒░ ░  ░ ▒▒   ▓▒█░ ░▒   ▒ ░░ ▒░ ░         ");
        System.out.println("      ░▒ ░       ▒   ▒▒ ░  ░   ░  ░ ░  ░         ");
        System.out.println("      ░░         ░   ▒   ░ ░   ░    ░            ");
        System.out.println("                     ░  ░      ░    ░  ░         ");
        System.out.println("                                                 ");
    }

    public void receiptLogo()
    {
        System.out.println("██▀███  ▓█████  ▄████▄  ▓█████  ██▓ ██▓███  ▄▄▄█████▓  ");
        System.out.println("▓██ ▒ ██▒▓█   ▀ ▒██▀ ▀█  ▓█   ▀ ▓██▒▓██░  ██▒▓  ██▒ ▓▒ ");
        System.out.println("▓██ ░▄█ ▒▒███   ▒▓█    ▄ ▒███   ▒██▒▓██░ ██▓▒▒ ▓██░ ▒░ ");
        System.out.println("▒██▀▀█▄  ▒▓█  ▄ ▒▓▓▄ ▄██▒▒▓█  ▄ ░██░▒██▄█▓▒ ▒░ ▓██▓ ░  ");
        System.out.println("░██▓ ▒██▒░▒████▒▒ ▓███▀ ░░▒████▒░██░▒██▒ ░  ░  ▒██▒ ░  ");
        System.out.println("░ ▒▓ ░▒▓░░░ ▒░ ░░ ░▒ ▒  ░░░ ▒░ ░░▓  ▒▓▒░ ░  ░  ▒ ░░    ");
        System.out.println("  ░▒ ░ ▒░ ░ ░  ░  ░  ▒    ░ ░  ░ ▒ ░░▒ ░         ░     ");
        System.out.println("  ░░   ░    ░   ░           ░    ▒ ░░░         ░       ");
        System.out.println("   ░        ░  ░░ ░         ░  ░ ░                     ");
        System.out.println("                ░                                      ");
    }

    public void thankYou()
    {
        System.out.println("┌┬┐┬ ┬┌─┐┌┐┌┬┌─  ┬ ┬┌─┐┬ ┬");
        System.out.println(" │ ├─┤├─┤│││├┴┐  └┬┘│ ││ │");
        System.out.println(" ┴ ┴ ┴┴ ┴┘└┘┴ ┴   ┴ └─┘└─┘\n");
    }

    public void cancelFullLogo()
    {
        System.out.println("\n");
        System.out.println("=====================================================================");
        System.out.println("|    ┌─┐┌─┐┌┐┌┌─┐┌─┐┬     |    ┌─┐┬ ┬┬  ┬  ┌─┐┌─┐┬ ┬┌┬┐┌─┐┌┐┌┌┬┐    |");
        System.out.println("|    │  ├─┤││││  ├┤ │     |    ├┤ │ ││  │  ├─┘├─┤└┬┘│││├┤ │││ │     |");
        System.out.println("|    └─┘┴ ┴┘└┘└─┘└─┘┴─┘   |    └  └─┘┴─┘┴─┘┴  ┴ ┴ ┴ ┴ ┴└─┘┘└┘ ┴     |");
        System.out.println("=====================================================================");
        System.out.println("\n");
    }

    public void warningLogo()
    {
        this.clearScreen(); 

        System.out.println("█     █░ ▄▄▄       ██▀███   ███▄    █  ██▓ ███▄    █   ▄████   ");
        System.out.println("▓█░ █ ░█░▒████▄    ▓██ ▒ ██▒ ██ ▀█   █ ▓██▒ ██ ▀█   █  ██▒ ▀█▒ ");
        System.out.println("▒█░ █ ░█ ▒██  ▀█▄  ▓██ ░▄█ ▒▓██  ▀█ ██▒▒██▒▓██  ▀█ ██▒▒██░▄▄▄░ ");
        System.out.println("░█░ █ ░█ ░██▄▄▄▄██ ▒██▀▀█▄  ▓██▒  ▐▌██▒░██░▓██▒  ▐▌██▒░▓█  ██▓ ");
        System.out.println("░░██▒██▓  ▓█   ▓██▒░██▓ ▒██▒▒██░   ▓██░░██░▒██░   ▓██░░▒▓███▀▒ ");
        System.out.println("░ ▓░▒ ▒   ▒▒   ▓▒█░░ ▒▓ ░▒▓░░ ▒░   ▒ ▒ ░▓  ░ ▒░   ▒ ▒  ░▒   ▒  ");
        System.out.println("  ▒ ░ ░    ▒   ▒▒ ░  ░▒ ░ ▒░░ ░░   ░ ▒░ ▒ ░░ ░░   ░ ▒░  ░   ░  ");
        System.out.println("  ░   ░    ░   ▒     ░░   ░    ░   ░ ░  ▒ ░   ░   ░ ░ ░ ░   ░  ");
        System.out.println("    ░          ░  ░   ░              ░  ░           ░       ░  ");
        System.out.println("\n");
    }

    public void penalty()
    {
        this.clearScreen();

        System.out.println("██▓███  ▓█████  ███▄    █  ▄▄▄       ██▓  ▄▄▄█████▓▓██   ██▓  "); 
        System.out.println("▓██░  ██▒▓█   ▀  ██ ▀█   █ ▒████▄    ▓██▒  ▓  ██▒ ▓▒ ▒██  ██▒ "); 
        System.out.println("▓██░ ██▓▒▒███   ▓██  ▀█ ██▒▒██  ▀█▄  ▒██░  ▒ ▓██░ ▒░  ▒██ ██░ "); 
        System.out.println("▒██▄█▓▒ ▒▒▓█  ▄ ▓██▒  ▐▌██▒░██▄▄▄▄██ ▒██░  ░ ▓██▓ ░   ░ ▐██▓░ "); 
        System.out.println("▒██▒ ░  ░░▒████▒▒██░   ▓██░ ▓█   ▓██▒░██████▒▒██▒ ░   ░ ██▒▓░ "); 
        System.out.println("▒▓▒░ ░  ░░░ ▒░ ░░ ▒░   ▒ ▒  ▒▒   ▓▒█░░ ▒░▓  ░▒ ░░      ██▒▒▒  "); 
        System.out.println("░▒ ░      ░ ░  ░░ ░░   ░ ▒░  ▒   ▒▒ ░░ ░ ▒  ░  ░     ▓██ ░▒░  "); 
        System.out.println("░░          ░      ░   ░ ░   ░   ▒     ░ ░   ░       ▒ ▒ ░░   "); 
        System.out.println("            ░  ░         ░       ░  ░    ░  ░        ░ ░      "); 
        System.out.println("                                                     ░ ░      "); 
        System.out.println("\n"); 
    }

    public void staffUserName()
    {
        System.out.println("==============================");
        System.out.println("|  ┬ ┬┌─┐┌─┐┬─┐┌┐┌┌─┐┌┬┐┌─┐  |");
        System.out.println("|  │ │└─┐├┤ ├┬┘│││├─┤│││├┤   |"); 
        System.out.println("|  └─┘└─┘└─┘┴└─┘└┘┴ ┴┴ ┴└─┘  |");
        System.out.println("==============================\n");
    }

    public void staffUserPass()
    {
        System.out.println("\n==============================");
        System.out.println("|  ┌─┐┌─┐┌─┐┌─┐┬ ┬┌─┐┬─┐┌┬┐  |");
        System.out.println("|  ├─┘├─┤└─┐└─┐││││ │├┬┘ ││  |");
        System.out.println("|  ┴  ┴ ┴└─┘└─┘└┴┘└─┘┴└──┴┘  |");
        System.out.println("==============================\n");
    }

    public void qrCode()
    {
        System.out.println("\n████████████████████████████████████████████████████████████████");
        System.out.println("████████████████████████████████████████████████████████████████");
        System.out.println("████              ██████████  ██    ██  ██████              ████");
        System.out.println("████  ██████████  ██    ██  ██  ██    ████████  ██████████  ████");
        System.out.println("████  ██      ██  ██  ██          ██████  ████  ██      ██  ████");
        System.out.println("████  ██      ██  ██    ████  ████    ██    ██  ██      ██  ████");
        System.out.println("████  ██      ██  ████  ██    ██    ██      ██  ██      ██  ████");
        System.out.println("████  ██████████  ████      ████  ████████  ██  ██████████  ████");
        System.out.println("████              ██  ██  ██  ████  ██  ██  ██              ████");
        System.out.println("████████████████████  ██████████    ██      ████████████████████");
        System.out.println("████  ██████████  ██      ████████    ██  ██    ████      ██████");
        System.out.println("████      ██  ██████  ██        ██        ██  ██    ██    ██████");
        System.out.println("████    ████████    ████████        ██  ██████████  ████████████");
        System.out.println("████████  ████  ████    ██  ██  ████████  ██  ██████  ██████████");
        System.out.println("██████████        ██████  ██        ██████  ██    ████████  ████");
        System.out.println("████  ████  ██████    ████  ██████  ██  ██  ██      ████    ████");
        System.out.println("████    ██████  ██  ██  ████████  ██████  ██████    ██  ██  ████");
        System.out.println("██████████  ████  ████        ████    ██  ██  ██  ██    ████████");
        System.out.println("████  ████████  ██    ██    ██  ████████  ████      ██      ████");
        System.out.println("████    ██        ██    ██  ████  ██  ██  ████████    ████  ████");
        System.out.println("████  ████████████  ████    ██  ██████████  ██  ████████████████");
        System.out.println("████  ██████        ██      ████  ██  ██            ██      ████");
        System.out.println("████████████████████  ██  ██  ████████████  ██████    ██████████");
        System.out.println("████              ████  ████  ██████        ██  ██      ████████");
        System.out.println("████  ██████████  ████  ██    ██    ████    ██████  ████████████");
        System.out.println("████  ██      ██  ████  ██  ██  ████                  ██████████");
        System.out.println("████  ██      ██  ██████    ██  ████          ██████      ██████");
        System.out.println("████  ██      ██  ████  ████          ██                  ██████");
        System.out.println("████  ██████████  ████  ████  ████      ██████    ██    ██  ████");
        System.out.println("████              ██        ████  ██████  ████      ██  ████████");
        System.out.println("████████████████████████████████████████████████████████████████");
        System.out.println("████████████████████████████████████████████████████████████████\n");
    }
}
