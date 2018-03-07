package sup.savemeaspot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**Denna klass öppnar vyn för val av kategori när man sparar en Spot**/
public class SaveSpotCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_spot_category);
    }
}
