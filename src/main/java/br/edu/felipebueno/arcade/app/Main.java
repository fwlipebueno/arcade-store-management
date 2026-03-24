package br.edu.felipebueno.arcade.app;

import br.edu.felipebueno.arcade.app.console.ConsoleMenu;
import br.edu.felipebueno.arcade.app.context.ApplicationContext;
import br.edu.felipebueno.arcade.domain.model.SistemaLojaArcade;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext();
        SistemaLojaArcade sistema = applicationContext.criarSistema();

        ConsoleMenu consoleMenu = new ConsoleMenu(sistema);
        consoleMenu.iniciar();
    }
}
