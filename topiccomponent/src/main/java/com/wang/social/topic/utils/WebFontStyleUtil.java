package com.wang.social.topic.utils;

/**
 * Created by Chao on 2017/7/7.
 */

public class WebFontStyleUtil {
    private static final String fontStyle = "<style>@font-face{\n" +
            "  font-family: 'kt';\n" +
            "  src: url('file:///android_asset/kaiti.ttf');\n" +
            "}@font-face{\n" +
            "  font-family: 'pfb';\n" +
            "  src: url('file:///android_asset/pingfang_bz.ttf');\n" +
            "}@font-face{\n" +
            "   font-family: 'pfc';\n" +
            "   src: url('file:///android_asset/pingfang_cu.ttf');\n" +
            "}@font-face{\n" +
            "  font-family: 'pfx';\n" +
            "   src: url('file:///android_asset/pingfang_xi.ttf');\n" +
            "}@font-face{\n" +
            "  font-family: 'st';\n" +
            "   src: url('file:///android_asset/songti.ttf');\n" +
            "}@font-face{\n" +
            "   font-family: 'ww';\n" +
            "   src: url('file:///android_asset/wawa.TTF');\n" +
            "}@font-face{\n" +
            "   font-family: 'yy';\n" +
            "   src: url('file:///android_asset/youyuan.TTF');\n" +
            "}" +
            "</style>";

    public static String getFontStyle() {
        return fontStyle;
    }

    public static String getHtmlFormat(String title, String content) {
        return "<!DOCTYPE html>" +
                "<html lang='zh'>" +
                "<head>" +
                "<meta charset='utf-8' />" +
                "<title>" + title + "</title>" +

                "<meta name='HandheldFriendly' content='True'/>" +
                "<meta name='MobileOptimized' content='320' />" +
                "<meta name='viewport' content='width=device-width, initial-scale=1' />"
                +
                "<style>"
                +
                "img {\n" +
                "\tmargin-top: 5px;\n" +
                "\tmargin-bottom: 5px;\n" +
                "\tmax-width: 100%;\n" +
                "\t-webkit-border-radius: 20px;\n" +
                "\tborder-radius: 20px;\n" +
                "\tborder: 0px solid black;\n" +
                "\tp{line-height: 1.6rem !important;\n" +
                "}}"
                +
                "#audioImg{\n" +
                "  margin-top:5px;\n" +
                "  margin-bottom:5px;\n" +
                "\n" +
                "  webkit-border-radius: 0px;border-radius: 0px;border: 0px solid black;\n" +
                "}"
                +
                "body {\n" +
                "  word-wrap:break-word;\n" +
                "  word-break:break-all;\n" +
                "  overflow: hidden;\n" +
                "  display: table;\n" +
                "  table-layout: fixed;\n" +
                "  width: 98%;\n" +
                "  min-height:100%;\n" +
                "}"
                +
                "blockquote{\n" +
                "    background-color: whitesmoke;\n" +
                "    border-left: 4px solid #999999;\n" +
                "    font-size: 15px;\n" +
                "    font-weight: 100;\n" +
                "    margin-left: 0px;\n" +
                "    margin-right : 0px;\n" +
                "}"
                +
                "</style>"
                +
                "<script>"
                +
                PLAY_AUDIO_S
                +
                "</script>" +
                "</head>" +
                "<body>" +
                "<audio src=\"\" id=\"musicPlay\"> </audio>"
                + content +
                "</body>" +
                "</html>";
    }

    private final static String PLAY_AUDIO_S = "" +
            "function playAudio(url) {\n" +
            "    var musicPlay = document.getElementById('musicPlay');\n" +
            "    var originUrl = musicPlay.getAttribute('src');\n" +
            "    if (originUrl == url) {\n" +
            "        if (musicPlay.paused == false) {\n" +
            "            musicPlay.pause();\n" +
            "        } else {\n" +
            "            musicPlay.play();\n window.App.webViewAudioClick();\n" +
            "        }\n" +
            "    } else {\n" +
            "        musicPlay.pause();\n" +
            "        musicPlay.setAttribute('src', url);\n" +
            "        musicPlay.play();\n window.App.webViewAudioClick();\n" +
            "    }\n" +
            "}";

//    String oldStr = "function playAudio(url){" +
//            "var musicPlay=document.getElementById('musicPlay');" +
//            "musicPlay.setAttribute('src',url);" +
//            "musicPlay.play();" +
//            "}";

//    private final static String PLAY_AUDIO_S = "function playAudio(url)" +
//            "{var musicPlay=document.getElementById('musicPlay');" +
//            "var musicPlay=document.getElementById('musicPlay');" +
//            "var originUrl = musicPlay.getAttribute('src');" +
//            "if(originUrl == url)" +
//            "{" +
//            "if(musicPlay.paused == false)" +
//            "{" +
//            "musicPlay.pause();" +
//            "}" +
//            "else" +
//            "{" +
//            "musicPlay.play();" +
//            "}}" +
//            "else" +
//            "{musicPlay.pause();musicPlay.setAttribute('src',url);musicPlay.play();}}";
}
