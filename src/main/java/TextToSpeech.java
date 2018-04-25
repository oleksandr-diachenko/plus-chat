import java.io.IOException;
import java.io.InputStream;


import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * @author GOXR3PLUS
 */
public class TextToSpeech {

    private SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

    public void speak(String text) throws IOException, JavaLayerException {
        InputStream mp3Data = synthesizer.getMP3Data(text);

        Player player = new Player(mp3Data);
        player.play();
    }
}