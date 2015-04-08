package com.epsi.hugo.rssreaderhg2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsi.hugo.rssreaderhg2.pck_classes.PostAdapter;
import com.epsi.hugo.rssreaderhg2.pck_classes.PostData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class Display_feed extends ActionBarActivity {

    private static ProgressDialog progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loading dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Fetchning RSS datas...");
        progress.show();

        // Get the message from the intent
        Intent intent = getIntent();
        String urlString = intent.getStringExtra("RSS_URL");

        Log.i("URL_FEED", ""+urlString);



        /*// Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(urlString);

        // Set the text view as the activity layout
        setContentView(textView);*/

        new InputStreamTask().execute(urlString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_feed, menu);
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

        return super.onOptionsItemSelected(item);

    }

    private class InputStreamTask extends AsyncTask<String, Void, InputStream>{
        @Override
        protected InputStream doInBackground(String... url){
            try {
                //get the xml content of the rss feed url
                InputStream in = new URL(url[0]).openStream();
                Log.i("URL[0]",""+url[0]);
                return in;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //onPostExecute is called AFTER doInBackground

        @Override
        protected void onPostExecute(InputStream input){
            Log.i("INPUT:", ""+input);
            new getXmlPost().execute(input);
        }
    }

    private class getXmlPost extends AsyncTask<InputStream, Void, List<XmlParser.Entry>>
    {
        @Override
        protected List<XmlParser.Entry> doInBackground(InputStream... inputStream)
        {
            XmlParser xmlParser = new XmlParser();

            try{
                Log.d("INPUT:", ""+inputStream[0]);


                if(inputStream[0] != null)
                {
                    Log.d("PARSE", "Parse des posts");

                    //parse des posts from the asynchronous inputstream
                    List<XmlParser.Entry> posts = xmlParser.parse(inputStream[0]);
                    Log.d("POSTS:", ""+posts);
                    return posts;
                }

            }
            catch (IOException | XmlPullParserException e)
            {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<XmlParser.Entry> posts)
        {
            if(posts != null)
            {
                //we create an ArrayList of PostData object
                ArrayList<PostData> listPostData = new ArrayList<PostData>();

                //save each post in an ArrayList
                for (XmlParser.Entry post : posts) {

                    PostData postData = new PostData(post.title, post.description, post.link);
                    //populate the arrayList with rss feed data
                    listPostData.add(postData);
                    Log.i("TITLE", ""+post.title);
                }


                PostAdapter postAdapter = new PostAdapter(Display_feed.this, listPostData);

                setContentView(R.layout.activity_display_feed);

                final ListView listView = (ListView) findViewById(R.id.listViewPostData);

                Log.i("POSTADAPTER:", ""+postAdapter);
                Log.i("LISTVIEW:", ""+listView);

                listView.setAdapter(postAdapter);

                progress.dismiss();

                //open the post url when user click on the post
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View child, int position, long id) {

                        Intent intentBrowser = new Intent(Intent.ACTION_VIEW);

                        //get the id of current post
                        PostData postData = (PostData) listView.getItemAtPosition(position);

                        //put inside the indent the url of the choosen post
                        intentBrowser.setData(Uri.parse(postData.getPostURL()));

                        //launch the intent browser containing the url
                        startActivity(intentBrowser);

                    }
                });
            }
            else
            {
                Log.e("EROrR", "THE URL IS INCORRECT");
                Toast.makeText(getApplicationContext(), "URL is invalid. Please retry with a valid URL :)", Toast.LENGTH_LONG).show();
            }

        }
    }
}
