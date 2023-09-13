import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
//пытался сделать с помощью сериализации, но выдает ошибку
public class Save implements Serializable {
    public final  Game m;
    private static final long serialVersionUID = 1L;

    public Save() throws IOException {
        m = new Game();
    }

    public void saveGame() throws IOException {
        Save save = new Save();
        FileOutputStream outputStream = new FileOutputStream("dex.tsv");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(save);
        objectOutputStream.flush();
        objectOutputStream.close();

    }
    @Override
    public String toString() {
        return "SavedGame{" + Arrays.toString(m.createApple().toArray());
    }
}
