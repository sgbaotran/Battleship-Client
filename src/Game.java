
import control.Controller;
import model.Model;
import view.View;

public class Game {
    public static void main(String[] args) throws Exception {

        View view = new View();

        Model model = new Model();

        Controller controller = new Controller(view, model);

    }
}
