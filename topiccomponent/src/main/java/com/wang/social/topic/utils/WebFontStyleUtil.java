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
                "<meta name='viewport' content='width=device-width, initial-scale=1' />" +
                "<style>" +
                "#audioImg{" +
                " min-width:14px!important;" +
                " max-width:14px!important;" +
                " height:14px!important;" +
                " margin:0;" +
                " padding:0;}" +
                "img{" +
                "-webkit-border-radius: 20px;border-radius: 20px;border: 0px solid black;" +
                "margin-top:10px;" +
                "margin-bottom:10px;" +
                "max-width:  98%;" +
                "min-width:  98%;" +
                "}" +
                "body{" +
                "width: 97.5%;" +
                "word-wrap:break-word;" +
                "}" +
                "</style>" +
                "<script>" +
                "function playAudio(url){" +
                "var musicPlay=document.getElementById('musicPlay');" +
                "musicPlay.setAttribute('src',url);" +
                "musicPlay.play();" +
                "}" +
                "</script>" +
                "</head>" +
                "<body>" +
                "<audio src=\"\" id=\"musicPlay\"> </audio>"
                + content +
                "</body>" +
                "</html>";
    }

//    String oldStr = "function playAudio(url){" +
//            "var musicPlay=document.getElementById('musicPlay');" +
//            "musicPlay.setAttribute('src',url);" +
//            "musicPlay.play();" +
//            "}";

    String str = "function playAudio(url)" +
            "{var musicPlay=document.getElementById('musicPlay');" +
            "var musicPlay=document.getElementById('musicPlay');" +
            "var originUrl = musicPlay.getAttribute('src');" +
            "if(originUrl == url)" +
            "{" +
            "if(musicPlay.paused == false)" +
            "{" +
            "musicPlay.pause();" +
            "}" +
            "else" +
            "{" +
            "musicPlay.play();" +
            "}}" +
            "else" +
            "{musicPlay.pause();musicPlay.setAttribute('src',url);musicPlay.play();}}";
}
