package com.garufa.tabletoproulette;

import android.content.res.XmlResourceParser;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from the API provided by BoardGameGeek.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class BoardGameGeekXmlParser {
    private static final String ns = null;

    public List<Game> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return  readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Game> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Game> games = new ArrayList<Game>();

        parser.require(XmlPullParser.START_TAG, ns, "boardgames");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("boardgame")) {
                games.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return games;
    }

    private Game readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "boardgame");
        int bgg_id = 0, min_players = 0, max_players = 0, min_play_time = 0, max_play_time = 0;
        String name = "Splendor", description = null, game_mechanic = null, image_url = null;
        double rating = 0;

        bgg_id = Integer.parseInt(parser.getAttributeValue(ns, "objectid"));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag_name = parser.getName();
            switch (tag_name){
//                case "name":
//                    String primary = parser.getAttributeValue(null, "primary");
//                    if (primary != null && primary.equals("true")){
//                        name = readName(parser); break;
//                    } else {
//                        skip(parser);
//                    }
                case "minplayers": min_players = readMinPlayers(parser); break;
                case "maxplayers": max_players = readMaxPlayers(parser); break;
                case "minplaytime": min_play_time = readMinPlayTime(parser); break;
                case "maxplaytime": max_play_time = readMaxPlayTime(parser); break;
                case "description": description = readDescription(parser); break;
                case "boardgamemechanic": game_mechanic = readGameMechanic(parser); break;
                case "statistics": rating = readStats(parser); break;
                case "thumbnail": image_url = readImageUrl(parser);
                    image_url = "http:" + image_url; break;
                default: skip(parser);
            }
        }
        return new Game(name, bgg_id, min_players, max_players, min_play_time, max_play_time,
                description, game_mechanic, rating, image_url);
    }

    private double readStats(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "statistics");
        double rating = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String tag_name = parser.getName();
            if (tag_name.equals("ratings")) {
                rating = readRating(parser);
            } else {
                skip(parser);
            }
        }

        return rating;
    }

    private double readRating(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "ratings");
        double rating = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String tag_name = parser.getName();
            if (tag_name.equals("bayesaverage")) {
                rating = readAverage(parser);
            } else {
                skip(parser);
            }
        }

        return rating;
    }

    private double readAverage(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "bayesaverage");
        String rating = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "bayesaverage");
        return Double.parseDouble(rating);
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = "";
        // Need to make sure the name is the primary
        String tag = parser.getName();
        String primary = parser.getAttributeValue(ns, "primary");
        if (tag.equals("name")) {
            if (primary.equals("true")){
                name = readText(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private Integer readMinPlayers(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "minplayers");
        String min_players = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "minplayers");
        return Integer.parseInt(min_players);
    }

    private Integer readMaxPlayers(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "maxplayers");
        String max_players = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "maxplayers");
        return Integer.parseInt(max_players);
    }

    private Integer readMinPlayTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "minplaytime");
        String min_play_time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "minplaytime");
        return Integer.parseInt(min_play_time);
    }

    private Integer readMaxPlayTime(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "maxplaytime");
        String max_play_time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "maxplaytime");
        return Integer.parseInt(max_play_time);
    }

    private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readGameMechanic(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "boardgamemechanic");
        String game_mechanic = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "boardgamemechanic");
        return game_mechanic;
    }

    private String readImageUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "thumbnail");
        String image_url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "thumbnail");
        return image_url;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
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
