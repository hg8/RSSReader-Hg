package com.epsi.hugo.rssreaderhg2;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XmlParser {
	private static final String ns=null;

    public XmlParser() {
    }

    public List<Entry>parse(InputStream in) throws XmlPullParserException,IOException
	{ 
	    try {
		    XmlPullParser parser=Xml.newPullParser();
		    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		    parser.setInput(in,null);
		    parser.nextTag();
		    return reedFeed(parser);
	    }finally{
	        in.close();
	    }

	}

	private List<Entry>reedFeed(XmlPullParser parser) throws XmlPullParserException,IOException
	{ 
	    List<Entry> entries = new ArrayList<Entry>();
	    parser.require(XmlPullParser.START_TAG, ns, "rss");
	    //String name1=parser.getName();
	    System.out.println(parser.getName());
	    //String tag=;
	    while (parser.nextTag()!=XmlPullParser.END_TAG) {

	        if(parser.getEventType()!=XmlPullParser.START_TAG){
	            continue;
	        }
	        String name=parser.getName();

	        if(name.equals("channel")){
	            String tag=parser.getName();
	            System.out.println("tag"+tag);
	        }
	        else if(name.equals("item")){
	            System.out.println("tag"+parser.getName());
	            entries.add(readEntry(parser));
	        }else{
	            skip(parser);
	        }


	    }
	    return entries;

	}

	public static class Entry implements Serializable
	{
		private static final long serialVersionUID = -2090100866116438791L;
	
		public String title=null;
	    public String link=null;
	    public String description=null;
	     private Entry(String title, String description, String link) {
	            this.title = title;
	            this.description = description;
	            this.link = link;
	        }
	}
	
	private Entry readEntry(XmlPullParser parser) throws XmlPullParserException,IOException

	{
	    parser.require(XmlPullParser.START_TAG, ns, "item");
	    System.out.println(parser.getDepth());
	     String title = null;
	     String description = null;
	     String link = null;
	     while (parser.next()!=XmlPullParser.END_TAG) {
	        if(parser.getEventType()!=XmlPullParser.START_TAG){
	            continue;
	        }
	        String name=parser.getName();
	        if(name.equals("title")){
	             title = readTitle(parser);
	        }else if (name.equals("link")) {
	             link = readLink(parser);
	        }else if(name.equals("description")){
	            description = readSummary(parser);
	        }else{
	            skip(parser);
	        }
	    }
	    return new Entry(title, description, link);

	}
	private String readTitle(XmlPullParser parser)throws IOException,XmlPullParserException
	{
	    //parser.require(XmlPullParser.START_TAG, ns, "item");
	    //parser.next();
	    parser.require(XmlPullParser.START_TAG, ns, "title");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "title");
	    return title;

	}
	private String readLink(XmlPullParser parser)throws IOException,XmlPullParserException
	{
	     String link="";
	    parser.require(XmlPullParser.START_TAG, ns, "link");
	    link=parser.nextText();
	    parser.require(XmlPullParser.END_TAG, ns, "link");
	    return link;

	}
	 private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String result = "";
	        if (parser.next() == XmlPullParser.TEXT) {
	            result = parser.getText();
	            parser.nextTag();
	        }
	        return result;
	    }
	 private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, "description");
	        String summary = readText(parser);
	        //System.out.println(summary);
	        parser.require(XmlPullParser.END_TAG, ns, "description");
	        return summary;
	    }
	 private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
}

