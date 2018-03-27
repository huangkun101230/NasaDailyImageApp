package nz.ac.vuw.ecs.nasadailyimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nz.ac.vuw.ecs.nasadailyimage.utils.ECSAuthenticator;
import nz.ac.vuw.ecs.nasadailyimage.utils.PictureUtils;

public class MainActivity extends AppCompatActivity {


    private static final String URL = "http://www.nasa.gov/rss/dyn/image_of_the_day.rss";

    private Image image = null;

    private Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MainTask().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup the Proxy for using the internet access from workstations at school
        Authenticator.setDefault(new ECSAuthenticator());
        System.setProperty("http.proxyHost", "www-cache.ecs.vuw.ac.nz");
        System.setProperty("http.proxyPort", "8080");
        System.setProperty("http.nonProxyHosts", "localhost|*.vuw.ac.nz|*.victoria.ac.nz|*.elvis.ac.nz");




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_share);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Share",shareBtn).show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
          if (id==R.id.action_share){
//            Button shareBtn = (Button) findViewById(R.id.action_share);
//            shareBtn.setOnClickListener(new View.OnClickListener(){
//                public void onClick(View v){
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_EMAIL,"emailaddress@emailaddress.com");
                    intent.putExtra(Intent.EXTRA_SUBJECT,image.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT,"IMAGE TITLE: "+image.getTitle()+
                            "\n"+"IMAGE DESCRIPTION: "+image.getDescription()+
                            "\n"+"IMAGE URL: "+image.getUrl());
//                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(image.getUrl()));

                    startActivity(Intent.createChooser(intent,"Send Email"));
              return true;
//                }
//            });
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is to reset the display on screen after
     * retrieving the latest image from RSS.
     *
     * @param
     * @return Nothing.
     */
    public void resetDisplay() {
        //Update the text content of the TextView widget "imageTitle"
        TextView imageTitle = (TextView) findViewById(R.id.imageTitle);
        imageTitle.setText(image.getTitle());

        //Update the text content of the TextView widget "imageDate"
        TextView imageDate = (TextView) findViewById(R.id.imageDate);
        imageDate.setText(image.getDate());

        //Update the text content of the TextView widget "imageDescription"
        TextView imageDescription = (TextView) findViewById(R.id.imageDescription);
        imageDescription.setText(image.getDescription());

        //Update the image of the ImageView widget "imageView" with bitmap
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }


    /**
     * This inner class inherits from AsyncTask which performs background
     * operations and publish results on the UI thread.
     */
    public class MainTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Invoke the function to retrieve the image from NASA RSS feed.
            Bitmap bitmap = PictureUtils.getBitmapFromURL(URL);
            processFeed();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Invoke the function to reset display after the latest daily image obtained.
            resetDisplay();
        }

        /**
         * This method is used to retrieve the latest daily image from NASA RSS feed.
         *
         * @param
         * @return Nothing.
         */
        public void processFeed() {
            StringBuffer stringBuffer = new StringBuffer();
            try {
                SAXParserFactory saxParserFactory =
                        SAXParserFactory.newInstance();
                SAXParser parser = saxParserFactory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                IotdHandler iotdHandler = new IotdHandler();
                reader.setContentHandler(iotdHandler);

                InputStream inputStream = new URL(URL).openStream();
                reader.parse(new InputSource(inputStream));
                image = iotdHandler.getImage();
                bitmap = PictureUtils.getBitmapFromURL(image.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

